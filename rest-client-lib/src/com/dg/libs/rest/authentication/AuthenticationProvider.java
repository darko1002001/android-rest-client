package com.dg.libs.rest.authentication;

import com.dg.libs.rest.client.BaseRestClient;

public interface AuthenticationProvider {

    public void authenticateRequest(BaseRestClient client);
}
