package com.nicare.ves.persistence.remote.apimodels.requestmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivationRequest {

   @Expose
   @SerializedName( "authorization_code")
   private String authorizationCode;
   @Expose
   @SerializedName("device_id")
   private String deviceId;
   @Expose
   @SerializedName( "device_model")
   private String deviceModel;

   public ActivationRequest() {
   }

   public String getAuthorizationCode() {
      return authorizationCode;
   }

   public void setAuthorizationCode(String authorizationCode) {
      this.authorizationCode = authorizationCode;
   }

   public String getDeviceId() {
      return deviceId;
   }

   public void setDeviceId(String deviceId) {
      this.deviceId = deviceId;
   }

   public String getDeviceModel() {
      return deviceModel;
   }

   public void setDeviceModel(String deviceModel) {
      this.deviceModel = deviceModel;
   }
}
