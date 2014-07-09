package com.dg.examples.restclientdemo.communication.requests;

import com.araneaapps.android.libs.logger.ALog;
import com.dg.examples.restclientdemo.communication.RestConstants;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.parsers.HttpResponseParser;
import com.dg.libs.rest.requests.ParameterHttpRequestImpl;

import java.io.InputStream;

public class PatchRequest extends ParameterHttpRequestImpl<Void> {

  public static final String TAG = PatchRequest.class.getSimpleName();

  public PatchRequest(String query, HttpCallback<Void> callback) {
    super(RequestMethod.PATCH, new HttpResponseParser<Void>() {
      @Override
      public Void parse(InputStream instream) throws Exception {
        ALog.d(org.apache.commons.io.IOUtils.toString(instream));
        return null;
      }
    }, callback);
    addParam("q", query);
    addParam("v", "1.0");
    addParam("include_entities", "" + true);
  }

  @Override
  protected String getUrl() {
    return RestConstants.HTTP_BIN_URL + "patch";
  }


}
