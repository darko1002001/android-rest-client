package com.dg.examples.restclientdemo.app;

import android.app.Application;
import com.araneaapps.android.libs.logger.ALog;
import com.araneaapps.android.libs.logger.ALog.DebugLevel;
import com.dg.libs.rest.authentication.AuthenticationProvider;
import com.dg.libs.rest.client.BaseRestClient;

public class App extends Application {

  public static final String TAG = App.class.getSimpleName();

  @Override
  public void onCreate() {
    super.onCreate();

    ALog.setDebugLevel(DebugLevel.ALL);

    BaseRestClient.setDefaultAuthenticationProvider(new AuthenticationProvider() {
      @Override
      public void authenticateRequest(BaseRestClient client) {
        // YOu can add parameters or headers which will be attached to each request
      }
    });

    // This is a simple token auth provider which stores a single token string in shared preferences. You can access it as a singleton and set/get token
    //TokenAuthenticationProvider.init(this);
    //BaseRestClient.setDefaultAuthenticationProvider(TokenAuthenticationProvider.getInstance());
  }
}
