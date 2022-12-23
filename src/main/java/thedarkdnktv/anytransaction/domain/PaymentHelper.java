package thedarkdnktv.anytransaction.domain;

import org.springframework.stereotype.Component;
import thedarkdnktv.anytransaction.domain.enums.PaymentType;

@Component
public class PaymentHelper {

    public <T> IPaymentCalculator provideCalculator(double initialPrice, T paymentType) {
        if (paymentType instanceof PaymentType pt) {
            return new PaymentCalculatorImpl(pt, initialPrice);
        }

        throw new IllegalArgumentException("No payment calculator was found for type of " + paymentType.getClass());
    }

    public <T> ITransactionMetadataValidator provideTransactionMetaValidator(T paymentType) {
        if (paymentType instanceof PaymentType pt) {
            return new PaymentTransactionMetaValidator(pt);
        }

        throw new IllegalArgumentException("No transaction validator was found for type of " + paymentType.getClass());
    }
}
