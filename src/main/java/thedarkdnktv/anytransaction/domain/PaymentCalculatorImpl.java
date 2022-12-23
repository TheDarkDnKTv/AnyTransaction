package thedarkdnktv.anytransaction.domain;

import thedarkdnktv.anytransaction.domain.enums.PaymentType;

public record PaymentCalculatorImpl(PaymentType type, double initialPrice) implements IPaymentCalculator {

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
