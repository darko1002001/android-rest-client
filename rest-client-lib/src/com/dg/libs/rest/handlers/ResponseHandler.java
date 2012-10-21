package com.dg.libs.rest.handlers;

import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public interface ResponseHandler<T> {
    public HttpCallback<T> getCallback();

    public void handleSuccess(final T responseData);

    public void handleError(final ResponseStatus status);
}
