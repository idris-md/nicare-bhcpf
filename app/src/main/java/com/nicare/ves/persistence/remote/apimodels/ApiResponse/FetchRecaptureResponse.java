package com.nicare.ves.persistence.remote.apimodels.ApiResponse;

import com.google.gson.annotations.Expose;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Reproductive;

import java.util.List;

public class FetchRecaptureResponse extends BaseResponse {

    @Expose
    private List<ReCapture> reCaptureList;

    @Expose
    private List<Reproductive> reproductiveList;


    public FetchRecaptureResponse() {
    }

    public List<ReCapture> getReCaptureList() {
        return reCaptureList;
    }

    public void setReCaptureList(List<ReCapture> reCaptureList) {
        this.reCaptureList = reCaptureList;
    }

    public List<Reproductive> getReproductiveList() {
        return reproductiveList;
    }

    public void setReproductiveList(List<Reproductive> reproductiveList) {
        this.reproductiveList = reproductiveList;
    }
}
