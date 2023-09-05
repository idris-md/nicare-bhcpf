package com.nicare.ves.ui.splash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SplashDeviceAuthResource<T> {

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;

    public SplashDeviceAuthResource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> SplashDeviceAuthResource<T> success(@Nullable T data) {
        return new SplashDeviceAuthResource<>(Status.SUCCESS, data, null);
    }

    public static <T> SplashDeviceAuthResource<T> error(@Nullable T data) {
        return new SplashDeviceAuthResource<>(Status.ERROR, data, null);
    }

    public static <T> SplashDeviceAuthResource<T> update(@Nullable T data) {
        return new SplashDeviceAuthResource<>(Status.UPDATE, data, null);
    }

    public static <T> SplashDeviceAuthResource<T> loading() {
        return new SplashDeviceAuthResource<>(Status.LOADING, null, null);
    }

    public static <T> SplashDeviceAuthResource<T> notRecognise() {
        return new SplashDeviceAuthResource<>(Status.NOT_RECOGNISED, null, null);
    }

    public static <T> SplashDeviceAuthResource<T> blocked() {
        return new SplashDeviceAuthResource<>(Status.BLOCKED, null, null);
    }



    public enum Status {SUCCESS, ERROR, NOT_RECOGNISED, LOADING, BLOCKED, UPDATE}
}
