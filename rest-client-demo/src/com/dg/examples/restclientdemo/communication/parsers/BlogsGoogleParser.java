package com.dg.examples.restclientdemo.communication.parsers;

import com.dg.examples.restclientdemo.domain.ResponseModel;

import java.io.InputStream;


public class BlogsGoogleParser extends BaseJacksonMapperResponseParser<ResponseModel> {

  public static final String TAG = BlogsGoogleParser.class.getSimpleName();

  @Override
  public ResponseModel parse(InputStream instream) throws Exception {
    return mapper.readValue(instream, ResponseModel.class);
  }
}
