package thedarkdnktv.anytransaction.domain.service;

import org.junit.jupiter.api.Test;
import thedarkdnktv.anytransaction.domain.PaymentTransactionMetaValidator;
import thedarkdnktv.anytransaction.domain.enums.DeliveryType;
import thedarkdnktv.anytransaction.domain.enums.PaymentType;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentTransactionMetaValidatorTest {

    static final Map<String, Object> COURIER_INFO;
    static final Map<String, Object> COURIER_INFO_INVALID;
    static final Map<String, Object> CARD_INFO;
    static final Map<String, Object> CARD_INFO_INVALID;
    static final Map<String, Object> BANK_INFO;
    static final Map<String, Object> CHEQUE_INFO;

    @Test
    public void cash_on_delivery_1() {
        var validator = new PaymentTransactionMetaValidator(PaymentType.CASH_ON_DELIVERY);
        assertTrue(validator.validate(COURIER_INFO));
    }

    @Test
    public void cash_on_delivery_2() {
        var validator = new PaymentTransactionMetaValidator(PaymentType.CASH_ON_DELIVERY);
        assertFalse(validator.validate(Collections.emptyMap()));
    }

    @Test
    public void cash_on_delivery_3() {
        var validator = new PaymentTransactionMetaValidator(PaymentType.CASH_ON_DELIVERY);
        assertFalse(validator.validate(COURIER_INFO_INVALID));
    }

    @Test
    public void mastercard() {
        var validator = new PaymentTransactionMetaValidator(PaymentType.MASTERCARD);
        assertTrue(validator.validate(CARD_INFO));
    }

    @Test
    public void visa() {
        var validator = new PaymentTransactionMetaValidator(PaymentType.VISA);
        assertFalse(validator.validate(Collections.emptyMap()));
    }

    @Test
    public void jcb() {
        var validator = new PaymentTransactionMetaValidator(PaymentType.JCB);
        assertFalse(validator.validate(CARD_INFO_INVALID));
    }

    @Test
    public void line_pay() {
        var validator = new PaymentTransactionMetaValidator(PaymentType.LINE_PAY);
        assertTrue(validator.validate(Collections.emptyMap()));
    }

    @Test
    public void bank_transfer_1() {
        var validator = new PaymentTransactionMetaValidator(PaymentType.BANK_TRANSFER);
        assertTrue(validator.validate(BANK_INFO));
    }

    @Test
    public void bank_transfer_2() {
        var validator = new PaymentTransactionMetaValidator(PaymentType.BANK_TRANSFER);
        assertFalse(validator.validate(Collections.emptyMap()));
    }

    @Test
    public void cheque_1() {
        var validator = new PaymentTransactionMetaValidator(PaymentType.CHEQUE);
        assertTrue(validator.validate(CHEQUE_INFO));
    }

    @Test
    public void cheque_2() {
        var validator = new PaymentTransactionMetaValidator(PaymentType.CHEQUE);
        assertFalse(validator.validate(Collections.emptyMap()));
    }

    static {
        COURIER_INFO            = Map.of("courier", DeliveryType.SAGAWA);
        COURIER_INFO_INVALID    = Map.of("courier", "UBER_EATS");
        CARD_INFO               = Map.of("last4", "5329");
        CARD_INFO_INVALID       = Map.of("last4", "5XX9");
        BANK_INFO               = Map.of(
            "accountNumber" , "DAWJD2NAWJ5342KDAFGWA5434",
            "accountName"   , "TEST 123",
            "bankName"      , "SOME BANK",
            "bankAddress"   , "Solar system, 3th planet"
        );
        CHEQUE_INFO             = Map.of(
            "chequeNumber"  , "FUNOIFOIA542fd",
            "accountName"   , "TEST 123",
            "bankName"      , "SOME BANK",
            "bankAddress"   , "Solar system, 3th planet"
        );
    }
}
