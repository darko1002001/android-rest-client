/**
 *
 */
package com.dg.libs.rest.services;

import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.dg.android.logger.Logger;
import com.dg.libs.rest.HttpRequest;

/** @author darko.grozdanovski */
public abstract class BaseObservableThreadPoolServiceService extends BaseThreadPoolService {

    public static final String TAG = BaseObservableThreadPoolServiceService.class.getSimpleName();

    /**
     * Use to shut down the service when done, register the worker when its started, unregister when
     * its completed
     */
    public ThreadCountObserver observer = new ThreadCountObserver() {

        private final Handler shutdownHandler = new Handler();
        private final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                Logger.d("Shutting down " + TAG);
                stopSelf();
            }
        };

        @Override
        public void onThreadsFinished() {
            shutdownHandler.postDelayed(runnable, TimeUnit.SECONDS.toMillis(60L));
        }

        @Override
        public void newRunnableRegistered() {
            shutdownHandler.removeCallbacks(runnable);
        }
    };

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Used to submit prioritized tasks to the Queue for the file download
     * 
     * @author darko.grozdanovski
     */
    class WorkerThread implements Runnable, WorkerPriority {

        private final DownloadPriority downloadPriority;
        private final HttpRequest request;

        public WorkerThread(final DownloadPriority downloadPriority, final HttpRequest request) {
            super();
            this.downloadPriority = downloadPriority;
            this.request = request;
        }

        /** @return the priority */
        @Override
        public DownloadPriority getPriority() {
            return downloadPriority;
        }

        @Override
        public void run() {
            observer.registerRunnable(this);
            request.execute();
            observer.unregisterRunnable(this);
        }
    }
}
