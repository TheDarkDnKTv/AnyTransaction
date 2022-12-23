package thedarkdnktv.anytransaction.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thedarkdnktv.anytransaction.data.PaymentType;
import thedarkdnktv.anytransaction.data.entity.TransactionEntity;
import thedarkdnktv.anytransaction.data.graphql.inputs.SalesRequestInput;
import thedarkdnktv.anytransaction.data.graphql.inputs.TransactionInput;
import thedarkdnktv.anytransaction.data.graphql.types.SalesDto;
import thedarkdnktv.anytransaction.data.graphql.types.TransactionResult;
import thedarkdnktv.anytransaction.data.repository.CustomerRepository;
import thedarkdnktv.anytransaction.data.repository.TransactionRepository;
import thedarkdnktv.anytransaction.domain.PaymentHelper;
import thedarkdnktv.anytransaction.exception.NotFoundException;
import thedarkdnktv.anytransaction.utils.Validators;

import java.util.*;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private CustomerRepository customerRepository;
    private PaymentHelper calculationProvider;
    private ObjectMapper mapper;

    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Autowired
    public void setCalculationProvider(PaymentHelper calculationProvider) {
        this.calculationProvider = calculationProvider;
    }

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Transactional
    public List<SalesDto> getSales(Long customerId, SalesRequestInput request) {
        var from = this.validateLocalTimestamp(request.startDateTime());
        var to = this.validateLocalTimestamp(request.endDateTime());
        return transactionRepository.findAllByCustomerIdAndTimestampBetween(customerId, from, to);
    }

    @Transactional
    public TransactionResult processTransaction(TransactionInput input) {
        var customer = this.customerRepository.findById(input.customerId())
            .orElseThrow(NotFoundException::new);

        var date = this.validateLocalTimestamp(input.datetime());
        var type = Optional.ofNullable(input.paymentMethod())
            .map(v -> v.replace(' ', '_').toUpperCase())
            .map(PaymentType::of)
            .orElseThrow(ValidationException::new);
        var price = Validators.validateDouble(input.price());
        var meta = this.validateTransactionMetadata(type, input.additionalItem());
        var calculator = this.calculationProvider.provideCalculator(price, type);

        var transaction = new TransactionEntity();

        {
            transaction.setTimestamp(date);
            transaction.setCustomer(customer);
            transaction.setPaymentType(type);
            transaction.setMetadata(meta);
            transaction.setPaymentAmount(calculator.calculatePrice(input.priceModifier()));
            transaction.setPoints(calculator.calculatePoints());
        }

        transaction = this.transactionRepository.save(transaction);
        return new TransactionResult(transaction.getPaymentAmount(), transaction.getPoints());
    }

    private Date validateLocalTimestamp(String localDateTime) {
        var dateTime = Validators.validateZonedDateTime(localDateTime);
        return Date.from(dateTime.toInstant());
    }

    private String validateTransactionMetadata(PaymentType type, String info) {
        var map = this.getTransactionMetadata(info);
        var validator = this.calculationProvider.provideTransactionMetaValidator(type);
        if (validator.validate(map)) {
            return info;
        } else {
            throw validator.error();
        }
    }

    private Map<String, Object> getTransactionMetadata(String info) {
        try {
            return this.mapper.readValue(info, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new ValidationException("Json is corrupted", e);
        }
    }
}
