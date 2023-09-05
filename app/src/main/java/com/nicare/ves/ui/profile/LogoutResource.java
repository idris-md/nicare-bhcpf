package com.nicare.ves.ui.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LogoutResource<T> {

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;

    public LogoutResource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> LogoutResource<T> success(@Nullable T data) {
        return new LogoutResource<>(Status.SUCCESS, data, null);
    }

    public static <T> LogoutResource<T> error(@Nullable T data, @Nullable String message) {
        return new LogoutResource<>(Status.ERROR, data, message);
    }

    public static <T> LogoutResource<T> loading(@Nullable T data) {
        return new LogoutResource<>(Status.LOADING, data, null);
    }


    public enum Status {SUCCESS, ERROR, LOADING}

}
