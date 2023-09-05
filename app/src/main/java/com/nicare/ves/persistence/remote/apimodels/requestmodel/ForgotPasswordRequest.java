package com.nicare.ves.persistence.remote.apimodels.requestmodel;

public class ForgotPasswordRequest {
    private String id;

    public ForgotPasswordRequest() {
    }

    public ForgotPasswordRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
