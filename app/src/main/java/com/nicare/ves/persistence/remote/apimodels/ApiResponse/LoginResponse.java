package com.nicare.ves.persistence.remote.apimodels.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nicare.ves.persistence.local.databasemodels.Benefactor;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Facility;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.LGA;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;

import java.util.List;

public class LoginResponse extends BaseResponse {

    @Expose
    private String token;
    @Expose
    private String name;
    @Expose
    private String phone;
    @Expose
    private String lga;
    @Expose
    private String lga_name;
    @Expose
    private String deviceId;
    @Expose
    private int versionCode;
    @Expose
    private String versionName;
    @Expose
    private String downloadUrl;
    @SerializedName("ward")
    @Expose
    private List<Ward> ward;
    @SerializedName("lga_list")
    @Expose
    private List<LGA> lga_list;
    @SerializedName("providers")
    @Expose
    private List<Facility> facilities;
    @Expose
    private List<ReCapture> reCaptureList;

    @Expose
    private List<Benefactor> benefactors;
    @Expose
    private int minVersionCode;
    @Expose
    private String xcvg;
    @Expose
    @SerializedName("lga_limit")
    private int lgaLimit;
    @Expose
    @SerializedName("enrolment_limit")
    private int enrolmentLimit;

    public LoginResponse() {
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

    public List<Ward> getWard() {
        return ward;
    }

    public void setWard(List<Ward> ward) {
        this.ward = ward;
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

    public String getXcvg() {
        return xcvg;
    }

    public void setXcvg(String xcvg) {
        this.xcvg = xcvg;
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

    public String getLga_name() {
        return lga_name;
    }

    public void setLga_name(String lga_name) {
        this.lga_name = lga_name;
    }

    public List<LGA> getLga_list() {
        return lga_list;
    }

    public void setLga_list(List<LGA> lga_list) {
        this.lga_list = lga_list;
    }

    public List<ReCapture> getReCaptureList() {
        return reCaptureList;
    }

    public void setReCaptureList(List<ReCapture> reCaptureList) {
        this.reCaptureList = reCaptureList;
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

    public List<Benefactor> getBenefactors() {
        return benefactors;
    }

    public void setBenefactors(List<Benefactor> benefactors) {
        this.benefactors = benefactors;
    }
}

