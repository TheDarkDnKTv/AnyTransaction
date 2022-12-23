package thedarkdnktv.anytransaction.domain.enums;

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
}
