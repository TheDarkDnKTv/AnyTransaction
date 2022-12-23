package thedarkdnktv.anytransaction.data.entity;

import thedarkdnktv.anytransaction.data.PaymentType;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "transactions_customer_id", columnList  = "customer_id"),
    @Index(name = "transactions_timestamp", columnList  = "timestamp")
})
public class TransactionEntity {

    @Id
    @Column(updatable = false)
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "transactions_to_customer_fk"))
    private CustomerEntity customer;

    @Column(name = "payment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "payment_amount", nullable = false)
    private double paymentAmount;

    @Column(nullable = false)
    private int points;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

    private String metadata;

    public long getId() {
        return id;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
