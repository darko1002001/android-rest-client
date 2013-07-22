package com.dg.examples.restclientdemo.communication.parsers;

import java.io.InputStream;

import com.dg.examples.restclientdemo.domain.ResponseModel;
import com.dg.libs.rest.parsers.BaseJacksonMapperResponseParser;


public class BlogsGoogleParser extends BaseJacksonMapperResponseParser<ResponseModel> {

    public static final String TAG = BlogsGoogleParser.class.getSimpleName();

    @Override
    public ResponseModel parse(InputStream instream) throws Exception {
        return mapper.readValue(instream, ResponseModel.class);
    }
}
