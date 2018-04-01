package com.example.devbox.stockhawkrewrite.Util;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class MyIdlingResources implements IdlingResource {


    private static final String NAME = "MY_IDLING_RESORUCE_TAG";
    private static final AtomicBoolean sIsIdleNow = new AtomicBoolean(true);
    private static MyIdlingResources sIdlingResources;
    private volatile ResourceCallback mResourceCallback;

    private MyIdlingResources() {

    }

    public static MyIdlingResources getInstance(){
        if(sIdlingResources == null){
            sIdlingResources = new MyIdlingResources();
        }
        return sIdlingResources;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isIdleNow() {
        return sIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mResourceCallback = callback;
    }

    public void setIdleState(boolean newIdleState){
        sIsIdleNow.set(newIdleState);
        if(isIdleNow() && mResourceCallback != null){
            mResourceCallback.onTransitionToIdle();
        }
    }

}
