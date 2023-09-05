package com.nicare.ves.persistence.remote.apimodels;

import com.google.gson.annotations.Expose;

public class DeviceInfo {

     @Expose private String deviceModel;
     @Expose private String deviceId;
     @Expose private String deviceIMEI;

    public DeviceInfo() {
    }

    public DeviceInfo( String deviceModel, String deviceId, String deviceIMEI) {
        this.deviceModel = deviceModel;
        this.deviceId = deviceId;
        this.deviceIMEI = deviceIMEI;
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
