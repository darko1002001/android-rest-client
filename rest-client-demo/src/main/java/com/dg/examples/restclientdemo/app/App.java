package com.dg.examples.restclientdemo.app;

import android.app.Application;

import com.dg.libs.rest.RestClientConfiguration;
import com.dg.libs.rest.authentication.AuthenticationProvider;
import com.dg.libs.rest.requests.RestClientRequest;

public class App extends Application {

  public static final String TAG = App.class.getSimpleName();

  @Override
  public void onCreate() {
    super.onCreate();

    /*
    This is a simple token auth provider which stores a single token string in shared preferences. You can access it as a singleton and set/get token
    TokenAuthenticationProvider.init(this);
    RestClientConfiguration.init(this, new RestClientConfiguration.ConfigurationBuilder().
        setAuthenticationProvider(TokenAuthenticationProvider.getInstance())
        .create());
    */

    RestClientConfiguration builder = new RestClientConfiguration.ConfigurationBuilder()
        .setAuthenticationProvider(new AuthenticationProvider() {
          @Override
          public void authenticateRequest(RestClientRequest client) {
            // YOu can add parameters or headers which will be attached to each request
          }
        })
        .create();

    RestClientConfiguration.init(this, builder);

  }
}
