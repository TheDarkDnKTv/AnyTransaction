package thedarkdnktv.anytransaction.domain.graphql.inputs;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record SalesRequestInput(
    @NotEmpty @DateTimeFormat(iso = ISO.DATE_TIME) String startDateTime,
    @NotEmpty @DateTimeFormat(iso = ISO.DATE_TIME) String endDateTime
) {}
