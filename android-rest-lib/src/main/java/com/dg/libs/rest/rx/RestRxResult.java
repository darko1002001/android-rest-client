package com.dg.libs.rest.rx;

import com.dg.libs.rest.domain.ResponseStatus;

public class RestRxResult<T> {

  T data;
  ResponseStatus status;

  public RestRxResult(T data, ResponseStatus status) {
    this.data = data;
    this.status = status;
  }

  public T getData() {
    return data;
  }

  public ResponseStatus getStatus() {
    return status;
  }
}
