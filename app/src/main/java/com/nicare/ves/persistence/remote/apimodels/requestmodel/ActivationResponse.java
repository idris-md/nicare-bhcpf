package com.nicare.ves.persistence.remote.apimodels.requestmodel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivationResponse {

   @Expose
   @SerializedName( "status")
   private int status;
   @Expose
   @SerializedName( "code")
   private String code;
   @Expose
   @SerializedName( "messages")
   private String message;

   public ActivationResponse() {}

   public int getStatus() {
      return status;
   }

   public void setStatus(int status) {
      this.status = status;
   }

   public String getCode() {
      return code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}
