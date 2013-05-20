package com.dg.examples.restclientdemo.communication.requests;

import android.content.Context;

import com.dg.examples.restclientdemo.communication.RestConstants;
import com.dg.examples.restclientdemo.communication.parsers.UsersTwitterParser;
import com.dg.examples.restclientdemo.domain.UserModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.requests.ParameterHttpRequestImpl;

public class UsersTwitterRequest extends ParameterHttpRequestImpl<UserModel> {


    public UsersTwitterRequest(Context context, String screenName, HttpCallback<UserModel> callback) {
        super(context, RequestMethod.GET, new UsersTwitterParser(), callback);
        addParam("screen_name", screenName);
        addParam("include_entities", "" + true);
    }

    public static final String TAG = UsersTwitterRequest.class.getSimpleName();


	@Override
	protected String getUrl() {
		return RestConstants.TWITTER_USERS;
	}

    
}
