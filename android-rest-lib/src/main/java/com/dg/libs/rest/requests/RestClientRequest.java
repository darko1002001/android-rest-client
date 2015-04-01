package com.dg.libs.rest.requests;

import android.app.Activity;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.araneaapps.android.libs.asyncrunners.models.RequestOptions;
import com.araneaapps.android.libs.asyncrunners.models.TaskStore;
import com.dg.libs.rest.HttpRequest;
import com.dg.libs.rest.RestClientConfiguration;
import com.dg.libs.rest.authentication.AuthenticationProvider;
import com.dg.libs.rest.callbacks.ActivityBoundHttpCallback;
import com.dg.libs.rest.callbacks.BoundCallback;
import com.dg.libs.rest.callbacks.FragmentBoundHttpCallback;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.RequestMethod;
import com.dg.libs.rest.domain.ResponseStatus;
import com.dg.libs.rest.handlers.DefaultResponseStatusHandler;
import com.dg.libs.rest.handlers.ResponseHandler;
import com.dg.libs.rest.handlers.ResponseStatusHandler;
import com.dg.libs.rest.handlers.UIThreadResponseHandler;
import com.dg.libs.rest.parsers.HttpResponseParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

public abstract class RestClientRequest<T> implements HttpRequest {

  private String TAG = RestClientRequest.class.getSimpleName();

  private HttpResponseParser<T> parser;
  private ResponseHandler<T> handler;
  private ResponseStatusHandler statusHandler;
  private BoundCallback<T> callback;
  private Request.Builder request = new Request.Builder();
  private StringBuilder queryParams;
  private String url;

  RequestOptions requestOptions = null;
  AuthenticationProvider authenticationProvider;

  protected RestClientRequest() {
    authenticationProvider = RestClientConfiguration.get().getAuthenticationProvider();
  }

