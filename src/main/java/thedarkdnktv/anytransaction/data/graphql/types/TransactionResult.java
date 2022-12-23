package thedarkdnktv.anytransaction.data.graphql.types;

public class TransactionResult {

    private String finalPrice;
    private int points;

    public TransactionResult(double finalPrice, int points) {
        this.finalPrice = Double.toString(finalPrice);
        this.points = points;
    }

    public TransactionResult() {}

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
