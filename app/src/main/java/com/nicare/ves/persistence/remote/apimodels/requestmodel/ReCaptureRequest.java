package com.nicare.ves.persistence.remote.apimodels.requestmodel;

import com.google.gson.annotations.Expose;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.ReCapture;

import java.util.List;

public class ReCaptureRequest {
    @Expose
    private List<ReCapture> recaptured_data;

    public ReCaptureRequest() {
    }

    public ReCaptureRequest(List<ReCapture> reCaptures) {
        this.recaptured_data = reCaptures;
    }

    public List<ReCapture> getRecaptured_data() {
        return recaptured_data;
    }

    public void setRecaptured_data(List<ReCapture> recaptured_data) {
        this.recaptured_data = recaptured_data;
    }
}
