package com.nicare.ves.persistence.local.databasemodels.enrolmentmodels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity(tableName = "vulnerable_table", indices = @Index(value = {"instantId"}, unique = true))
public class Vulnerable {

    @PrimaryKey(autoGenerate = true)
    @Expose
    private int id;

    @Expose
    private String benefactor;
    @Expose
    private String firstName;
    @Expose
    private String surName;
    @Expose
    private String otherName;

    @Expose
    private String nin;

    @Expose
    @ColumnInfo(defaultValue = "")
    private String guardianNIN;

    @Expose
    @ColumnInfo(defaultValue = "")
    private String guardianID;

    @Expose
    private String dateOfBirth;
    @Expose
    private String maritalStatus;
    @Expose
    private String gender;

    @Expose
    private String phone;
    @Expose
    private String occupation;
    @Expose
    private String category;

    @Expose
    private String email;
    @Expose
    private String address;
    @Expose
    private String ward;
    @Expose
    private String lga;
    @Expose
    private String provider;

    @Expose
    private String nkName;
    @Expose
    private String nkRelationship;
    @Expose
    private String nkPhone;

    @Expose
    private String photo;
    @Expose
    private String settlement;
    private String instantId;
    @Expose
    @Ignore
    private Fingerprint biometric;

    @Expose
    private String regDate;
    @Expose
    private String stateOfOrigin;
    @Expose
    private String lgaOfOrigin;
    @Expose
    private boolean pregnant;
    @Expose
    private String disability;
    @Expose
    private String community;


    public Vulnerable() {
    }

    @Ignore
    public Vulnerable(int id, String benefactor, String firstName, String surName, String otherName, String dateOfBirth, String maritalStatus, String gender, String phone, String occupation, String category, String email, String address, String state, String lga, String provider, String nkName, String nkRelationship, String nkPhone, String photo, Fingerprint biometric, String regDate, Boolean pregnant, String disability, String community, String guardianNIN, String guardianID) {
        this.id = id;
        this.benefactor = benefactor;
        this.firstName = firstName;
        this.surName = surName;
        this.otherName = otherName;
        this.dateOfBirth = dateOfBirth;
        this.maritalStatus = maritalStatus;
        this.gender = gender;
        this.phone = phone;
        this.occupation = occupation;
        this.category = category;
        this.email = email;
        this.address = address;
        this.ward = state;
        this.lga = lga;
        this.provider = provider;
        this.nkName = nkName;
        this.nkRelationship = nkRelationship;
        this.nkPhone = nkPhone;
        this.photo = photo;
        this.biometric = biometric;
        this.regDate = regDate;
        this.pregnant = pregnant;
        this.disability = disability;
        this.community = community;
        this.guardianNIN = guardianNIN;
        this.guardianID = guardianID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getBenefactor() {
        return benefactor;
    }

    public void setBenefactor(String benefactor) {
        this.benefactor = benefactor;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getNkName() {
        return nkName;
    }

    public void setNkName(String nkName) {
        this.nkName = nkName;
    }

    public String getNkRelationship() {
        return nkRelationship;
    }

    public void setNkRelationship(String nkRelationship) {
        this.nkRelationship = nkRelationship;
    }

    public String getNkPhone() {
        return nkPhone;
    }

    public void setNkPhone(String nkPhone) {
        this.nkPhone = nkPhone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Fingerprint getBiometric() {
        return biometric;
    }

    public void setBiometric(Fingerprint biometric) {
        this.biometric = biometric;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getStateOfOrigin() {
        return stateOfOrigin;
    }

    public void setStateOfOrigin(String stateOfOrigin) {
        this.stateOfOrigin = stateOfOrigin;
    }

    public String getLgaOfOrigin() {
        return lgaOfOrigin;
    }

    public void setLgaOfOrigin(String lgaOfOrigin) {
        this.lgaOfOrigin = lgaOfOrigin;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    public String getInstantId() {
        return instantId;
    }

    public void setInstantId(String instantId) {
        this.instantId = instantId;
    }

    public boolean isPregnant() {
        return pregnant;
    }

    public void setPregnant(boolean pregnant) {
        this.pregnant = pregnant;
    }

    public String getDisability() {
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getGuardianNIN() {
        return guardianNIN;
    }

    public void setGuardianNIN(String guardianNIN) {
        this.guardianNIN = guardianNIN;
    }

    public String getGuardianID() {
        return guardianID;
    }

    public void setGuardianID(String guardianID) {
        this.guardianID = guardianID;
    }
}
