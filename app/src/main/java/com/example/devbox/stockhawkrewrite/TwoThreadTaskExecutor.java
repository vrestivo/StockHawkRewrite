package com.example.devbox.stockhawkrewrite;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 */

public class TwoThreadTaskExecutor {

    private MyIOExecutor mMyIOExecutor;
    private MyMainThreadExecutor mMyMainThreadExecutor;

    public TwoThreadTaskExecutor() {
        mMyIOExecutor = new MyIOExecutor();
        mMyMainThreadExecutor = new MyMainThreadExecutor();
    }


    public MyIOExecutor iOExecutor(){
        return mMyIOExecutor;
    }

    public MyMainThreadExecutor mainThreadExecutor() {
        return mMyMainThreadExecutor;
    }


    public class MyIOExecutor implements Executor {

        private Executor mIOExecutor;

        public MyIOExecutor() {
            mIOExecutor = Executors.newSingleThreadExecutor();
        }

        @Override
        public void execute(@NonNull Runnable command) {
            mIOExecutor.execute(command);
        }
    }


    public class MyMainThreadExecutor implements Executor{

        private Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mMainThreadHandler.post(command);
        }
    }

    public void cleanup(){
        //TODO implement thred shutdown
    }

}
