package com.dg.libs.rest.authentication;

import com.dg.libs.rest.requests.RestClientRequest;

public interface AuthenticationProvider {

  public void authenticateRequest(RestClientRequest restClientRequest);
}
