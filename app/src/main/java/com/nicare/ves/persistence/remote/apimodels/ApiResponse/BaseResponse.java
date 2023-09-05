package com.nicare.ves.persistence.remote.apimodels.ApiResponse;

import com.google.gson.annotations.Expose;

public class BaseResponse {

    @Expose int status;
    @Expose String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
