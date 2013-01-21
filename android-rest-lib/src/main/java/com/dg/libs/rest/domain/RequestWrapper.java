package com.dg.libs.rest.domain;

import com.dg.libs.rest.HttpRequest;


public class RequestWrapper {

    public static final String TAG = RequestWrapper.class.getSimpleName();

    private final HttpRequest request;
    private final RequestOptions options;

    public RequestWrapper(HttpRequest request, RequestOptions options) {
        super();
        this.request = request;
        if (options != null) {
            this.options = options;
        } else {
            this.options = new RequestOptions();
        }
    }

    public HttpRequest getRequest() {
        return request;
    }

    public RequestOptions getOptions() {
        return options;
    }

}
