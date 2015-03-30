package com.dg.examples.restclientdemo.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.dg.libs.rest.authentication.AuthenticationProvider;
import com.dg.libs.rest.requests.RestClientRequest;

public class TokenAuthenticationProvider implements AuthenticationProvider {

  private static final String TOKEN_KEY = "api_key";

  private static TokenAuthenticationProvider account;
  private final Context context;
  private String token;

  /**
   * <b> this object will be using a Reference to the application context via
   * getApplicationContext() NOT the Activity context</b> Recomended to be
   * initialized at the application startup or by initializing in your own class
   * extending application
   * <p>
   * <b> Dont forget to set the password on first init </b>
   *
   * @return
   */
  public static synchronized TokenAuthenticationProvider getInstance() {
    if (account == null) {
      throw new RuntimeException("Initialize the Provider first");
    }
    return account;
  }

  public static synchronized void init(Context context) {
    if (account == null) {
      account = new TokenAuthenticationProvider(context);
    }
  }

  private TokenAuthenticationProvider(final Context context) {
    this.context = context.getApplicationContext();
    initializeToken();
  }

  @Override
  public void authenticateRequest(RestClientRequest client) {
    if (TextUtils.isEmpty(token)) {
      return;
    }
    client.addHeader("Authorization", "OAuth " + token);
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
    saveApiKey(token);
  }

  public boolean isTokenValid() {
    return !TextUtils.isEmpty(token);
  }

  public boolean clearAuth() {
    Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
    editor.remove(TOKEN_KEY);
    boolean commit = editor.commit();
    return commit;
  }

  /**
   * Use as an alternative for saving the token to accounts (Note that using the
   * account manager is a preferred and safer method)
   * 
   * @param apiKey
   *          the token aqured from chute auth
   * @return if the save was successful
   */
  private boolean saveApiKey(final String apiKey) {
    Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
    editor.putString(TOKEN_KEY, apiKey);
    boolean commit = editor.commit();
    return commit;
  }

  private String restoreApiKey() {
    SharedPreferences savedSession = PreferenceManager
        .getDefaultSharedPreferences(context);
    return savedSession.getString(TOKEN_KEY, "");
  }

  private void initializeToken() {
    String apiKey = restoreApiKey();
    if (!TextUtils.isEmpty(apiKey)) {
      this.setToken(apiKey);
      return;
    }
    // Set a manual token for testing
    // this.setPassword("46e580a90085912ed11c565084f1f2465f28630bd58fa80cc98432f3078fc5ac");
  }
}
