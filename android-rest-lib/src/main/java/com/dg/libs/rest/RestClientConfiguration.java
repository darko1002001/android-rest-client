package com.dg.libs.rest;

import android.content.Context;

import com.araneaapps.android.libs.asyncrunners.models.AsyncRunners;
import com.dg.libs.rest.authentication.AuthenticationProvider;

public class RestClientConfiguration {

  private static Context applicationContext;
  private AuthenticationProvider authenticationProvider;
  int connectionTimeout;
  int readTimeout;
  int writeTimeout;

  private RestClientConfiguration(AuthenticationProvider authenticationProvider, int connectionTimeout, int readTimeout, int writeTimeout) {
    this.authenticationProvider = authenticationProvider;
    this.connectionTimeout = connectionTimeout;
    this.readTimeout = readTimeout;
    this.writeTimeout = writeTimeout;
  }

  private static RestClientConfiguration generateDefaultConfig() {
    return new ConfigurationBuilder().create();
  }

  private static RestClientConfiguration instance;

  public static void init(Context context) {
    applicationContext = context.getApplicationContext();
    AsyncRunners.init(applicationContext);
    instance = generateDefaultConfig();
  }

  public static void init(Context context, RestClientConfiguration restClientConfiguration) {
    applicationContext = context.getApplicationContext();
    AsyncRunners.init(applicationContext);
    instance = restClientConfiguration;
  }

  public static RestClientConfiguration get() {
    if (instance == null) {
      throw new IllegalStateException("You need to call Init on " + RestClientConfiguration.class + " First. Do it in your class extending application");
    }
    return instance;
  }


  public AuthenticationProvider getAuthenticationProvider() {
    return authenticationProvider;
  }

  public int getConnectionTimeout() {
    return connectionTimeout;
  }

  public int getReadTimeout() {
    return readTimeout;
  }

  public int getWriteTimeout() {
    return writeTimeout;
  }

  public Context getContext() {
    return applicationContext;
  }

  public static class ConfigurationBuilder {
    private AuthenticationProvider authenticationProvider;
    private int connectionTimeout = 10000;
    private int readTimeout = 20000;
    private int writeTimeout = 20000;

    public ConfigurationBuilder setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
      this.authenticationProvider = authenticationProvider;
      return this;
    }

    public ConfigurationBuilder setConnectionTimeout(int connectionTimeout) {
      this.connectionTimeout = connectionTimeout;
      return this;
    }

    public ConfigurationBuilder setReadTimeout(int readTimeout) {
      this.readTimeout = readTimeout;
      return this;
    }

    public ConfigurationBuilder setWriteTimeout(int writeTimeout) {
      this.writeTimeout = writeTimeout;
      return this;
    }

    public RestClientConfiguration create() {
      return new RestClientConfiguration(authenticationProvider, connectionTimeout, readTimeout, writeTimeout);
    }
  }
}
