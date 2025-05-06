package com.lrbell.llamabot.errors;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.ErrorType;

public interface GraphQlResolvableError {

    ErrorType getErrorType();

    String getMessage();

    default GraphQLError resolveGraphQlError(DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .message(getMessage())
                .errorType(getErrorType())
                .build();
    }

}
