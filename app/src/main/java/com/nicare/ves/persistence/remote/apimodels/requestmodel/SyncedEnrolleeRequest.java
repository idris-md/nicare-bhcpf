package com.nicare.ves.persistence.remote.apimodels.requestmodel;

import com.google.gson.annotations.Expose;

public class SyncedEnrolleeRequest {
    @Expose
    private String name;

    public SyncedEnrolleeRequest() {
    }

    public SyncedEnrolleeRequest(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
