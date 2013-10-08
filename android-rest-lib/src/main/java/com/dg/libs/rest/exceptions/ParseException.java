package com.dg.libs.rest.exceptions;

public class ParseException extends HttpException {

  private static final long serialVersionUID = -7964127989566422126L;
  @SuppressWarnings("unused")
  private static final String TAG = ParseException.class.getSimpleName();

  public ParseException(final String s) {
    super(s);
  }

  public ParseException(final String detailMessage, final Throwable throwable) {
    super(detailMessage, throwable);
  }

  public ParseException(final Throwable throwable) {
    super(throwable);
  }

}
