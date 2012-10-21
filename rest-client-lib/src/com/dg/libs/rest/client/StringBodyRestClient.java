package com.dg.libs.rest.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.dg.android.logger.Logger;
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

    public void execute() throws HttpException {
	try {
	    HttpPost request = new HttpPost(getUrl() + generateParametersString(getParams()));
	    request.setEntity(new StringEntity(body, "UTF-8"));
	    executeRequest(request);
	} catch (UnsupportedEncodingException e) {
	    Logger.w(TAG, "", e);
	    throw new HttpException(e);
	} catch (IOException e) {
	    Logger.w(TAG, "", e);
	    throw new HttpException(e);
	}
    }

}
