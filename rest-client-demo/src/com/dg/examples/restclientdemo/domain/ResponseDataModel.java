package com.dg.examples.restclientdemo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

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
