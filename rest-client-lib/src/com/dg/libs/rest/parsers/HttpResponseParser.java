package com.dg.libs.rest.parsers;

import android.net.ParseException;

/**
 * The {@link HttpResponseParser} interface has the responsibility to parse JSON
 * responses from the server.
 * 
 * @param <T>
 *            Parameter that indicates which object the parser returns. It can
 *            be of any type.
 */
public interface HttpResponseParser<T> {

    /**
     * This method is used for parsing JSON response from server. Given a JSON
     * string, returns response data which can be of any type.
     * 
     * @param responseBody
     *            The JSON string needed for parsing.
     * @return Object of any type returned by the parser.
     * @throws ParserException
     *             Thrown when various JSON errors occur, such as missing
     *             attribute.
     */
    public T parse(final String responseBody) throws ParseException;
}
