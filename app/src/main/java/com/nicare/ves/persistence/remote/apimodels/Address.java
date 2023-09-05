package com.nicare.ves.persistence.remote.apimodels;

public class Address {

    String ward;
    String lga;

    public Address() {
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }
}
