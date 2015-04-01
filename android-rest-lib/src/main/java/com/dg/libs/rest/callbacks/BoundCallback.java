package com.dg.libs.rest.callbacks;

import android.util.Log;

import com.dg.libs.rest.domain.ResponseStatus;

public class BoundCallback<T> implements HttpCallback<T> {

  private static final String TAG = BoundCallback.class.getSimpleName();
  private HttpCallback<T> callback;
  private boolean isRegistered = true;

  public BoundCallback(HttpCallback<T> callback) {
    this.callback = callback;
  }

  public void unregister() {
    isRegistered = false;
  }

  public boolean isRegistered() {
    return isRegistered;
  }

  @Override
  public void onSuccess(T responseData, ResponseStatus responseStatus) {
    if (isRegistered) {
      callback.onSuccess(responseData, responseStatus);
    } else {
      Log.d(TAG, "callback is unregistered, wont be called");
    }
  }

  @Override
  public void onHttpError(ResponseStatus responseStatus) {
    if (isRegistered) {
      callback.onHttpError(responseStatus);
    } else {
      Log.d(TAG, "callback is unregistered, wont be called");
    }
  }

}
