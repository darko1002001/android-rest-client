package com.dg.examples.restclientdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.dg.examples.restclientdemo.communication.GoogleService;
import com.dg.examples.restclientdemo.communication.requests.PatchRequest;
import com.dg.examples.restclientdemo.domain.ResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;


public class MainActivity extends Activity {


  private TextView textViewResponse;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    textViewResponse = (TextView) findViewById(R.id.textViewResponse);

    GoogleService.getGoogleBlogsRequest(getApplicationContext(), "Official Google Blogs", new GoogleBlogsCallback())
        .executeAsync();

    new PatchRequest("Hello", new HttpCallback<Void>() {
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

  private final class GoogleBlogsCallback implements HttpCallback<ResponseModel> {

    @Override
    public void onSuccess(ResponseModel responseData, ResponseStatus status) {
      textViewResponse.setText(responseData.toString());
    }

    @Override
    public void onHttpError(ResponseStatus responseCode) {
      Toast.makeText(getApplicationContext(),
          responseCode.getStatusCode() + " " + responseCode.getStatusMessage(),
          Toast.LENGTH_LONG).show();

    }
  }
}
