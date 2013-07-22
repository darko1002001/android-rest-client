package com.dg.examples.restclientdemo.domain;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseDataModel {
	
	@JsonProperty
	private String query;
	
	@JsonProperty
	ArrayList<EntriesModel> entries;

	@Override
	public String toString() {
		return "ResponseDataModel [query=" + query + ", entries=" + entries
				+ "]";
	}

}
