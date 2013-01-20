package com.dg.examples.restclientdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.examples.restclientdemo.communication.TwitterService;
import com.dg.examples.restclientdemo.domain.UserModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class MainActivity extends Activity {


    private TextView textViewResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResponse = (TextView) findViewById(R.id.textViewResponse);

        TwitterService.getUsersTwitterRequest(getApplicationContext(), "android", new HttpCallbackImplementation())
        .executeAsync();
    }

    private final class HttpCallbackImplementation implements HttpCallback<UserModel> {

        @Override
        public void onSuccess(UserModel responseData) {
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
