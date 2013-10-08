package com.dg.libs.rest.parsers;

import java.io.InputStream;

public class NoResponseParser implements HttpResponseParser<Void> {

  public static final String TAG = NoResponseParser.class.getSimpleName();

  @Override
  public Void parse(InputStream responseStream) throws Exception {
    return null;
  }
}
