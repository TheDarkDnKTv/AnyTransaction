package thedarkdnktv.anytransaction.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import thedarkdnktv.anytransaction.domain.entity.CustomerEntity;
import thedarkdnktv.anytransaction.domain.entity.TransactionEntity;
import thedarkdnktv.anytransaction.domain.enums.PaymentType;
import thedarkdnktv.anytransaction.domain.graphql.inputs.TransactionInput;
import thedarkdnktv.anytransaction.domain.repository.CustomerRepository;
import thedarkdnktv.anytransaction.domain.repository.TransactionRepository;

import jakarta.validation.ValidationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @BeforeEach
    public void createMocks() {
        when(customerRepository.findAll()).thenReturn(
            List.of(new CustomerEntity(0L))
        );

        when(customerRepository.findById(0L)).thenReturn(
            Optional.of(new CustomerEntity(0L))
        );

        when(transactionRepository.save(Mockito.any(TransactionEntity.class)))
            .thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    public void integration_add_default() {
        var trans = new TransactionInput(
            0L,
            "100.0",
            0.9D,
            PaymentType.CASH.name(),
            "2022-12-24T00:30:00Z",
            ""
        );

        var result = transactionService.processTransaction(trans);

        assertEquals(result.getPoints(), 5);
        assertEquals(Double.parseDouble(result.getFinalPrice()), 90D);
    }

    @Test
    public void integration_add_cash_on_delivery() {
        var trans = new TransactionInput(
            0L,
            "60.0",
            1.0D,
            PaymentType.CASH_ON_DELIVERY.name(),
            "2022-12-24T00:40:00Z",
            "{\"courier\":\"YAMATO\"}"
        );

        var result = transactionService.processTransaction(trans);

        assertEquals(result.getPoints(), 3);
        assertEquals(Double.parseDouble(result.getFinalPrice()), 60D);
    }

    @Test
    public void integration_add_cash_on_delivery_wrong() {
        var trans = new TransactionInput(
            0L,
            "100.0",
            0.9D,
            PaymentType.CASH_ON_DELIVERY.name(),
            "2022-12-24T00:30:00Z",
            ""
        );

        assertThrows(ValidationException.class, () -> transactionService.processTransaction(trans));
    }

    @Test
    public void integration_add_amex() {
        var trans = new TransactionInput(
            0L,
            "200.0",
            0.96D,
            PaymentType.AMEX.name(),
            "2022-12-24T01:30:00Z",
            "{\"last4\":\"7777\"}"
        );

        var result = transactionService.processTransaction(trans);

        assertEquals(result.getPoints(), 4);
        assertEquals(Double.parseDouble(result.getFinalPrice()), 196D);
    }

    @Test
    public void integration_add_mastercard_wrong() {
        var trans = new TransactionInput(
            0L,
            "150.0",
            0.96D,
            PaymentType.AMEX.name(),
            "2022-12-24T01:30:00Z",
            "{\"last4\":\"something not number\"}"
        );

        assertThrows(ValidationException.class, () -> transactionService.processTransaction(trans));
    }

    @Test
    public void integration_add_grab_pay() {
        var trans = new TransactionInput(
            0L,
            "20.0",
            1D,
            PaymentType.GRAB_PAY.name(),
            "2022-12-24T01:31:00Z",
            ""
        );

        var result = transactionService.processTransaction(trans);

        assertEquals(result.getPoints(), 0);
        assertEquals(Double.parseDouble(result.getFinalPrice()), 20D);
    }

    @Test
    public void integration_add_cheque_wrong() {
        var trans = new TransactionInput(
            0L,
            "1000.0",
            0.95D,
            PaymentType.CHEQUE.name(),
            "2022-12-24T02:30:00Z",
            """
            {
                "chequeNumber": "DA787DA678NWAMOILA"
            }
            """
        );

        assertThrows(ValidationException.class, () -> transactionService.processTransaction(trans));
    }

    @Test
    public void integration_add_cheque() {
        var trans = new TransactionInput(
            0L,
            "1000.0",
            0.95D,
            PaymentType.CHEQUE.name(),
            "2022-12-24T02:30:00Z",
            """
            {
                "chequeNumber": "DA787DA678NWAMOILA",
                "accountName": "TATSUO DENJI",
                "bankName": "Yamagawa Chuou Ginko",
                "bankAddress": "Japan, Nagano, 1-1-1"
            }
            """
        );

        var result = transactionService.processTransaction(trans);

        assertEquals(result.getPoints(), 0);
        assertEquals(Double.parseDouble(result.getFinalPrice()), 950D);
    }

    @Test
    public void integration_add_bank_wrong() {
        var trans = new TransactionInput(
            0L,
            "700.0",
            0.99D,
            PaymentType.BANK_TRANSFER.name(),
            "2022-12-24T02:30:00Z",
            """
            {
                "dawdaw": "DA787DA678NWAMOILA"
            }
            """
        );

        assertThrows(ValidationException.class, () -> transactionService.processTransaction(trans));
    }

    @Test
    public void integration_add_bank() {
        var trans = new TransactionInput(
            0L,
            "700.0",
            0.99D,
            PaymentType.BANK_TRANSFER.name(),
            "2022-12-24T02:35:00Z",
            """
            {
                "accountNumber": "HABA48JP904321094104242",
                "accountName": "TATSUO DENJI",
                "bankName": "Yamagawa Chuou Ginko",
                "bankAddress": "Japan, Nagano, 1-1-1"
            }
            """
        );

        var result = transactionService.processTransaction(trans);

        assertEquals(result.getPoints(), 0);
        assertEquals(Double.parseDouble(result.getFinalPrice()), 700D);
    }
}
