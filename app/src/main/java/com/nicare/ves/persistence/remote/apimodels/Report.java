package com.nicare.ves.persistence.remote.apimodels;

public class Report {
    private int spouseCount;
    private int childCount;
    private int capturedCount;
    private int principalCount;

    public Report() {
    }

    public int getSpouseCount() {
        return spouseCount;
    }

    public void setSpouseCount(int spouseCount) {
        this.spouseCount = spouseCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public int getCapturedCount() {
        return capturedCount;
    }

    public void setCapturedCount(int capturedCount) {
        this.capturedCount = capturedCount;
    }

    public int getPrincipalCount() {
        return principalCount;
    }

    public void setPrincipalCount(int principalCount) {
        this.principalCount = principalCount;
    }
}
