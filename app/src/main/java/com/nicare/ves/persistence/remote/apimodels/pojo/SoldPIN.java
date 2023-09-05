package com.nicare.ves.persistence.remote.apimodels.pojo;

import com.google.gson.annotations.Expose;

public class SoldPIN {

    @Expose
    private String pin;

    @Expose
    private  boolean sales_status;

    public SoldPIN() {
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean isSales_status() {
        return sales_status;
    }

    public void setSales_status(boolean sales_status) {
        this.sales_status = sales_status;
    }

}
