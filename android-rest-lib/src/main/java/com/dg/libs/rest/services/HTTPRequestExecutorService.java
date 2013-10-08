package com.dg.libs.rest.services;

import android.content.Intent;

import com.dg.libs.rest.HttpRequestStore;
import com.dg.libs.rest.domain.RequestOptions;
import com.dg.libs.rest.domain.RequestWrapper;

public class HTTPRequestExecutorService extends BaseObservableThreadPoolServiceService {

  @Override
  public void handleIntent(final Intent intent) {
    RequestWrapper wrapper = HttpRequestStore.getInstance(getApplicationContext())
        .getRequest(intent);
    RequestOptions options = wrapper.getOptions();
    if (options.shouldRunInSingleThread() == false) {
      getFixedSizePoolExecutor().execute(
          new WorkerThread(options.getPriority(), wrapper.getRequest()));
      return;
    }

    // Handle according to options

    getSingleThreadExecutorService().execute(
        new WorkerThread(options.getPriority(), wrapper.getRequest()));

  }

}
