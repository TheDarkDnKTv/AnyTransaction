package thedarkdnktv.anytransaction.domain;

import org.springframework.stereotype.Component;
import thedarkdnktv.anytransaction.data.PaymentType;

@Component
public class PaymentHelper {

    public <T> IPaymentCalculator provideCalculator(double initialPrice, T paymentType) {
        if (paymentType instanceof PaymentType pt) {
            return new PaymentType.Calculator(pt, initialPrice);
        }

        throw new IllegalArgumentException("No payment calculator was found for type of " + paymentType.getClass());
    }

    public <T> ITransactionMetadataValidator provideTransactionMetaValidator(T paymentType) {
        if (paymentType instanceof PaymentType pt) {
            return new PaymentType.MetaValidator(pt);
        }

        throw new IllegalArgumentException("No transaction validator was found for type of " + paymentType.getClass());
    }
}
