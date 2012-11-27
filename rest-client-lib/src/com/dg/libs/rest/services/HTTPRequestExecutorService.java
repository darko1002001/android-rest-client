package com.dg.libs.rest.services;

import android.content.Intent;

import com.dg.libs.rest.HttpRequestStore;

public class HTTPRequestExecutorService extends BaseObservableThreadPoolServiceService {

    @Override
    public void handleIntent(final Intent intent) {
	getExecutor().execute(
		new WorkerThread(DownloadPriority.NORMAL, HttpRequestStore.getInstance(
			getApplicationContext()).getBlock(intent)));
    }

}
