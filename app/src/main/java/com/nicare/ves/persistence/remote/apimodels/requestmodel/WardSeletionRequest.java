package com.nicare.ves.persistence.remote.apimodels.requestmodel;

import com.google.gson.annotations.Expose;

public class WardSeletionRequest {
    @Expose
    private String wardId;

    public WardSeletionRequest() {
    }

    public WardSeletionRequest(String ward_id) {
        this.wardId = ward_id;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }
}
