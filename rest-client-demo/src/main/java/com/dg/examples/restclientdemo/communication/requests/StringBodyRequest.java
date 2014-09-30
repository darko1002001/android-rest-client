package com.dg.examples.restclientdemo.communication.requests;


import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.parsers.HttpResponseParser;
import com.dg.libs.rest.requests.StringBodyHttpRequestImpl;

public class StringBodyRequest extends StringBodyHttpRequestImpl<String> {

  public StringBodyRequest(HttpResponseParser<String> parser,
                           HttpCallback<String> callback) {
    super(RequestMethod.POST /* Or can be RequestMethod.PUT*/, parser, callback);
    addParam("key", "value");
    addHeader("key", "value");
  }

  @Override
  public String bodyContents() {
    // Return the string you want to send in the body
    return "This is the string i want to set as a body";
  }

  @Override
  protected String getUrl() {
    // TODO Auto-generated method stub
    return "The URL you want to open connection to";
  }

}
