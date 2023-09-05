package com.nicare.ves.persistence.remote.apimodels.requestmodel;

import com.google.gson.annotations.Expose;

public class LoginRequest {

    @Expose
    private String username;
    @Expose
    private String password;
    @Expose
    private String deviceModel;
    @Expose
    private String deviceId;
    @Expose
    private String deviceIMEI;
    @Expose
    private int versionCode;
    @Expose
    private boolean isInit;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password, String deviceModel, String deviceId, String deviceIMEI, int versionCode) {
        this.username = username;
        this.password = password;
        this.deviceModel = deviceModel;
        this.deviceId = deviceId;
        this.deviceIMEI = deviceIMEI;
        this.versionCode = versionCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceIMEI() {
        return deviceIMEI;
    }

    public void setDeviceIMEI(String deviceIMEI) {
        this.deviceIMEI = deviceIMEI;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }
}
