package com.nicare.ves.persistence.local.databasemodels.enrolmentmodels;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity(tableName = "fingerprints_table")
public class Fingerprint implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @Expose
    private long beneficiary_id;

    private int beneficiary_type;
    @Expose
    private String leftOne;
    @Expose
    private String leftTwo;
    @Expose
    private String leftThree;
    @Expose
    private String leftFour;
    @Expose
    private String leftFive;
    @Expose
    private String rightOne;
    @Expose
    private String rightTwo;
    @Expose
    private String rightThree;
    @Expose
    private String rightFour;
    @Expose
    private String rightFive;
    @Expose
    private String failureReason;


    @Expose
    private String fingersPhoto;

    @Expose
    private boolean isPrincipalActive;

    public Fingerprint() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Ignore
    public Fingerprint(Parcel parcel) {
        leftOne = parcel.readString();
        leftTwo = parcel.readString();
        leftThree = parcel.readString();
        leftFour = parcel.readString();
        leftFive = parcel.readString();
        rightOne = parcel.readString();
        rightTwo = parcel.readString();
        rightThree = parcel.readString();
        rightFour = parcel.readString();
        rightFive = parcel.readString();
        fingersPhoto = parcel.readString();
        failureReason = parcel.readString();
    }

    public String getLeftOne() {
        return leftOne;
    }

    public void setLeftOne(String leftOne) {
        this.leftOne = leftOne;
    }

    public String getLeftTwo() {
        return leftTwo;
    }

    public void setLeftTwo(String leftTwo) {
        this.leftTwo = leftTwo;
    }

    public String getLeftThree() {
        return leftThree;
    }

    public void setLeftThree(String leftThree) {
        this.leftThree = leftThree;
    }

    public String getLeftFour() {
        return leftFour;
    }

    public void setLeftFour(String leftFour) {
        this.leftFour = leftFour;
    }

    public String getLeftFive() {
        return leftFive;
    }

    public void setLeftFive(String leftFive) {
        this.leftFive = leftFive;
    }

    public String getRightOne() {
        return rightOne;
    }

    public void setRightOne(String rightOne) {
        this.rightOne = rightOne;
    }

    public String getRightTwo() {
        return rightTwo;
    }

    public void setRightTwo(String rightTwo) {
        this.rightTwo = rightTwo;
    }

    public String getRightThree() {
        return rightThree;
    }

    public void setRightThree(String rightThree) {
        this.rightThree = rightThree;
    }

    public String getRightFour() {
        return rightFour;
    }

    public void setRightFour(String rightFour) {
        this.rightFour = rightFour;
    }

    public String getRightFive() {
        return rightFive;
    }

    public void setRightFive(String rightFive) {
        this.rightFive = rightFive;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public long getBeneficiary_id() {
        return beneficiary_id;
    }

    public void setBeneficiary_id(long beneficiary_id) {
        this.beneficiary_id = beneficiary_id;
    }

    public boolean isPrincipalActive() {
        return isPrincipalActive;
    }

    public void setPrincipalActive(boolean principalActive) {
        isPrincipalActive = principalActive;
    }

    public int getBeneficiary_type() {
        return beneficiary_type;
    }

    public void setBeneficiary_type(int beneficiary_type) {
        this.beneficiary_type = beneficiary_type;
    }

    public String getFingersPhoto() {
        return fingersPhoto;
    }

    public void setFingersPhoto(String fingersPhoto) {
        this.fingersPhoto = fingersPhoto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(leftOne);
        parcel.writeString(leftTwo);
        parcel.writeString(leftThree);
        parcel.writeString(leftFour);
        parcel.writeString(leftFive);

        parcel.writeString(rightOne);
        parcel.writeString(rightTwo);
        parcel.writeString(rightThree);
        parcel.writeString(rightFour);
        parcel.writeString(rightFive);
        parcel.writeString(fingersPhoto);
        parcel.writeString(failureReason);

    }

    public static final Creator<Fingerprint> CREATOR =
            new Creator<Fingerprint>() {

                @Override
                public Fingerprint createFromParcel(Parcel parcel) {
                    return new Fingerprint(parcel);
                }

                @Override
                public Fingerprint[] newArray(int size) {
                    return new Fingerprint[size];
                }

            };

}
