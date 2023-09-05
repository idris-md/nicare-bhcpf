package com.nicare.ves.persistence;

public class ImageProcessResult {

    private boolean leftCheek;
    private boolean rightCheek;
    private boolean isLeftEyeOpen;
    private boolean isRightEyeOpen;
    private boolean isLeftEarOpen;
    private boolean isRightEarOpen;
    private boolean isSmiling;

    public boolean isLeftEyeOpen() {
        return isLeftEyeOpen;
    }

    public void setLeftEyeOpen(boolean leftEyeOpen) {
        isLeftEyeOpen = leftEyeOpen;
    }

    public boolean isRightEyeOpen() {
        return isRightEyeOpen;
    }

    public void setRightEyeOpen(boolean rightEyeOpen) {
        isRightEyeOpen = rightEyeOpen;
    }

    public boolean isLeftEarOpen() {
        return isLeftEarOpen;
    }

    public void setLeftEarOpen(boolean leftEarOpen) {
        isLeftEarOpen = leftEarOpen;
    }

    public boolean isRightEarOpen() {
        return isRightEarOpen;
    }

    public void setRightEarOpen(boolean rightEarOpen) {
        isRightEarOpen = rightEarOpen;
    }

    public boolean isSmiling() {
        return isSmiling;
    }

    public void setSmiling(boolean smiling) {
        isSmiling = smiling;
    }

    public boolean isLeftCheek() {
        return leftCheek;
    }

    public void setLeftCheek(boolean leftCheek) {
        this.leftCheek = leftCheek;
    }

    public boolean isRightCheek() {
        return rightCheek;
    }

    public void setRightCheek(boolean rightCheek) {
        this.rightCheek = rightCheek;
    }
}
