package com.dg.libs.rest.client;

import android.text.TextUtils;

import com.araneaapps.android.libs.logger.ALog;
import com.dg.libs.rest.authentication.AuthenticationProvider;
import com.dg.libs.rest.domain.ResponseStatus;
import com.dg.libs.rest.entities.UnicodeBOMInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public abstract class BaseRestClient implements Rest {

	public enum RequestMethod {
		GET, POST, PUT, DELETE
	}

	private static final String TAG = ParametersRestClient.class.getSimpleName();

	private static HttpClient client;
	private RequestMethod requestMethod = RequestMethod.GET;

	private final ArrayList<NameValuePair> headers;
	private final ArrayList<NameValuePair> params;

	private String url;

	ResponseStatus responseStatus = new ResponseStatus();
	private InputStream responseStream;

	private AuthenticationProvider authProvider;
	private static AuthenticationProvider authenticationProvider;

	public BaseRestClient() {
		headers = new ArrayList<NameValuePair>();
		params = new ArrayList<NameValuePair>();
	}

	@Override
	public void setUrl(final String url) {
		this.url = url;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public InputStream getResponse() {
		return responseStream;
	}

	@Override
	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}

	@Override
	public ArrayList<NameValuePair> getHeaders() {
		return headers;
	}

	@Override
	public ArrayList<NameValuePair> getParams() {
		return params;
	}

	@Override
	public void addHeader(final String name, final String value) {
		if (TextUtils.isEmpty(value) == false) {
			headers.add(new BasicNameValuePair(name, value));
		}
	}

	@Override
	public void addParam(final String name, final String value) {
		if (TextUtils.isEmpty(value) == false) {
			params.add(new BasicNameValuePair(name, value));
		}
	}

	@Override
	public void setAuthentication(AuthenticationProvider authProvider) {
		this.authProvider = authProvider;
	}

	@Override
	public void setRequestMethod(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
	}

	@Override
	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	public static void setDefaultAuthenticationProvider(AuthenticationProvider provider) {
		BaseRestClient.authenticationProvider = provider;
	}

	private void authenticateRequest() {
		if (authProvider != null) {
			authProvider.authenticateRequest(this);
			return;
		}
		if (authenticationProvider != null) {
			authenticationProvider.authenticateRequest(this);
			return;
		}
	}

	@Override
	public void executeRequest(final HttpUriRequest request) throws IOException {
		authenticateRequest();

		// add headers
		for (NameValuePair h : getHeaders()) {
			request.addHeader(h.getName(), h.getValue());
		}
		HttpResponse httpResponse;
		try {

			httpResponse = getClient().execute(request);

			responseStatus.setStatusCode(httpResponse.getStatusLine().getStatusCode());
			responseStatus.setStatusMessage(httpResponse.getStatusLine().getReasonPhrase());

			final HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {
				UnicodeBOMInputStream unicodeBOMInputStream = new UnicodeBOMInputStream(entity.getContent());
				unicodeBOMInputStream.skipBOM();
				responseStream = unicodeBOMInputStream;
			}
		} catch (final IOException e) {
			closeStream();
			throw e;
		}
	}

	@Override
	public void closeStream() {
		IOUtils.closeQuietly(responseStream);
	}

	public static HttpClient getClient() {
		if (client == null) {
			client = new ExtendedOkApacheClient();
		}
		return client;
	}

	public static String generateParametersString(final ArrayList<NameValuePair> params)
			throws UnsupportedEncodingException {
		// add parameters
		String combinedParams = "";
		if (params != null && !params.isEmpty()) {
			combinedParams += "?";
			for (final NameValuePair p : params) {
				final String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
				if (combinedParams.length() > 1) {
					combinedParams += "&" + paramString;
				} else {
					combinedParams += paramString;
				}
			}
		}
		return combinedParams;
	}

}
