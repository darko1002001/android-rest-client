package com.dg.examples.restclientdemo.communication;


import android.content.Context;

import com.dg.examples.restclientdemo.communication.requests.BlogsGoogleRequest;
import com.dg.examples.restclientdemo.domain.ResponseModel;
import com.dg.libs.rest.HttpRequest;
import com.dg.libs.rest.callbacks.HttpCallback;

public class GoogleService {

	public static final String TAG = GoogleService.class.getSimpleName();

	public static HttpRequest getGoogleBlogsRequest(Context context,
			String query, HttpCallback<ResponseModel> callback) {
		return new BlogsGoogleRequest(context, query, callback);
	}
}
