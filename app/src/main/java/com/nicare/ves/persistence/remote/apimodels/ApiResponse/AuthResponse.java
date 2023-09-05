package com.nicare.ves.persistence.remote.apimodels.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResponse extends BaseResponse {
    @Expose
    private int minVersionCode;
    @Expose private String versionName;
    @Expose private String downloadUrl;
    @Expose
    @SerializedName("enrolment_limit")
    private int limit;
    public AuthResponse() {}

    public int getMinVersionCode() {
        return minVersionCode;
    }

    public void setMinVersionCode(int minVersionCode) {
        this.minVersionCode = minVersionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
