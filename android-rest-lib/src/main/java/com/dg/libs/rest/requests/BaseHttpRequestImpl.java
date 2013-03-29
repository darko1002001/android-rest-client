package com.dg.libs.rest.requests;

import android.content.Context;

import com.dg.libs.android.logger.ALog;
import com.dg.libs.rest.HttpRequest;
import com.dg.libs.rest.HttpRequestStore;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.Rest;
import com.dg.libs.rest.domain.ResponseStatus;
import com.dg.libs.rest.exceptions.HttpException;
import com.dg.libs.rest.handlers.ResponseHandler;
import com.dg.libs.rest.handlers.UIThreadResponseHandler;
import com.dg.libs.rest.parsers.HttpResponseParser;

public abstract class BaseHttpRequestImpl<T> implements HttpRequest {

	public static final String TAG = BaseHttpRequestImpl.class.getSimpleName();

	private final Context context;

	private final HttpResponseParser<T> parser;
	private ResponseHandler<T> handler;
	private final HttpCallback<T> callback;

	public BaseHttpRequestImpl(final Context context, final HttpResponseParser<T> parser, final HttpCallback<T> callback) {
		super();
		this.parser = parser;
		this.callback = callback;
		this.context = context.getApplicationContext();
	}

	public abstract Rest getClient();

	public Context getContext() {
		return context;
	}

	protected HttpCallback<T> getCallback() {
		return callback;
	}

	public void addParam(final String key, final String value) {
		getClient().addParam(key, value);
	}

	public void addHeader(final String key, final String value) {
		getClient().addHeader(key, value);
	}

	public void setHandler(ResponseHandler<T> handler) {
		this.handler = handler;
	}

	protected HttpResponseParser<T> getParser() {
		return parser;
	}

	protected void runRequest(final String url) {

		if (handler == null) {
			handler = new UIThreadResponseHandler<T>(callback);
		}

		final Rest client = getClient();
		try {
			client.setUrl(url);
			prepareRequest();
			client.execute();
		} catch (final Exception e) {
			ResponseStatus responseStatus = ResponseStatus.getConnectionErrorStatus();
			ALog.d(TAG, responseStatus.toString(), e);
			handler.handleError(responseStatus);
			return;
		}

		final ResponseStatus status = client.getResponseStatus();
		ALog.d(TAG, status.toString());
		if (status.getStatusCode() < 200 || status.getStatusCode() >= 300) {
			handler.handleError(status);
			return;
		}

		try {
			final T responseData = parser.parse(client.getResponse());
			handler.handleSuccess(responseData);
		} catch (final Exception e) {
			ResponseStatus responseStatus = ResponseStatus.getParseErrorStatus();
			ALog.d(TAG, responseStatus.toString(), e);
			handler.handleError(responseStatus);
		}

	}

	@Override
	public void executeAsync() {
		HttpRequestStore.getInstance(context).launchServiceIntent(this);
	}

	protected abstract void prepareRequest() throws HttpException;

	protected abstract void prepareParams();
}
