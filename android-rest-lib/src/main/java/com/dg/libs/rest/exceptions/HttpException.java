package com.dg.libs.rest.exceptions;

/**
 * usually represents a general error for the whole api call, if you need to
 * know if the parsing failed check the status of the response and the error
 * message ( if status is SC_OK means the api call was successful and there was
 * a parsing error).
 * 
 * @author DArkO
 */
public class HttpException extends Exception {

  private static final long serialVersionUID = -120498658240098246L;
  @SuppressWarnings("unused")
  private static final String TAG = HttpException.class.getSimpleName();

  public HttpException() {
    super();
  }

  public HttpException(final String detailMessage, final Throwable throwable) {
    super(detailMessage, throwable);
  }

  public HttpException(final String detailMessage) {
    super(detailMessage);
  }

  public HttpException(final Throwable throwable) {
    super(throwable);
  }

}
