package com.dg.libs.rest.requests;

import android.content.Context;

import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.client.ParametersRestClient;
import com.dg.libs.rest.client.Rest;
import com.dg.libs.rest.exceptions.HttpException;
import com.dg.libs.rest.parsers.HttpResponseParser;

public abstract class ParameterHttpRequestImpl<T> extends BaseHttpRequestImpl<T> {

    public static final String TAG = ParameterHttpRequestImpl.class.getSimpleName();
    protected ParametersRestClient client;

    public ParameterHttpRequestImpl(final Context context,
            final RequestMethod requestMethod,
            final HttpResponseParser<T> parser,
            final HttpCallback<T> callback) {
        super(context, parser, callback);
        client = new ParametersRestClient();
        client.setMethod(requestMethod);
    }

    @Override
    protected void prepareAndExecuteRequest() throws HttpException {
        prepareParams();
        client.execute();
    }

    @Override
    public Rest getClient() {
        return client;
    }

    public void addParam(final String key, final String value) {
        client.addParam(key, value);
    }
}
