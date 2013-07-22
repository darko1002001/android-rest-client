package com.dg.examples.restclientdemo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseModel {
	// To learn how to configure this go here:
	// http://wiki.fasterxml.com/JacksonInFiveMinutes
	// Url to pull down is set under communication/RestConstants.java
	public static final String TAG = ResponseModel.class.getSimpleName();

	@JsonProperty
	private String responseStatus;
	
	@JsonProperty
	private String responseDetails;
	
	@JsonProperty
	private ResponseDataModel responseData;

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getResponseDetails() {
		return responseDetails;
	}

	public void setResponseDetails(String responseDetails) {
		this.responseDetails = responseDetails;
	}

	public ResponseDataModel getResponseData() {
		return responseData;
	}

	public void setResponseData(ResponseDataModel responseData) {
		this.responseData = responseData;
	}

	@Override
	public String toString() {
		return "ResponseModel [responseStatus=" + responseStatus
				+ ", responseDetails=" + responseDetails + ", responseData="
				+ responseData + "]";
	}

	
	

}
