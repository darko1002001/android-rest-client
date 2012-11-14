package com.dg.libs.rest;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import com.dg.libs.rest.services.HTTPRequestExecutorService;

/**
 * @author Darko.Grozdanovski
 */
public class HttpRequestStore {

    public static final String ID = "id";
    public static final String IS_SUCCESSFUL = "isSuccessful";

    public static final String TAG = HttpRequestStore.class.getSimpleName();

    private final Context context;

    private static HttpRequestStore instance;

    private HttpRequestStore(final Context context) {
        this.context = context;
    }

    private final AtomicInteger counter = new AtomicInteger();
    private static Class<?> executorServiceClass = HTTPRequestExecutorService.class;

    public static HttpRequestStore getInstance(final Context context) {
        if (instance == null) {
            instance = new HttpRequestStore(context.getApplicationContext());
        }
        return instance;
    }

    public static void init(final Class<?> executorServiceClass) {
        HttpRequestStore.executorServiceClass = executorServiceClass;

    }

    private static final HashMap<Integer, HttpRequest> map = new HashMap<Integer, HttpRequest>();

    public Integer addBlock(final HttpRequest block) {
        return addBlock(counter.incrementAndGet(), block);
    }

    public Integer addBlock(final Integer id, final HttpRequest block) {
        map.put(id, block);
        return id;
    }

    public void removeBlock(final Integer id) {
        if (map.containsKey(id)) {
            map.remove(id);
        }
    }

    public HttpRequest getBlock(final Integer id) {
        if (map.containsKey(id)) {
            final HttpRequest httpRequestImpl = map.get(id);
            return httpRequestImpl;
        }
        return null;
    }

    public HttpRequest getBlock(final Intent intent) {
        final Bundle extras = intent.getExtras();
        if (extras == null || extras.containsKey(ID) == false) {
            throw new RuntimeException("Intent Must be Filled with ID of the block");
        }
        final int id = extras.getInt(ID);
        if (map.containsKey(id)) {
            final HttpRequest httpRequestImpl = map.get(id);
            return httpRequestImpl;
        }
        return null;
    }

    public Integer launchServiceIntent(final HttpRequest block) {
        if (executorServiceClass == null) {
            throw new RuntimeException("Initialize the Executor service class in a class extending application");
        }
        if (isServiceAvailable() == false) {
            throw new RuntimeException("Declare the " + executorServiceClass.getSimpleName() + " in your manifest");
        }
        final Intent service = new Intent(context, executorServiceClass);
        final Integer addBlock = addBlock(block);
        service.putExtra(ID, addBlock);
        context.startService(service);
        return addBlock;
    }

    public boolean isServiceAvailable() {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(context, executorServiceClass);
        final List<ResolveInfo> resolveInfo = packageManager.queryIntentServices(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo.size() > 0) {
            return true;
        }
        return false;
    }

}
