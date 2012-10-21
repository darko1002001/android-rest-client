package com.dg.libs.rest.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.text.TextUtils;
import android.util.Log;

import com.dg.libs.rest.authentication.AuthenticationProvider;
import com.dg.libs.rest.authentication.TokenAuthenticationProvider;
import com.dg.libs.rest.domain.ResponseStatus;

public abstract class BaseRestClient implements Rest {

    public enum RequestMethod {
	GET, POST, PUT, DELETE
    }

    private static final String TAG = ParametersRestClient.class.getSimpleName();

    private static DefaultHttpClient client;

    private final ArrayList<NameValuePair> headers;
    private final ArrayList<NameValuePair> params;

    private String url;

    ResponseStatus responseStatus = new ResponseStatus();
    private String response;

    int connectionTimeout = 8000;
    int socketTimeout = 12000;

    private AuthenticationProvider authProvider;
    private static AuthenticationProvider authenticationProvider;

    public BaseRestClient() {
	headers = new ArrayList<NameValuePair>();
	params = new ArrayList<NameValuePair>();
    }

    @Override
    public int getConnectionTimeout() {
	return connectionTimeout;
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
    public void setConnectionTimeout(final int connectionTimeout) {
	this.connectionTimeout = connectionTimeout;
    }

    @Override
    public int getSocketTimeout() {
	return socketTimeout;
    }

    @Override
    public void setSocketTimeout(final int socketTimeout) {
	this.socketTimeout = socketTimeout;
    }

    @Override
    public String getResponse() {
	return response;
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
	TokenAuthenticationProvider.getInstance().authenticateRequest(this);
    }

    @Override
    public void executeRequest(final HttpUriRequest request) throws IOException {
	authenticateRequest();

	// add headers
	for (NameValuePair h : getHeaders()) {
	    request.addHeader(h.getName(), h.getValue());
	}

	final HttpParams httpParameters = new BasicHttpParams();
	// Set the timeout in milliseconds until a connection is established.
	HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeout);
	// Set the default socket timeout (SO_TIMEOUT)
	// in milliseconds which is the timeout for waiting for data.
	HttpConnectionParams.setSoTimeout(httpParameters, socketTimeout);

	getClient().setParams(httpParameters);
	HttpResponse httpResponse;
	InputStream instream = null;
	try {

	    httpResponse = getClient().execute(request);

	    responseStatus.setStatusCode(httpResponse.getStatusLine().getStatusCode());
	    responseStatus.setStatusMessage(httpResponse.getStatusLine().getReasonPhrase());

	    final HttpEntity entity = httpResponse.getEntity();

	    if (entity != null) {

		instream = entity.getContent();
		response = StreamUtil.convertStreamToString(instream);
		Log.d(TAG, "URL: " + url + " RESPONSE: " + response);
		// Closing the input stream will trigger connection release
		instream.close();
	    }

	} catch (final IOException e) {
	    throw e;
	} catch (final RuntimeException ex) {
	    // In case of an unexpected exception you may want to abort
	    // the HTTP request in order to shut down the underlying
	    // connection immediately.
	    request.abort();
	    throw ex;
	} finally {
	    // Closing the input stream will trigger connection release
	    StreamUtil.silentClose(instream);
	}
    }

    public static DefaultHttpClient getClient() {
	if (client == null) {
	    client = StreamUtil.generateClient();
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
		final String paramString = p.getName() + "="
			+ URLEncoder.encode(p.getValue(), "UTF-8");
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
