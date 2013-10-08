package com.dg.libs.rest.domain;

import android.os.Bundle;

public class RequestOptions {

  protected Bundle bundle = new Bundle();

  private boolean runInSingleThread = false;
  private DownloadPriority priority = DownloadPriority.NORMAL;

  public Bundle getBundle() {
    return bundle;
  }

  public boolean shouldRunInSingleThread() {
    return runInSingleThread;
  }

  public void setRunInSingleThread(boolean runInSingleThread) {
    this.runInSingleThread = runInSingleThread;
  }

  public DownloadPriority getPriority() {
    return priority;
  }

  public void setPriority(DownloadPriority priority) {
    this.priority = priority;
  }

}
