package com.nicare.ves;

import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.di.DaggerAppComponent;

import java.net.SocketException;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import io.reactivex.plugins.RxJavaPlugins;

public class BaseApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PrefUtils.getInstance(this).setSynceable(true);
//        RxJavaPlugins.setErrorHandler(); { e ->
//            if (e is UndeliverableException) {
//                // Merely log undeliverable exceptions
//                log.error(e.message)
//            } else {
//                // Forward all others to current thread's uncaught exception handler
//                Thread.currentThread().also { thread ->
//                        thread.uncaughtExceptionHandler.uncaughtException(thread, e)
//                }
//            };
    }

}
