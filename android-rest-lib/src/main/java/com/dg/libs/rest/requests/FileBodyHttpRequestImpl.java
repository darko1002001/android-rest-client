package com.dg.libs.rest.requests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.dg.libs.android.logger.ALog;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.entities.CountingInputStreamEntity;
import com.dg.libs.rest.entities.CountingInputStreamEntity.UploadListener;
import com.dg.libs.rest.parsers.HttpResponseParser;

public abstract class FileBodyHttpRequestImpl<T> extends EntityHttpRequestImpl<T> {

	public static final String TAG = FileBodyHttpRequestImpl.class.getSimpleName();
	private UploadListener listener;

	public FileBodyHttpRequestImpl(final Context context, RequestMethod requestMethod,
			final HttpResponseParser<T> parser, final HttpCallback<T> callback) {
		super(context, requestMethod, parser, callback);
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
			CountingInputStreamEntity countingInputStreamEntity = new CountingInputStreamEntity(fileInputStream, file.length());
			countingInputStreamEntity.setUploadListener(listener);
			return countingInputStreamEntity;
		} catch (IOException e) {
			ALog.e(e);
		}
		return null;
	}
}
