package com.dg.examples.restclientdemo.communication.requests;

import android.content.Context;
import com.dg.examples.restclientdemo.communication.RestConstants;
import com.dg.examples.restclientdemo.communication.parsers.BlogsGoogleParser;
import com.dg.examples.restclientdemo.domain.ResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.requests.ParameterHttpRequestImpl;

public class BlogsGoogleRequest extends ParameterHttpRequestImpl<ResponseModel> {

  public static final String TAG = BlogsGoogleRequest.class.getSimpleName();

  public BlogsGoogleRequest(Context context, String query, HttpCallback<ResponseModel> callback) {
    super(context, RequestMethod.GET, new BlogsGoogleParser(), callback);
    addParam("q", query);
    addParam("v", "1.0");
    addParam("include_entities", "" + true);
  }

  @Override
  protected String getUrl() {
    return RestConstants.GOOGLE_BLOGS;
  }


}
