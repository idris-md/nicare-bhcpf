package com.nicare.ves.persistence.remote.apimodels.requestmodel;

import com.google.gson.annotations.Expose;

public class LGARequest {

    @Expose String name;

    public LGARequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
