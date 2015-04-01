package com.dg.examples.restclientdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.examples.restclientdemo.communication.requests.BlogsGoogleRequest;
import com.dg.examples.restclientdemo.communication.requests.PatchRequest;
import com.dg.examples.restclientdemo.domain.ResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;
import com.dg.libs.rest.requests.RestClientRequest;


public class MainActivity extends Activity {


  private TextView textViewResponse;
  private RestClientRequest<ResponseModel> request;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    textViewResponse = (TextView) findViewById(R.id.textViewResponse);

    request = new BlogsGoogleRequest("Official Google Blogs").setCallback(new GoogleBlogsCallback());
    request
      .executeAsync();

    new PatchRequest("Hello").setCallback(new HttpCallback<Void>() {
      @Override
      public void onSuccess(Void responseData, ResponseStatus responseStatus) {
        Toast.makeText(getApplicationContext(), "Success patch", Toast.LENGTH_LONG).show();
      }

      @Override
      public void onHttpError(ResponseStatus responseStatus) {
        Toast.makeText(getApplicationContext(), "FAIL patch", Toast.LENGTH_LONG).show();
      }
    }).executeAsync();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    request.cancel();
  }

  private final class GoogleBlogsCallback implements HttpCallback<ResponseModel> {


    @Override
    public void onSuccess(ResponseModel responseData, ResponseStatus responseStatus) {
      textViewResponse.setText(responseData.toString());
    }

    @Override
    public void onHttpError(ResponseStatus responseStatus) {

    }

  }
}
