package com.dg.libs.rest.requests;

import android.content.Context;

import com.dg.libs.android.logger.ALog;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.client.Rest;
import com.dg.libs.rest.client.StringBodyRestClient;
import com.dg.libs.rest.exceptions.HttpException;
import com.dg.libs.rest.parsers.HttpResponseParser;

public abstract class StringBodyHttpRequestImpl<T> extends BaseHttpRequestImpl<T> {

    public static final String TAG = StringBodyHttpRequestImpl.class.getSimpleName();
    private final StringBodyRestClient client;

    public StringBodyHttpRequestImpl(final Context context,
    		RequestMethod requestMethod,
            final HttpResponseParser<T> parser,
            final HttpCallback<T> callback) {
        super(context, parser, callback);
        client = new StringBodyRestClient();
        client.setRequestMethod(requestMethod);
    }

    @Override
    protected void prepareRequest() throws HttpException {
        prepareParams();
        setBody(bodyContents());
    }

    private void setBody(final String body) {
        client.setBody(bodyContents());
    }
    
    public abstract String bodyContents();

    @Override
    public Rest getClient() {
        return client;
    }
}
