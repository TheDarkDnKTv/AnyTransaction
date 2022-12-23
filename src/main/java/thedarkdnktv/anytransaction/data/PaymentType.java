package thedarkdnktv.anytransaction.data;

import jakarta.validation.ValidationException;
import thedarkdnktv.anytransaction.domain.DeliveryType;
import thedarkdnktv.anytransaction.domain.IPaymentCalculator;
import thedarkdnktv.anytransaction.domain.ITransactionMetadataValidator;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public enum PaymentType {

    CASH            (0.9D , 1.0D , 0.05D),
    CASH_ON_DELIVERY(1.0D , 1.02D, 0.05D),
    VISA            (0.95D, 1.0D , 0.03D),
    MASTERCARD      (0.95D, 1.0D , 0.03D),
    AMEX            (0.98D, 1.01D, 0.02D),
    JCB             (0.95D, 1.0D , 0.05D),
    LINE_PAY        (0.01D),
    PAYPAY          (0.01D),
    POINTS          (0.00D),
    GRAB_PAY        (0.01D),
    BANK_TRANSFER   (0.00D),
    CHEQUE          (0.9D , 1.0D , 0D);

    public final double priceMinModifier;
    public final double priceMaxModifier;
    public final double pointModifier;

    PaymentType(double pointModifier) {
        this(1D, 1D, pointModifier);
    }

    PaymentType(double priceMinModifier, double priceMaxModifier, double pointModifier) {
        this.priceMinModifier   = priceMinModifier;
        this.priceMaxModifier   = priceMaxModifier;
        this.pointModifier      = pointModifier;
    }

    public static PaymentType of(String value) {
        try {
            return valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    public record Calculator(PaymentType type, double initialPrice) implements IPaymentCalculator {

        @Override
        public double calculatePrice(double discountMod) {
            // Make sure that discountMod is not undervalued, or not exceed allowed.
            discountMod = Math.max(discountMod, type.priceMinModifier);
            discountMod = Math.min(discountMod, type.priceMaxModifier);
            return initialPrice * discountMod;
        }

        @Override
        public int calculatePoints() {
            return (int) (initialPrice * type.pointModifier);
        }
    }

    public static class MetaValidator implements ITransactionMetadataValidator {

        private static final Pattern DIGITS_REGEX;
        static {
            DIGITS_REGEX = Pattern.compile("\\d{4}");
        }

        private final PaymentType type;
        private ValidationException exception;

        public MetaValidator(PaymentType type) {
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
                case CASH_ON_DELIVERY: {
                    var value = map.get("courier");
                    if (value == null || DeliveryType.of(value.toString()).isEmpty()) {
                        throw new ValidationException("Invalid courier type");
                    }

                    break;
                }
                case VISA:
                case MASTERCARD:
                case AMEX:
                case JCB: {
                    var value = map.get("last4");
                    if (value == null || !DIGITS_REGEX.matcher(value.toString()).matches()) {
                        throw new ValidationException("Invalid card's last 4 digit");
                    }

                    break;
                }
                case BANK_TRANSFER:
                case CHEQUE: {
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

                    break;
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
}
