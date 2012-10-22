package com.dg.libs.rest.parsers;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public abstract class BaseJacksonMapperResponseParser<T> implements HttpResponseParser<T> {

    public static final String TAG = BaseJacksonMapperResponseParser.class.getSimpleName();

    public static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
    }

}
