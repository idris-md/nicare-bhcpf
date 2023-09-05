package com.nicare.ves.ui.auth.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AuthResource<T> {

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;

    public AuthResource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> AuthResource<T> success(@Nullable T data) {
        return new AuthResource<>(Status.SUCCESS, data, null);
    }

    public static <T> AuthResource<T> error(@Nullable T data, @Nullable String message) {
        return new AuthResource<>(Status.ERROR, data, message);
    }

    public static <T> AuthResource<T> loading(@Nullable T data) {
        return new AuthResource<>(Status.LOADING, data, null);
    }

    public static <T> AuthResource<T> not_authenticated(@Nullable T data) {
        return new AuthResource<>(Status.NOT_AUTHENTICATED, data, null);
    }

    public static <T> AuthResource<T> blocked(@Nullable T data) {
        return new AuthResource<>(Status.BLOCKED, data, null);
    }

    public static <T> AuthResource<T> suspend(@Nullable T data) {
        return new AuthResource<>(Status.SUSPENDED, data, null);
    }

    public static <T> AuthResource<T> update(@Nullable T data) {
        return new AuthResource<>(Status.UPDATE, data, null);
    }

    public enum Status {SUCCESS, ERROR, LOADING, NOT_AUTHENTICATED, BLOCKED, SUSPENDED, UPDATE}

}
