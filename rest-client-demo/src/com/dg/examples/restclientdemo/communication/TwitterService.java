package com.dg.examples.restclientdemo.communication;

import android.content.Context;

import com.dg.examples.restclientdemo.communication.requests.UsersTwitterRequest;
import com.dg.examples.restclientdemo.domain.UserModel;
import com.dg.libs.rest.HttpRequest;
import com.dg.libs.rest.callbacks.HttpCallback;


public class TwitterService {

    public static final String TAG = TwitterService.class.getSimpleName();

    public static HttpRequest
            getUsersTwitterRequest(Context context, String screenName, HttpCallback<UserModel> callback) {
        return new UsersTwitterRequest(context, screenName, callback);
    }
}