  public RestClientRequest<T> setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
    this.authenticationProvider = authenticationProvider;
    return this;
  }

  protected HttpCallback<T> getCallback() {
    return callback;
  }

  public RestClientRequest<T> setCallback(HttpCallback<T> callback) {
    BoundCallback<T> boundCallback = new BoundCallback<>(callback);
    this.callback = boundCallback;
    return this;
  }

  public RestClientRequest<T> setActivityBoundCallback(Activity activity, HttpCallback<T> callback) {
    this.callback = new ActivityBoundHttpCallback<>(activity, callback);
    return this;
  }

  public RestClientRequest<T> setFragmentBoundCallback(Fragment fragment, HttpCallback<T> callback) {
    this.callback = new FragmentBoundHttpCallback<>(fragment, callback);
    return this;
  }


  public RestClientRequest<T> setParser(HttpResponseParser<T> parser) {
    this.parser = parser;
    return this;
  }

  public ResponseHandler<T> getHandler() {
    return handler;
  }

  /**
   * Adds a new header value, existing value with same key will not be overwritten
   *
   * @param key
   * @param value
   */
  public RestClientRequest<T> addHeader(final String key, final String value) {
    request.addHeader(key, value);
    return this;
  }

  /**
   * Overrides an existing header value
   *
   * @param key
   * @param value
   */
  public RestClientRequest<T> header(final String key, final String value) {
    request.header(key, value);
    return this;
  }

  @Override
  public void run() {
    doBeforeRunRequestInBackgroundThread();
    validateRequestArguments();
    runRequest();
  }

  public void validateRequestArguments() {
    if(TextUtils.isEmpty(this.url)) {
      Log.e(TAG, "Request url is empty or null", new IllegalArgumentException());
    }
  }

  @Override
  public void cancel() {
    if (callback != null) {
      callback.unregister();
    }
  }

  public boolean isCanceled() {
    return callback != null && callback.isRegistered();
  }

  /**
   * Set a custom handler that will be triggered when the response returns
   * either Success or Fail. You can choose where this info is sent. **Default**
   * is the UIThreadREsponseHandler implementation which runs the appropriate
   * callback on the UI thread.
   *
   * @param handler
   */
  public RestClientRequest<T> setResponseHandler(ResponseHandler<T> handler) {
    this.handler = handler;
    return this;
  }

  /**
   * By default success is a code in the range of 2xx. Everything else triggers
   * an Error. You can set a handler which will take into account your own
   * custom error codes to determine if the response is a success or fail.
   *
   * @param statusHandler
   */
  public RestClientRequest<T> setStatusHandler(ResponseStatusHandler statusHandler) {
    this.statusHandler = statusHandler;
    return this;
  }

  protected HttpResponseParser<T> getParser() {
    return parser;
  }

  public RestClientRequest<T> setUrl(String url) {
    this.url = url;
    return this;
  }

  public RestClientRequest<T> setRequestMethod(RequestMethod requestMethod) {
    return setRequestMethod(requestMethod, null);

  }

  public RestClientRequest<T> setRequestMethod(RequestMethod requestMethod, RequestBody requestBody) {
    request.method(requestMethod.name(), requestBody);
    return this;
  }

  protected void runRequest() {
    if (handler == null) {
      handler = new UIThreadResponseHandler<>(callback);
    }
    if (statusHandler == null) {
      statusHandler = new DefaultResponseStatusHandler();
    }

    OkHttpClient client = generateClient();
    StringBuilder url = new StringBuilder(this.url);
    StringBuilder queryParams = this.queryParams;
    if (queryParams != null) {
      url.append(queryParams);
    }

    request.url(url.toString());

    if (this.authenticationProvider != null) {
      authenticationProvider.authenticateRequest(this);
    }

    Request preparedRequest = request.build();
    Response response;
    try {
      response = client.newCall(preparedRequest).execute();
    } catch (final Exception e) {
      ResponseStatus responseStatus = ResponseStatus.getConnectionErrorStatus();
      Log.e(TAG, responseStatus.toString(), e);
      handler.handleError(responseStatus);
      return;
    }

    final ResponseStatus status = new ResponseStatus(response.code(), response.message());
    Log.d(TAG, status.toString());
    if (handleResponseStatus(status)) {
      return;
    }

    try {
      if (parser != null) {
        InputStream instream = response.body().byteStream();
        final T responseData = parser.parse(instream);
        close(response.body());
        doAfterSuccessfulRequestInBackgroundThread(responseData);
        handler.handleSuccess(responseData, status);
      } else {
        Log.i(TAG, "You haven't added a parser for your request");
        handler.handleSuccess(null, status);
        doAfterSuccessfulRequestInBackgroundThread(null);
      }
    } catch (final Exception e) {
      ResponseStatus responseStatus = ResponseStatus.getParseErrorStatus();
      Log.d(TAG, responseStatus.toString(), e);
      handler.handleError(responseStatus);
    }

  }

  /**
   * Return true if you have handled the status yourself.
   */
  protected boolean handleResponseStatus(ResponseStatus status) {
    if (statusHandler.hasErrorInStatus(status)) {
      handler.handleError(status);
      return true;
    }
    return false;
  }

  @Override
  public void executeAsync() {
    TaskStore.get(RestClientConfiguration.get().getContext()).queue(this, getRequestOptions());
  }

  public RequestOptions getRequestOptions() {
    return requestOptions;
  }

  public RestClientRequest<T> setRequestOptions(RequestOptions requestOptions) {
    this.requestOptions = requestOptions;
    return this;
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
  protected void doAfterSuccessfulRequestInBackgroundThread(T data) {
  }

  private OkHttpClient generateClient() {
    OkHttpClient client = new OkHttpClient();
    customizeClient(client);
    return client;
  }

  protected void customizeClient(OkHttpClient client) {
    client.setConnectTimeout(RestClientConfiguration.get().getConnectionTimeout(), TimeUnit.MILLISECONDS);
    client.setReadTimeout(RestClientConfiguration.get().getReadTimeout(), TimeUnit.MILLISECONDS);
    client.setWriteTimeout(RestClientConfiguration.get().getWriteTimeout(), TimeUnit.MILLISECONDS);
  }

  public RestClientRequest<T> addQueryParam(String name, String value) {
    addQueryParam(name, value, false, true);
    return this;
  }

  public RestClientRequest<T> addEncodedQueryParam(String name, String value) {
    addQueryParam(name, value, false, false);
    return this;
  }

  private void addQueryParam(String name, Object value, boolean encodeName, boolean encodeValue) {
    if (value instanceof Iterable) {
      for (Object iterableValue : (Iterable<?>) value) {
        if (iterableValue != null) { // Skip null values
          addQueryParam(name, iterableValue.toString(), encodeName, encodeValue);
        }
      }
    } else if (value.getClass().isArray()) {
      for (int x = 0, arrayLength = Array.getLength(value); x < arrayLength; x++) {
        Object arrayValue = Array.get(value, x);
        if (arrayValue != null) { // Skip null values
          addQueryParam(name, arrayValue.toString(), encodeName, encodeValue);
        }
      }
    } else {
      addQueryParam(name, value.toString(), encodeName, encodeValue);
    }
  }

  private void addQueryParam(String name, String value, boolean encodeName, boolean encodeValue) {
    if (name == null) {
      throw new IllegalArgumentException("Query param name must not be null.");
    }
    if (value == null) {
      throw new IllegalArgumentException("Query param \"" + name + "\" value must not be null.");
    }
    try {
      StringBuilder queryParams = this.queryParams;
      if (queryParams == null) {
        this.queryParams = queryParams = new StringBuilder();
      }

      queryParams.append(queryParams.length() > 0 ? '&' : '?');

      if (encodeName) {
        name = URLEncoder.encode(name, "UTF-8");
      }
      if (encodeValue) {
        value = URLEncoder.encode(value, "UTF-8");
      }
      queryParams.append(name).append('=').append(value);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(
        "Unable to convert query parameter \"" + name + "\" value to UTF-8: " + value, e);
    }
  }

  private void close(ResponseBody body) {
    try {
      if (body != null) {
        body.close();
      }
    } catch (IOException e) {
    }

  }
}
