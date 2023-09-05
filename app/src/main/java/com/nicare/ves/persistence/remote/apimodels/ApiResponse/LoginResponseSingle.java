package com.nicare.ves.persistence.remote.apimodels.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Facility;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;

import java.util.List;

public class LoginResponseSingle extends BaseResponse {

    @Expose
    private String token;
    @Expose
    private String name;
    @Expose
    private String phone;
    @Expose
    private String deviceId;
    @Expose
    private int versionCode;
    @Expose
    private String versionName;
    @Expose
    @SerializedName("downloadUrl")
    private String downloadUrl;
    @Expose
    private String lga;

    @Expose
    private int minVersionCode;
    @SerializedName("ward")
    @Expose
    private List<Ward> ward;
    @SerializedName("providers")
    @Expose
    private List<Facility> facilities;
    //    @Expose private List<ReCapture> reCaptureList;
    @Expose
    @SerializedName("enrolment_limit")
    private int enrolmentLimit;
    @Expose
    @SerializedName("lga_limit")
    private int lgaLimit;

    public LoginResponseSingle() {
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
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

    public String getDeviceId() {
        return deviceId;
    }


    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getMinVersionCode() {
        return minVersionCode;
    }

    public void setMinVersionCode(int minVersionCode) {
        this.minVersionCode = minVersionCode;
    }

    public List<Ward> getWard() {
        return ward;
    }

    public void setWard(List<Ward> ward) {
        this.ward = ward;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public int getLgaLimit() {
        return lgaLimit;
    }

    public void setLgaLimit(int lgaLimit) {
        this.lgaLimit = lgaLimit;
    }

    public int getEnrolmentLimit() {
        return enrolmentLimit;
    }

    public void setEnrolmentLimit(int enrolmentLimit) {
        this.enrolmentLimit = enrolmentLimit;
    }
//    public List<ReCapture> getReCaptureList() {
//        return reCaptureList;
//    }
//
//    public void setReCaptureList(List<ReCapture> reCaptureList) {
//        this.reCaptureList = reCaptureList;
//    }
}

