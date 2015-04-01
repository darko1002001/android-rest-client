package com.dg.libs.rest.callbacks;

import android.app.Activity;

import java.lang.ref.WeakReference;

public class ActivityBoundHttpCallback<T> extends BoundCallback<T> {

  private static final String TAG = ActivityBoundHttpCallback.class.getSimpleName();
  WeakReference<Activity> activityWeakReference;

  public ActivityBoundHttpCallback(Activity activity, HttpCallback<T> callback) {
    super(callback);
    this.activityWeakReference = new WeakReference<>(activity);
  }

  @Override
  public boolean isRegistered() {
    Activity activity = activityWeakReference.get();
    return activity != null && activity.isFinishing() == false;
  }

}
