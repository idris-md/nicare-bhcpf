package com.nicare.ves.persistence.remote.apimodels.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UploadResponse {
    @Expose
    private int status;
    @Expose
    private String message;
    @Expose
    @SerializedName("lga_limit")
    private int lgaLimit;
    @Expose
    private List<RowID> pins;

    public UploadResponse() {}

    public UploadResponse(int status, List<RowID> pin) {
        this.status = status;
        this.pins = pin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<RowID> getPin() {
        return pins;
    }

    public void setPin(List<RowID> pin) {
        this.pins = pin;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<RowID> getPins() {
        return pins;
    }

    public void setPins(List<RowID> pins) {
        this.pins = pins;
    }

    public int getLgaLimit() {
        return lgaLimit;
    }

    public void setLgaLimit(int lgaLimit) {
        this.lgaLimit = lgaLimit;
    }
}
