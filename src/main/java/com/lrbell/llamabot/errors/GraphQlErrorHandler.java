package com.lrbell.llamabot.errors;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

/**
 * Error handler for GraphQL response.
 */
@Component
public class GraphQlErrorHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(final Throwable ex, final DataFetchingEnvironment env) {
        if (ex instanceof GraphQlResolvableError) {
            return ((GraphQlResolvableError) ex).resolveGraphQlError(env);
        } else if (ex instanceof Exception) {
            return  GraphqlErrorBuilder.newError(env)
                    .message(ex.getMessage())
                    .errorType(ErrorType.INTERNAL_ERROR)
                    .build();
        }
        return null;
    }

}
