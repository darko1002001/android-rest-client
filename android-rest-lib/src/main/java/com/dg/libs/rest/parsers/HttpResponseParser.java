package com.dg.libs.rest.parsers;

import java.io.InputStream;

/**
 * The {@link HttpResponseParser} interface has the responsibility to parse
 * responses from the server.
 * 
 * @param <T>
 *          Parameter that indicates which object the parser returns. It can be
 *          of any type.
 */
public interface HttpResponseParser<T> {

  /**
   * This method is used for parsing JSON response from server. Given a JSON
   * string, returns response data which can be of any type.
   * 
   * @param responseStream
   *          The JSON string needed for parsing.
   * @return Object of any type returned by the parser.
   * @throws Exception
   *           Thrown when various parsing errors occur.
   */
  public T parse(final InputStream instream) throws Exception;
}
