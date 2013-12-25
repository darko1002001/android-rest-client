package com.dg.examples.restclientdemo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;


public class EntriesModel {

  @JsonProperty
  private String url;

  @JsonProperty
  private String title;

  @JsonProperty
  private String contentSnippet;

  @JsonProperty
  private String link;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContentSnippet() {
    return contentSnippet;
  }

  public void setContentSnippet(String contentSnippet) {
    this.contentSnippet = contentSnippet;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  @Override
  public String toString() {
    return "EntriesModel [url=" + url + ", title=" + title
        + ", contentSnippet=" + contentSnippet + ", link=" + link + "]";
  }


}
