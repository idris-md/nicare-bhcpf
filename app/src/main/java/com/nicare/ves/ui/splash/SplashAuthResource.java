package com.nicare.ves.ui.splash;

import androidx.annotation.NonNull;


public class SplashAuthResource<T> {

    @NonNull
    public final Status status;
//    @Nullable
//    public final T data;
//    @Nullable
//    public final String message;

    public SplashAuthResource(@NonNull Status status) {
        this.status = status;
    }

    public static <T> SplashAuthResource<T> success() {
        return new SplashAuthResource<>(Status.SUCCESS);
    }

    public static <T> SplashAuthResource<T> blocked() {
        return new SplashAuthResource<>(Status.BLOCKED);
    }

    public static <T> SplashAuthResource<T> suspend() {
        return new SplashAuthResource<>(Status.SUSPEND);
    }

    public static <T> SplashAuthResource<T> loading() {
        return new SplashAuthResource<>(Status.LOADING);
    }

    public static <T> SplashAuthResource<T> notSecured() {
        return new SplashAuthResource<>(Status.NOT_SECURED);
    }

    public static <T> SplashAuthResource<T> notAuthenticated() {
        return new SplashAuthResource<>(Status.NOT_AUTHENTICATED);
    }

    public enum Status {SUCCESS, BLOCKED, SUSPEND, LOADING, NOT_SECURED, NOT_AUTHENTICATED}
}
