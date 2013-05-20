package com.dg.libs.rest.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;

import com.dg.libs.rest.authentication.AuthenticationProvider;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.domain.ResponseStatus;
import com.dg.libs.rest.exceptions.HttpException;

public interface Rest {

	public abstract void setUrl(final String url);

	public abstract String getUrl();
	
	public abstract void setAuthentication(final AuthenticationProvider authProvider);

	public abstract InputStream getResponse();

	public abstract ArrayList<NameValuePair> getHeaders();

	public abstract ArrayList<NameValuePair> getParams();

	public abstract void addHeader(final String name, final String value);

	public abstract void addParam(final String name, final String value);

	public void setRequestMethod(RequestMethod requestMethod);

	public RequestMethod getRequestMethod();

	public abstract void executeRequest(final HttpUriRequest request) throws IOException;

	public void execute() throws HttpException;

	public void closeStream();

	public abstract ResponseStatus getResponseStatus();

}
