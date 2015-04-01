package com.dg.examples.restclientdemo.communication.requests;

import android.util.Log;

import com.dg.examples.restclientdemo.communication.RestConstants;
import com.dg.examples.restclientdemo.communication.parsers.BlogsGoogleParser;
import com.dg.examples.restclientdemo.domain.ResponseModel;
import com.dg.libs.rest.client.RequestMethod;
import com.dg.libs.rest.requests.RestClientRequest;
import com.squareup.okhttp.OkHttpClient;

public class BlogsGoogleRequest extends RestClientRequest<ResponseModel> {

  public static final String TAG = BlogsGoogleRequest.class.getSimpleName();

  public BlogsGoogleRequest(String query) {
    super();
    setRequestMethod(RequestMethod.GET);
    setUrl(RestConstants.GOOGLE_BLOGS);

    setParser(new BlogsGoogleParser());

    addQueryParam("q", query);
    addQueryParam("v", "1.0");
    addQueryParam("include_entities", "" + true);
  }

  @Override
  protected void doAfterSuccessfulRequestInBackgroundThread(ResponseModel data) {
    try {

      Log.d(TAG, "sleeping to be able to see Bound callbacks in action");
      Thread.sleep(3000);
    } catch (InterruptedException e) {
    }
    super.doAfterSuccessfulRequestInBackgroundThread(data);
  }

  @Override
  protected void customizeClient(OkHttpClient client) {
    super.customizeClient(client);
    client.setRetryOnConnectionFailure(false);
  }
}
