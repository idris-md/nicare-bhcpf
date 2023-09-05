package com.nicare.ves.ui.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ChangePasswordResource<T> {

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;

    public ChangePasswordResource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> ChangePasswordResource<T> success(@Nullable T data) {
        return new ChangePasswordResource<>(Status.SUCCESS, data, null);
    }

    public static <T> ChangePasswordResource<T> error(@Nullable T data, @Nullable String message) {
        return new ChangePasswordResource<>(Status.ERROR, data, message);
    }

    public static <T> ChangePasswordResource<T> loading(@Nullable T data) {
        return new ChangePasswordResource<>(Status.LOADING, data, null);
    }


    public enum Status {SUCCESS, ERROR, LOADING}

}
