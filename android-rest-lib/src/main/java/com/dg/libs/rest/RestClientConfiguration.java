package com.dg.libs.rest;

import android.content.Context;
import com.araneaapps.android.libs.asyncrunners.models.AsyncRunners;
import com.dg.libs.rest.authentication.AuthenticationProvider;

public class RestClientConfiguration {

  private static Context applicationContext;
  private AuthenticationProvider authenticationProvider;
  int connectionTimeout;
  int socketTimeout;

  private RestClientConfiguration(AuthenticationProvider authenticationProvider, int connectionTimeout, int socketTimeout) {
    this.authenticationProvider = authenticationProvider;
    this.connectionTimeout = connectionTimeout;
    this.socketTimeout = socketTimeout;
  }


  private static RestClientConfiguration generateDefaultConfig() {
    return new ConfigurationBuilder().create();
  }

  private static RestClientConfiguration instance;

  public static void init(Context context, RestClientConfiguration restClientConfiguration) {
    applicationContext = context.getApplicationContext();
    AsyncRunners.init(applicationContext);
    instance = restClientConfiguration;
  }

  public static RestClientConfiguration get() {
    if (instance == null) {
      instance = generateDefaultConfig();
    }
    return instance;
  }


  public AuthenticationProvider getAuthenticationProvider() {
    return authenticationProvider;
  }

  public int getConnectionTimeout() {
    return connectionTimeout;
  }

  public int getSocketTimeout() {
    return socketTimeout;
  }

  public Context getContext() {
    return applicationContext;
  }

  public static class ConfigurationBuilder {
    private AuthenticationProvider authenticationProvider;
    private int connectionTimeout = 8000;
    private int socketTimeout = 20000;

    public ConfigurationBuilder setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
      this.authenticationProvider = authenticationProvider;
      return this;
    }

    public ConfigurationBuilder setConnectionTimeout(int connectionTimeout) {
      this.connectionTimeout = connectionTimeout;
      return this;
    }

    public ConfigurationBuilder setSocketTimeout(int socketTimeout) {
      this.socketTimeout = socketTimeout;
      return this;
    }

    public RestClientConfiguration create() {
      return new RestClientConfiguration(authenticationProvider, connectionTimeout, socketTimeout);
    }
  }
}
