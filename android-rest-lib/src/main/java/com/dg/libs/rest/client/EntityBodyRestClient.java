package com.dg.libs.rest.client;

import com.araneaapps.android.libs.logger.ALog;
import com.dg.libs.rest.entities.HttpPatch;
import com.dg.libs.rest.exceptions.HttpException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class EntityBodyRestClient extends BaseRestClient {

  public static final String TAG = EntityBodyRestClient.class.getSimpleName();
  private HttpEntity entity;

  public EntityBodyRestClient() {
    super();
  }

  public void setEntity(HttpEntity entity) {
    this.entity = entity;

  }

  @Override
  public void execute() throws HttpException {
    super.execute();
    try {
      switch (getRequestMethod()) {
      case POST:
        HttpPost postRequest = new HttpPost(getUrl()
            + generateParametersString(getParams()));
        postRequest.setEntity(entity);
        executeRequest(postRequest);
        break;
      case PUT:

        HttpPut putRequest = new HttpPut(getUrl() + generateParametersString(getParams()));
        putRequest.setEntity(entity);
        executeRequest(putRequest);
        break;
      case PATCH:
        HttpPatch patchRequest = new HttpPatch(getUrl() + generateParametersString(getParams()));
        patchRequest.setEntity(entity);
        executeRequest(patchRequest);
      default:
        throw new RuntimeException(
            "RequestMethod not supported, Only POST and PUT can contain body");
      }
    } catch (UnsupportedEncodingException e) {
      ALog.w(TAG, "", e);
      throw new HttpException(e);
    } catch (IOException e) {
      ALog.w(TAG, "", e);
      throw new HttpException(e);
    }
  }

}
