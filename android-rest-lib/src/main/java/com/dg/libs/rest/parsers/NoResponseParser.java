package com.dg.libs.rest.parsers;

public class NoResponseParser implements HttpResponseParser<Void> {

    public static final String TAG = NoResponseParser.class.getSimpleName();

    @Override
    public Void parse(String responseBody) {
        return null;
    }
}
