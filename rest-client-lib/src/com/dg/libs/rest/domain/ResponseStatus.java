package com.dg.libs.rest.domain;

import org.apache.http.HttpStatus;

public class ResponseStatus {
    public static final String TAG = ResponseStatus.class.getSimpleName();

    private int statusCode;
    private String statusMessage;

    public int getStatusCode() {
	return statusCode;
    }

    public void setStatusCode(int statusCode) {
	this.statusCode = statusCode;
    }

    public String getStatusMessage() {
	return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
	this.statusMessage = statusMessage;
    }

    public static ResponseStatus getConnectionErrorStatus() {
	ResponseStatus status = new ResponseStatus();
	status.setStatusCode(HttpStatus.SC_REQUEST_TIMEOUT);
	status.setStatusMessage("HTTP Connection Error");
	return status;
    }

    public static ResponseStatus getParseErrorStatus() {
	ResponseStatus status = new ResponseStatus();
	status.setStatusCode(HttpStatus.SC_BAD_REQUEST);
	status.setStatusMessage("Parser Error");
	return status;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("ResponseStatus [statusCode=");
	builder.append(statusCode);
	builder.append(", statusMessage=");
	builder.append(statusMessage);
	builder.append("]");
	return builder.toString();
    }

}
