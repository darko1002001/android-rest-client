package com.dg.libs.rest.callbacks;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;

import java.lang.ref.WeakReference;

public class FragmentBoundHttpCallback<T> extends BoundCallback<T> {

  private static final String TAG = FragmentBoundHttpCallback.class.getSimpleName();
  WeakReference<Fragment> fragmentWeakReference;

  public FragmentBoundHttpCallback(Fragment fragment, HttpCallback<T> callback) {
    super(callback);
    this.fragmentWeakReference = new WeakReference<>(fragment);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  @Override
  public boolean isRegistered() {
    Fragment fragment = fragmentWeakReference.get();
    return fragment != null && fragment.isAdded() && fragment.isInLayout();
  }
}
