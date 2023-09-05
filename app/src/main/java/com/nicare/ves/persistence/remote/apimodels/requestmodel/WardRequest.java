package com.nicare.ves.persistence.remote.apimodels.requestmodel;

import com.google.gson.annotations.Expose;

public class WardRequest {
    @Expose
    String name;

    public WardRequest(String name) {
        this.name = name;
    }

    public WardRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
