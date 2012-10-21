/**
 *
 */
package com.dg.libs.rest.services;

import java.util.LinkedList;

import com.dg.android.logger.Logger;

/** @author darko.grozdanovski */
public abstract class ThreadCountObserver {

    public static final String TAG = ThreadCountObserver.class.getSimpleName();

    public abstract void onThreadsFinished();

    public abstract void newRunnableRegistered();

    LinkedList<Runnable> observerList = new LinkedList<Runnable>();

    public void registerRunnable(final Runnable r) {
        synchronized (observerList) {
            observerList.add(r);
        }
        newRunnableRegistered();
        Logger.d(TAG, "observer list size on register: " + observerList.size());
    }

    public void unregisterRunnable(final Runnable r) {
        synchronized (observerList) {
            observerList.remove(r);
        }
        Logger.d(TAG, "observer list size on unregister: " + observerList.size());
        if (observerList.size() == 0) {
            Logger.d(TAG, "observer calling finish method");
            onThreadsFinished();
        }
    }
}
