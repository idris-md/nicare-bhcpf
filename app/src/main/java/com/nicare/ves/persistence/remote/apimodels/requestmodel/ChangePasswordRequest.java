package com.nicare.ves.persistence.remote.apimodels.requestmodel;

import com.google.gson.annotations.Expose;

public class ChangePasswordRequest {

    @Expose
    private String password;

    public ChangePasswordRequest() {}

    public ChangePasswordRequest( String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
