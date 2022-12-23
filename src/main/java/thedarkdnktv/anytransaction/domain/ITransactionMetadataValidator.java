package thedarkdnktv.anytransaction.domain;

import jakarta.validation.ValidationException;
import java.util.Map;

public interface ITransactionMetadataValidator {

    boolean validate(Map<String, Object> jsonTree);

    ValidationException error();
}
