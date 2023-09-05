package com.nicare.ves.persistence.remote.apimodels.requestmodel;

import com.google.gson.annotations.Expose;

public class LogoutRequest {


     @Expose private String username;
     @Expose private String password;
     @Expose private String deviceModel;
     @Expose private String deviceId;
     @Expose private String deviceIMEI;

    public LogoutRequest() {
    }

    public LogoutRequest( String username, String password, String deviceModel, String deviceId, String deviceIMEI) {
        this.username = username;
        this.password = password;
        this.deviceModel = deviceModel;
        this.deviceId = deviceId;
        this.deviceIMEI = deviceIMEI;
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

}
