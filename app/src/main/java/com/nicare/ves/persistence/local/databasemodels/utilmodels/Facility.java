package com.nicare.ves.persistence.local.databasemodels.utilmodels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "facilities_table", indices = @Index(value = {"hcpcode"}, unique = true))
public class Facility {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @Expose
    private String hcpname;
    @Expose
    private String hcpcode;
    @Expose
    @SerializedName("ward_id")
    private String hcpward;

    @Expose
    private String hcpzone;
    @Expose
    @SerializedName("lga_id")
    private String hcplga;
    @Expose
    private int hcpcap;
    @Expose
    private int hcpcount;

    public Facility() { }

@Ignore
    public Facility(String hcpcode) {
        this.hcpcode = hcpcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHcpname() {
        return hcpname;
    }

    public void setHcpname(String hcpname) {
        this.hcpname = hcpname;
    }

    public String getHcpcode() {
        return hcpcode;
    }

    public void setHcpcode(String hcpcode) {
        this.hcpcode = hcpcode;
    }

    public String getHcpward() {
        return hcpward;
    }

    public void setHcpward(String hcpward) {
        this.hcpward = hcpward;
    }

    public String getHcpzone() {
        return hcpzone;
    }

    public void setHcpzone(String hcpzone) {
        this.hcpzone = hcpzone;
    }

    public String getHcplga() {
        return hcplga;
    }

    public void setHcplga(String hcplga) {
        this.hcplga = hcplga;
    }

    public int getHcpcap() {
        return hcpcap;
    }

    public void setHcpcap(int hcpcap) {
        this.hcpcap = hcpcap;
    }

    public int getHcpcount() {
        return hcpcount;
    }

    public void setHcpcount(int hcpcount) {
        this.hcpcount = hcpcount;
    }

    @NonNull
    @Override
    public String toString() {
        return hcpname;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Facility)) return false;

        Facility product = (Facility) obj;
        return  hcpcode.equals(product.getHcpcode());

    }

    @Override
    public int hashCode() {
        return hcpcode == null ? 0 : hcpcode.hashCode();
    }

}
