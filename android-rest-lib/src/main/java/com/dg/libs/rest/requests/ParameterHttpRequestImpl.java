package com.dg.libs.rest.requests;

import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.client.ParametersRestClient;
import com.dg.libs.rest.client.Rest;
import com.dg.libs.rest.parsers.HttpResponseParser;

public abstract class ParameterHttpRequestImpl<T> extends BaseHttpRequestImpl<T> {

  public static final String TAG = ParameterHttpRequestImpl.class.getSimpleName();
  protected ParametersRestClient client;

  public ParameterHttpRequestImpl(final RequestMethod requestMethod,
      final HttpResponseParser<T> parser,
      final HttpCallback<T> callback) {
    super(parser, callback);
    client = new ParametersRestClient();
    client.setRequestMethod(requestMethod);
  }

  @Override
  public Rest getClient() {
    return client;
  }
}
