package com.dg.libs.rest.handlers;

import android.os.Handler;
import android.os.Looper;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class UIThreadResponseHandler<T> extends BackgroundThreadResponseHandler<T>
    implements ResponseHandler<T> {

  public static final String TAG = UIThreadResponseHandler.class.getSimpleName();
  protected static Handler handler;

  public UIThreadResponseHandler(HttpCallback<T> callback) {
    super(callback);
    if (handler == null) {
      handler = new Handler(Looper.getMainLooper());
    }
  }

  @Override
  public void handleSuccess(final T responseData, final ResponseStatus status) {
    handler.post(new Runnable() {

      @Override
      public void run() {
        if (callback != null) {
          callback.onSuccess(responseData, status);
        }
      }
    });
  }

  @Override
  public void handleError(final ResponseStatus status) {
    handler.post(new Runnable() {

      @Override
      public void run() {
        if (callback != null) {
          callback.onHttpError(status);
        }
      }
    });
  }
}
