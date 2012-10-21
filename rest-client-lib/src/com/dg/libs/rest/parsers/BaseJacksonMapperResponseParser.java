package com.dg.libs.rest.parsers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseJacksonMapperResponseParser<T> implements HttpResponseParser<T> {

    public static final String TAG = BaseJacksonMapperResponseParser.class.getSimpleName();

    public static ObjectMapper mapper = new ObjectMapper();

    static {
	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

}
