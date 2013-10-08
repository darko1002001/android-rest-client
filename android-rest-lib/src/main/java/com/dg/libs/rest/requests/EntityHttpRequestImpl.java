package com.dg.libs.rest.requests;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.client.EntityBodyRestClient;
import com.dg.libs.rest.client.Rest;
import com.dg.libs.rest.parsers.HttpResponseParser;

public abstract class EntityHttpRequestImpl<T> extends BaseHttpRequestImpl<T> {

  public static final String TAG = EntityHttpRequestImpl.class.getSimpleName();
  protected final EntityBodyRestClient client;

  public EntityHttpRequestImpl(final Context context,
      RequestMethod requestMethod,
      final HttpResponseParser<T> parser,
      final HttpCallback<T> callback) {
    super(context, parser, callback);
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
