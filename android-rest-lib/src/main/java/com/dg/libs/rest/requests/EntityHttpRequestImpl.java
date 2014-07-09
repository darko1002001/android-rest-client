package com.dg.libs.rest.requests;

import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.client.EntityBodyRestClient;
import com.dg.libs.rest.client.Rest;
import com.dg.libs.rest.parsers.HttpResponseParser;
import org.apache.http.HttpEntity;

public abstract class EntityHttpRequestImpl<T> extends BaseHttpRequestImpl<T> {

  public static final String TAG = EntityHttpRequestImpl.class.getSimpleName();
  private final EntityBodyRestClient client;

  public EntityHttpRequestImpl(RequestMethod requestMethod,
      final HttpResponseParser<T> parser,
      final HttpCallback<T> callback) {
    super(parser, callback);
    client = new EntityBodyRestClient();
    client.setRequestMethod(requestMethod);
  }

  @Override
  protected void doBeforeRunRequestInBackgroundThread() {
    super.doBeforeRunRequestInBackgroundThread();
    client.setEntity(getEntity());
  }

  public abstract HttpEntity getEntity();

  @Override
  public Rest getClient() {
    return client;
  }
}
