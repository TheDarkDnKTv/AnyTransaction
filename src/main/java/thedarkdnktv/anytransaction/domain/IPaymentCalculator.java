package thedarkdnktv.anytransaction.domain;

public interface IPaymentCalculator {

    /**
     * Used for no discount applicable
     */
    default double calculatePrice() {
        return this.calculatePrice(1.0D);
    }

    double calculatePrice(double discountMod);

    int calculatePoints();
}
