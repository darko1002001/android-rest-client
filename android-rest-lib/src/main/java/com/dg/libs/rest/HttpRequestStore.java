package com.dg.libs.rest;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dg.libs.rest.domain.RequestOptions;
import com.dg.libs.rest.domain.RequestWrapper;
import com.dg.libs.rest.services.HTTPRequestExecutorService;

/**
 * @author Darko.Grozdanovski
 */
public class HttpRequestStore {

    public static final String TAG = HttpRequestStore.class.getSimpleName();

    public static final String KEY_ID = "id";

    private static final HashMap<Integer, RequestWrapper> map = new HashMap<Integer, RequestWrapper>();

    private final AtomicInteger counter = new AtomicInteger();
    private static Class<?> executorServiceClass = HTTPRequestExecutorService.class;

    private final Context context;
    private static HttpRequestStore instance;

    private HttpRequestStore(final Context context) {
        this.context = context;
    }

    public static HttpRequestStore getInstance(final Context context) {
        if (instance == null) {
            instance = new HttpRequestStore(context.getApplicationContext());
        }
        return instance;
    }

    public static void init(final Class<?> executorServiceClass) {
        HttpRequestStore.executorServiceClass = executorServiceClass;
    }

    public Integer addRequest(final RequestWrapper block) {
        return addRequest(counter.incrementAndGet(), block);
    }

    public Integer addRequest(final Integer id, final RequestWrapper block) {
        map.put(id, block);
        return id;
    }

    public void removeBlock(final Integer id) {
        map.remove(id);
    }

    public RequestWrapper getRequest(final Integer id) {
        return map.remove(id);
    }

    public RequestWrapper getRequest(final Intent intent) {
        final Bundle extras = intent.getExtras();
        if (extras == null || extras.containsKey(KEY_ID) == false) {
            throw new RuntimeException("Intent Must be Filled with ID of the block");
        }
        final int id = extras.getInt(KEY_ID);
        return getRequest(id);
    }

    public Integer launchServiceIntent(final HttpRequest block) {
        return launchServiceIntent(block, null);
    }

    public Integer launchServiceIntent(final HttpRequest block, RequestOptions options) {
        if (executorServiceClass == null) {
            throw new RuntimeException(
                    "Initialize the Executor service class in a class extending application");
        }
        final Intent service = new Intent(context, executorServiceClass);
        final RequestWrapper wrapper = new RequestWrapper(block, options);
        final Integer requestId = addRequest(wrapper);
        service.putExtra(KEY_ID, requestId);
        context.startService(service);
        return requestId;
    }

}
