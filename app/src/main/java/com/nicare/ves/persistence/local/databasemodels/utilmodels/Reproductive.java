package com.nicare.ves.persistence.local.databasemodels.utilmodels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "nin_table", indices = @Index(value = {"nin"}, unique = true))
public class Reproductive {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Expose
//    @NonNull
    private String name;

    @Expose
    @SerializedName("first_name")
    @Ignore
    private String firstName;

    @Expose
    @Ignore
    @SerializedName("surname")
    private String surname;
    @Expose
    @Ignore
    @SerializedName("other_name")
    private String otherName;

    @Expose
//    @NonNull
    @SerializedName("enrolment_number")
    private String nicareId;

    @Expose
    private String nin;

    private boolean isNow;

    public Reproductive() {
    }

    @Ignore
    public Reproductive(String name, String nicareId, String nin, boolean isNow) {
        this.name = name;
        this.nicareId = nicareId;
        this.nin = nin;
        this.isNow = isNow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        String fullName;
        if (otherName != null) {
            fullName = surname + " " + otherName + " " + firstName;
        } else {
            fullName = surname + " " + firstName;
        }

        return fullName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNicareId() {
        return nicareId;
    }

    public void setNicareId(String nicareId) {
        this.nicareId = nicareId;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public boolean isNow() {
        return isNow;
    }

    public void setNow(boolean now) {
        isNow = now;
    }
}
