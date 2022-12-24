package thedarkdnktv.anytransaction.domain;

import jakarta.validation.ValidationException;
import thedarkdnktv.anytransaction.domain.enums.DeliveryType;
import thedarkdnktv.anytransaction.domain.enums.PaymentType;

import java.util.Map;
import java.util.regex.Pattern;

public class PaymentTransactionMetaValidator implements ITransactionMetadataValidator {

    private static final Pattern DIGITS_REGEX;

    static {
        DIGITS_REGEX = Pattern.compile("\\d{4}");
    }

    private final PaymentType type;
    private ValidationException exception;

    public PaymentTransactionMetaValidator(PaymentType type) {
        this.type = type;
    }

    @Override
    public boolean validate(Map<String, Object> map) {
        try {
            this.validateInternal(map);
        } catch (ValidationException e) {
            this.exception = e;
        }

        return error() == null;
    }

    private void validateInternal(Map<String, Object> map) {
        switch (type) {
            case CASH_ON_DELIVERY -> {
                var value = map.get("courier");
                if (value == null || DeliveryType.of(value.toString()).isEmpty()) {
                    throw new ValidationException("Invalid courier type");
                }
            }
            case VISA, MASTERCARD, AMEX, JCB -> {
                var value = map.get("last4");
                if (value == null || !DIGITS_REGEX.matcher(value.toString()).matches()) {
                    throw new ValidationException("Invalid card's last 4 digit");
                }
            }
            case BANK_TRANSFER, CHEQUE -> {
                // banking info
                this.validateBanking(map);

                Object value;
                if (type == PaymentType.CHEQUE) {
                    // in case of cheque, checking it number
                    value = map.get("chequeNumber");
                    if (value == null || value.toString().isBlank()) {
                        throw new ValidationException("Cheque number is invalid");
                    }
                } else {
                    // otherwise it's banking, so checking account number
                    value = map.get("accountNumber");
                    if (value == null || value.toString().isBlank()) {
                        throw new ValidationException("Account number is invalid");
                    }
                }
            }
        }
    }

    private void validateBanking(Map<String, Object> map) {
        Object value;

        /*
            Banking info usually includes:
            - Account Name
            - Bank name
            - Address
            and more, actually
         */

        value = map.get("accountName");
        if (value == null || value.toString().isBlank()) {
            throw new ValidationException("Account name is invalid");
        }

        value = map.get("bankName");
        if (value == null || value.toString().isBlank()) {
            throw new ValidationException("Bank name number is invalid");
        }

        value = map.get("bankAddress");
        if (value == null || value.toString().isBlank()) {
            throw new ValidationException("Bank address number is invalid");
        }
    }

    @Override
    public ValidationException error() {
        return exception;
    }
}
