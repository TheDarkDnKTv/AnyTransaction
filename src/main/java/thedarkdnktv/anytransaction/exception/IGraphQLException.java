package thedarkdnktv.anytransaction.exception;

import org.springframework.graphql.execution.ErrorType;

public interface IGraphQLException {

    ErrorType getType();

    String getMessage();

    default boolean showPath() {
        return true;
    }

    default boolean showLocation() {
        return true;
    }
}
