package com.dg.libs.rest.rx;

import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;
import com.dg.libs.rest.requests.RestClientRequest;

import rx.Observable;
import rx.Subscriber;

public class RxUtil {

  public static <T> Observable<RestRxResult<T>> createObservable(final RestClientRequest<T> restClientRequest) {
    return Observable.create(new Observable.OnSubscribe<RestRxResult<T>>() {

      @Override
      public void call(final Subscriber<? super RestRxResult<T>> subscriber) {
        restClientRequest.setCallback(new HttpCallback<T>() {
          @Override
          public void onSuccess(T responseData, ResponseStatus responseStatus) {
            if (!subscriber.isUnsubscribed()) {
              subscriber.onNext(new RestRxResult<T>(responseData, responseStatus));
              subscriber.onCompleted();
            }
          }

          @Override
          public void onHttpError(ResponseStatus responseStatus) {
            if (!subscriber.isUnsubscribed()) {
              subscriber.onError(responseStatus);
            }
          }
        }).executeAsync();
      }
    });
  }

  private static boolean hasRxJavaOnClasspath() {
    try {
      Class.forName("rx.Observable");
      return true;
    } catch (ClassNotFoundException ignored) {
    }
    return false;
  }

}
