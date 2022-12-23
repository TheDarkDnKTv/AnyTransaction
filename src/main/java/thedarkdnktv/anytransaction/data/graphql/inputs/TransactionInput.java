package thedarkdnktv.anytransaction.data.graphql.inputs;

import jakarta.validation.constraints.NotEmpty;

public record TransactionInput(
    long customerId,
    @NotEmpty String price,
    double priceModifier,
    @NotEmpty String paymentMethod,
    @NotEmpty String datetime,
    String additionalItem
) {}
