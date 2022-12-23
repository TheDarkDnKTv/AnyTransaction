package thedarkdnktv.anytransaction.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ValidationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class GraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof IGraphQLException ge) {
            var builder = GraphqlErrorBuilder.newError()
                .errorType(ge.getType());

            if (ge.getMessage() != null) {
                builder.message(ge.getMessage());
            } else {
                builder.message("");
            }

            if (ge.showPath()) {
                builder.path(env.getExecutionStepInfo().getPath());
            }

            if (ge.showLocation()) {
                builder.location(env.getField().getSourceLocation());
            }

            return builder.build();
        } else if (ex instanceof ValidationException ve) {
            var builder = GraphqlErrorBuilder.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .path(env.getExecutionStepInfo().getPath());

            if (ve.getMessage() != null) {
                builder.message(ve.getMessage());
            } else {
                builder.message("Validation error");
            }

            return builder.build();
        }

        return super.resolveToSingleError(ex, env);
    }
}
