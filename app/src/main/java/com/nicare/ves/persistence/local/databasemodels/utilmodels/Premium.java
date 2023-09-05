package com.nicare.ves.persistence.local.databasemodels.utilmodels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

@Entity(tableName = "premium_table", indices = @Index(value = {"pin"}, unique = true))
public class Premium implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    @Expose
    private String pin;
    @Expose
    private String serial_no;
    @Expose
    private String pin_type;
    @Expose
    private String date_expired;
    @Expose
    private String sales_code;
    private boolean is_used;
    @Expose
    private boolean sales_status;

    private String soldAt;

    public Premium() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getPin_type() {
        return pin_type;
    }

    public void setPin_type(String pin_type) {
        this.pin_type = pin_type;
    }

    public String getDate_expired() {
        return date_expired;
    }

    public void setDate_expired(String date_expired) {
        this.date_expired = date_expired;
    }

    public String getSales_code() {
        return sales_code;
    }

    public void setSales_code(String sales_code) {
        this.sales_code = sales_code;
    }

    public boolean isIs_used() {
        return is_used;
    }

    public void setIs_used(boolean is_used) {
        this.is_used = is_used;
    }

    public boolean isSales_status() {
        return sales_status;
    }

    public void setSales_status(boolean sales_status) {
        this.sales_status = sales_status;
    }

    public String getSoldAt() {
        return soldAt;
    }

    public void setSoldAt(String soldAt) {
        this.soldAt = soldAt;
    }

}
