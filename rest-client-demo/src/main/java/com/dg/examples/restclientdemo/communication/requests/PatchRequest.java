package com.dg.examples.restclientdemo.communication.requests;

import com.araneaapps.android.libs.logger.ALog;
import com.dg.examples.restclientdemo.communication.RestConstants;
import com.dg.libs.rest.client.RequestMethod;
import com.dg.libs.rest.parsers.HttpResponseParser;
import com.dg.libs.rest.requests.RestClientRequest;

import java.io.InputStream;

public class PatchRequest extends RestClientRequest<Void> {

  public static final String TAG = PatchRequest.class.getSimpleName();

  public PatchRequest(String query) {
    super();

    setParser(new HttpResponseParser<Void>() {
      @Override
      public Void parse(InputStream instream) throws Exception {
        ALog.d(org.apache.commons.io.IOUtils.toString(instream));
        return null;
      }
    });
    setRequestMethod(RequestMethod.PATCH);
    addEncodedQueryParam("q", query);
    addQueryParam("v", "1.0");
    addQueryParam("include_entities", "" + true);
    setUrl(RestConstants.HTTP_BIN_URL + "patch");
  }

}
