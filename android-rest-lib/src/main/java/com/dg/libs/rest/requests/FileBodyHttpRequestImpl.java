package com.dg.libs.rest.requests;

import com.araneaapps.android.libs.logger.ALog;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.entities.CountingInputStreamEntity;
import com.dg.libs.rest.entities.CountingInputStreamEntity.UploadListener;
import com.dg.libs.rest.parsers.HttpResponseParser;
import org.apache.http.HttpEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class FileBodyHttpRequestImpl<T> extends EntityHttpRequestImpl<T> {

  public static final String TAG = FileBodyHttpRequestImpl.class.getSimpleName();
  private UploadListener listener;

  public FileBodyHttpRequestImpl(RequestMethod requestMethod,
      final HttpResponseParser<T> parser, final HttpCallback<T> callback) {
    super(requestMethod, parser, callback);
  }

  public abstract File fileToSend();

  public void setListener(UploadListener listener) {
    this.listener = listener;
  }

  @Override
  public HttpEntity getEntity() {
    try {
      File file = fileToSend();
      FileInputStream fileInputStream = new FileInputStream(file);
      CountingInputStreamEntity countingInputStreamEntity = new CountingInputStreamEntity(
          fileInputStream, file.length());
      countingInputStreamEntity.setUploadListener(listener);
      return countingInputStreamEntity;
    } catch (IOException e) {
      ALog.e(e);
    }
    return null;
  }
}
