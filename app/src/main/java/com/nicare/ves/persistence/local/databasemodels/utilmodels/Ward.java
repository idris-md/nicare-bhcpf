package com.nicare.ves.persistence.local.databasemodels.utilmodels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "wards_table", indices = @Index(value = {"ward_id"}, unique = true))
public class Ward implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("id")
    @Expose
    String ward_id;
    @Expose
    String lga_id;
    @Expose
    @SerializedName("ward")
    String name;

    @Expose
    @SerializedName("enrolmentCap")
    int enrolmentCap;

    @Expose
    @SerializedName("total_enrolled")
    int totalEnrolledRemotely;

    @Expose
    @SerializedName("totalEnrolledLocally")
    int totalEnrolled;

    public Ward() {
    }

    @Ignore
    public Ward(String ward_id) {
        this.ward_id = ward_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWard_id() {
        return ward_id;
    }

    public void setWard_id(String ward_id) {
        this.ward_id = ward_id;
    }

    public String getLga_id() {
        return lga_id;
    }

    public void setLga_id(String lga_id) {
        this.lga_id = lga_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEnrolmentCap() {
        return enrolmentCap;
    }

    public void setEnrolmentCap(int enrolmentCap) {
        this.enrolmentCap = enrolmentCap;
    }

    public int getTotalEnrolled() {
        return totalEnrolled;
    }

    public void setTotalEnrolled(int totalEnrolled) {
        this.totalEnrolled = totalEnrolled;
    }

    public int getTotalEnrolledRemotely() {
        return totalEnrolledRemotely;
    }

    public void setTotalEnrolledRemotely(int totalEnrolledRemotely) {
        this.totalEnrolledRemotely = totalEnrolledRemotely;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Ward)) return false;

        Ward product = (Ward) obj;
        return ward_id.equals(product.getWard_id());

    }

    @Override
    public int hashCode() {
        return ward_id == null ? 0 : ward_id.hashCode();
    }

}
