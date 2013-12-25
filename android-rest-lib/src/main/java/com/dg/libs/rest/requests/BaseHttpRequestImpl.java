package com.dg.libs.rest.requests;

import android.content.Context;
import com.araneaapps.android.libs.asyncrunners.models.RequestOptions;
import com.araneaapps.android.libs.asyncrunners.models.TaskStore;
import com.araneaapps.android.libs.logger.ALog;
import com.dg.libs.rest.HttpRequest;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.Rest;
import com.dg.libs.rest.domain.ResponseStatus;
import com.dg.libs.rest.handlers.DefaultResponseStatusHandler;
import com.dg.libs.rest.handlers.ResponseHandler;
import com.dg.libs.rest.handlers.ResponseStatusHandler;
import com.dg.libs.rest.handlers.UIThreadResponseHandler;
import com.dg.libs.rest.parsers.HttpResponseParser;

public abstract class BaseHttpRequestImpl<T> implements HttpRequest {

  public static final String TAG = BaseHttpRequestImpl.class.getSimpleName();

  private final Context context;

  private final HttpResponseParser<T> parser;
  private ResponseHandler<T> handler;
  private ResponseStatusHandler statusHandler;
  private final HttpCallback<T> callback;

  RequestOptions requestOptions = null;

  public BaseHttpRequestImpl(final Context context, final HttpResponseParser<T> parser,
                             final HttpCallback<T> callback) {
    super();
    this.parser = parser;
    this.callback = callback;
    this.context = context.getApplicationContext();
  }

  public abstract Rest getClient();

  public Context getContext() {
    return context;
  }

  protected HttpCallback<T> getCallback() {
    return callback;
  }

  public void addParam(final String key, final String value) {
    getClient().addParam(key, value);
  }

  public ResponseHandler<T> getHandler() {
    return handler;
  }

  public void addHeader(final String key, final String value) {
    getClient().addHeader(key, value);
  }

  @Override
  public void run() {
    doBeforeRunRequestInBackgroundThread();
    runRequest(getUrl());
    doAfterRunRequestInBackgroundThread();
  }

  protected abstract String getUrl();

  /**
   * Set a custom handler that will be triggered when the response returns
   * either Success or Fail. You can choose where this info is sent. **Default**
   * is the UIThreadREsponseHandler implementation which runs the appropriate
   * callback on the UI thread.
   *
   * @param handler
   */
  public void setResponseHandler(ResponseHandler<T> handler) {
    this.handler = handler;
  }

  /**
   * By default success is a code in the range of 2xx. Everything else triggers
   * an Error. You can set a handler which will take into account your own
   * custom error codes to determine if the response is a success or fail.
   *
   * @param statusHandler
   */
  public void setStatusHandler(ResponseStatusHandler statusHandler) {
    this.statusHandler = statusHandler;
  }

  protected HttpResponseParser<T> getParser() {
    return parser;
  }

  protected void runRequest(final String url) {

    if (handler == null) {
      handler = new UIThreadResponseHandler<T>(callback);
    }
    if (statusHandler == null) {
      statusHandler = new DefaultResponseStatusHandler();
    }

    final Rest client = getClient();
    try {
      client.setUrl(url);
      client.execute();
    } catch (final Exception e) {
      ResponseStatus responseStatus = ResponseStatus.getConnectionErrorStatus();
      ALog.d(TAG, responseStatus.toString(), e);
      handler.handleError(responseStatus);
      return;
    }

    final ResponseStatus status = client.getResponseStatus();
    ALog.d(TAG, status.toString());
    if (handleResponseStatus(status)) {
      return;
    }
    try {
      final T responseData = parser.parse(client.getResponse());
      client.closeStream();
      handler.handleSuccess(responseData);
    } catch (final Exception e) {
      ResponseStatus responseStatus = ResponseStatus.getParseErrorStatus();
      ALog.d(TAG, responseStatus.toString(), e);
      handler.handleError(responseStatus);
    }

  }

  /**
   * Return true if you have handled the status yourself.
   * */
  protected boolean handleResponseStatus(ResponseStatus status) {
    if (statusHandler.hasErrorInStatus(status)) {
      handler.handleError(status);
      return true;
    }
    return false;
  }

  @Override
  public void executeAsync() {
    TaskStore.get(context).queue(this, getRequestOptions());
  }

  public RequestOptions getRequestOptions() {
    return requestOptions;
  }

  public void setRequestOptions(RequestOptions requestOptions) {
    this.requestOptions = requestOptions;
  }

  /**
   * Use this method to add the required data to the request. This will happen
   * in the background thread which enables you to pre-process the parameters,
   * do queries etc..
   */
  protected void doBeforeRunRequestInBackgroundThread() {
  }

  /**
   * This will happen in the background thread which enables you to do some
   * cleanup in the background after the request finishes
   */
  protected void doAfterRunRequestInBackgroundThread() {

  }
}
