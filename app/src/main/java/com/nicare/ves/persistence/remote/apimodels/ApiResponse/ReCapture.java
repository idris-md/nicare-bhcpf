package com.nicare.ves.persistence.remote.apimodels.ApiResponse;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Fingerprint;

import org.jetbrains.annotations.NonNls;

@Entity(tableName = "recapture_table",indices = @Index(value = {"nicareId"}, unique = true))
public class ReCapture {

    @PrimaryKey(autoGenerate = true)
    @Expose
    private int id;

    @NonNull
    @Expose
    String firstName;
    @Expose
    @NonNull
    String lastName;
    @Expose
    String otherName;
    @Expose
    String phone;
    @Expose
    @NonNull
    String provider;

    @Expose
    @SerializedName("wardname")
    @NonNull
    String ward;

    @Expose
    @NonNull

    private String nicareId;
    @Expose
    private String photo;
    @Expose
    @Ignore
    private Fingerprint fingerprint;
    @Expose
    private boolean recaptured;

    @Expose
    @SerializedName("approval_comment")
    private int issue_code;

    public ReCapture() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNicareId() {
        return nicareId;
    }

    public void setNicareId(String nicareId) {
        this.nicareId = nicareId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Fingerprint getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(Fingerprint fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public boolean isRecaptured() {
        return recaptured;
    }

    public void setRecaptured(boolean recaptured) {
        this.recaptured = recaptured;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getIssue_code() {
        return issue_code;
    }

    public void setIssue_code(int issue_code) {
        this.issue_code = issue_code;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }
}
