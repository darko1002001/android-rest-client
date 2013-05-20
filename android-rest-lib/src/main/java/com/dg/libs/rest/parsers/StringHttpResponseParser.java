package com.dg.libs.rest.parsers;

import java.io.InputStream;

import com.dg.libs.rest.client.StreamUtil;

/**
 * The {@link HttpResponseParser} interface has the responsibility to parse
 * responses from the server.
 * 
 * @param <T>
 *           Parameter that indicates which object the parser returns. It can be
 *           of any type.
 */
public abstract class StringHttpResponseParser<T> implements HttpResponseParser<T> {

	/**
	 * Do not override this method. Use the **abstract** {@link #parse(String)}
	 * 
	 * This method is used for parsing text from the server. can be used for
	 * anything that can be converted out of a string
	 * 
	 * @param responseStream
	 *           The JSON string needed for parsing.
	 * @return Object of any type returned by the parser.
	 * @throws Exception
	 *            Thrown when various parsing errors occur.
	 */
	@Override
	public T parse(final InputStream responseStream) throws Exception {
		return parse(StreamUtil.convertStreamToString(responseStream));
	}

	public abstract T parse(final String response) throws Exception;
}
