package com.dg.libs.rest.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import com.dg.libs.android.logger.ALog;
import com.dg.libs.rest.exceptions.HttpException;

public class StringBodyRestClient extends BaseRestClient {

	public static final String TAG = StringBodyRestClient.class.getSimpleName();
	private String body;


	public StringBodyRestClient() {
		super();
	}

	public void setBody(final String body) {
		this.body = body;
	}

	@Override
	public void execute() throws HttpException {

		try {
			switch (getRequestMethod()) {
			case POST:
				HttpPost postRequest = new HttpPost(getUrl());
				postRequest.setEntity(new StringEntity(body, "UTF-8"));
				executeRequest(postRequest);
				break;
			case PUT:
				HttpPut putRequest = new HttpPut(getUrl());
				putRequest.setEntity(new StringEntity(body, "UTF-8"));
				executeRequest(putRequest);
				break;
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
