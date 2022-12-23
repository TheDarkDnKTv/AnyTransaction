package thedarkdnktv.anytransaction.domain.enums;

import java.util.Optional;

public enum DeliveryType {
    YAMATO,
    SAGAWA;

    public static Optional<DeliveryType> of(String value) {
        try {
            return Optional.of(valueOf(value));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
