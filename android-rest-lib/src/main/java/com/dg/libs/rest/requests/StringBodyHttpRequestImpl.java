package com.dg.libs.rest.requests;

import android.content.Context;

import com.araneaapps.android.libs.logger.ALog;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.parsers.HttpResponseParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

public abstract class StringBodyHttpRequestImpl<T> extends EntityHttpRequestImpl<T> {

	public static final String TAG = StringBodyHttpRequestImpl.class.getSimpleName();

	public StringBodyHttpRequestImpl(final Context context, RequestMethod requestMethod,
			final HttpResponseParser<T> parser, final HttpCallback<T> callback) {
		super(context, requestMethod, parser, callback);
	}

	public abstract String bodyContents();

	@Override
	public HttpEntity getEntity() {
		try {
			return new StringEntity(bodyContents(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			ALog.e(e);
		}
		return null;
	}
}
