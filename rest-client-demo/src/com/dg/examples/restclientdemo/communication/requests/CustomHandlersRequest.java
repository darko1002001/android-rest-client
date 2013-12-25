package com.dg.examples.restclientdemo.communication.requests;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.araneaapps.android.libs.asyncrunners.enums.DownloadPriority;
import com.araneaapps.android.libs.asyncrunners.models.RequestOptions;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient;
import com.dg.libs.rest.domain.ResponseStatus;
import com.dg.libs.rest.handlers.UIThreadResponseHandler;
import com.dg.libs.rest.parsers.HttpResponseParser;
import com.dg.libs.rest.requests.ParameterHttpRequestImpl;

import java.io.InputStream;

public class CustomHandlersRequest extends ParameterHttpRequestImpl<Void> {

  private final CustomHandlersRequest.CustomUIHandler handler;
  private VoidHttpCallback callback;

  public static CustomHandlersRequest createInstance(Context c) {
    CustomHandlersRequest customHandlersRequest = new CustomHandlersRequest(c,
        new VoidHttpResponseParser(),
        new VoidHttpCallback());
    return customHandlersRequest;
  }

  public CustomHandlersRequest(Context context, VoidHttpResponseParser parser,
                               VoidHttpCallback callback) {
    super(context, BaseRestClient.RequestMethod.GET, parser, callback);
    this.callback = callback;

    handler = new CustomUIHandler(callback);
    setResponseHandler(handler);
    // You can add custom request options for specific request. there is a queue running the requests so new requests coming in
    // will be sorted and executed according to priority if needed.
    // ex. you queue 50 downloads but you want the app to still run API get requests without waiting for everything else to finish first.
    RequestOptions requestOptions = new RequestOptions.RequestOptionsBuilder()
        .setPriority(DownloadPriority.HIGH)
        .setRunInSingleThread(true).build();
    setRequestOptions(requestOptions);
  }

  @Override
  protected boolean handleResponseStatus(final ResponseStatus status) {
    // This method will also run in the background thread. Returning true means you have handled the request and this will be
    // the last piece of code which will be executed.
    // False means the execution will continue with the parser and then success if the request is parsed successfully.
    if (status.getStatusCode() == 204) {

      callback.onCustomResult(status);// careful this runs in Background thread.

      // Or to run in UI thread:
      Handler handler = new Handler(Looper.getMainLooper());
      handler.post(new Runnable() {
        @Override
        public void run() {
          callback.onCustomResult(status); // This will run in UI thread.
        }
      });

      // Alternatively:
      CustomHandlersRequest.this.handler.handleCustom(status);
      // will also run in UI thread but needs extra logic to implement as opposed to the code above. Cleaner but a bit more complex.

      return true;
    } else {
      return super.handleResponseStatus(status);
    }
  }

  private class CustomUIHandler extends UIThreadResponseHandler<Void> {

    private VoidHttpCallback callback;

    public CustomUIHandler(VoidHttpCallback callback) {
      super(callback);
      this.callback = callback;
    }

    public void handleCustom(final ResponseStatus status) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          callback.onCustomResult(status); // This will run in UI thread.
        }
      });
    }
  }

  @Override
  protected void doAfterRunRequestInBackgroundThread() {
    // this will run in after the request completes in background.
    super.doAfterRunRequestInBackgroundThread();
  }

  @Override
  protected void doBeforeRunRequestInBackgroundThread() {
    // this will run before the request runs. here you can query databases, add parameters or headers in the request background thread.
    // So there is no need to do async tasks or similar stuff to fetch parameters before executing a request if needed.
    super.doBeforeRunRequestInBackgroundThread();
  }

  @Override
  protected String getUrl() {
    return "http://some-dummy-url.com";
  }

  private static class VoidHttpResponseParser implements HttpResponseParser<Void> {
    @Override
    public Void parse(InputStream instream) throws Exception {
      return null;
    }
  }

  private static class VoidHttpCallback implements HttpCallback<Void> {
    @Override
    public void onSuccess(Void responseData) {
    }

    @Override
    public void onHttpError(ResponseStatus responseCode) {

    }

    public void onCustomResult(ResponseStatus status) {
    }
  }
}
