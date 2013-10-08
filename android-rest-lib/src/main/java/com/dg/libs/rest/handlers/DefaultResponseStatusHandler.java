package com.dg.libs.rest.handlers;

import com.dg.libs.rest.domain.ResponseStatus;

public class DefaultResponseStatusHandler implements ResponseStatusHandler {

  public DefaultResponseStatusHandler() {
    super();
  }

  @Override
  public boolean hasErrorInStatus(ResponseStatus status) {
    return (status.getStatusCode() < 200 || status.getStatusCode() >= 300);
  }

}
