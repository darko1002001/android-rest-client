/**
 *
 */
package com.dg.libs.rest.services;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.dg.android.logger.Logger;

/** @author darko.grozdanovski */
abstract class BaseThreadPoolService extends Service {

    /** the maximum number of objects that can be stored in the pool */
    private static final int MAX_POOL_SIZE = 300;
    /** the number of objects that will be executed simultaniously */
    private static final int CORE_POOL_SIZE = 3;

    public static final String TAG = BaseThreadPoolService.class.getSimpleName();
    private ExecutorService executor;

    @Override
    public void onDestroy() {
        try {
            executor.shutdown();
        } catch (Exception e) {
            Logger.w(TAG, "", e);
        }
    };

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onCreate() {
        super.onCreate();
        @SuppressWarnings("unchecked")
        final PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>(10, new ComparePriority());
        executor = new ThreadPoolExecutor(getCorePoolSize(), MAX_POOL_SIZE, 100L, TimeUnit.SECONDS, queue);
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        handleIntent(intent);
        return START_NOT_STICKY;
    }

    /**
     * This method should be implemented to handle the execution of the background threads it runs
     * in the UI thread, so don't do processor heavy operations
     */
    public abstract void handleIntent(Intent intent);

    public int getCorePoolSize() {
        return CORE_POOL_SIZE;
    }

    /** @return the executor */
    public ExecutorService getExecutor() {
        return executor;
    }

    public enum DownloadPriority {
        NORMAL, HIGH;
    }

    /**
     * @author darko.grozdanovski
     * @param <T>
     */
    private static class ComparePriority<T extends WorkerPriority> implements Comparator<T> {

        @Override
        public int compare(final T o1, final T o2) {
            if (o1.getPriority() == DownloadPriority.HIGH) {
                return -1;
            }
            if (o1.equals(o2)) {
                return 0;
            }
            return 1;
        }
    }

    /**
     * Implement in the worker to be able to prioritize the execution
     * 
     * @author darko.grozdanovski
     */
    public interface WorkerPriority {

        public DownloadPriority getPriority();
    }
}
