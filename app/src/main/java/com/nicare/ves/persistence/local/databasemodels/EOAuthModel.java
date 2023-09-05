package com.nicare.ves.persistence.local.databasemodels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "auth")
public class EOAuthModel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "right")
    private String rightThumb;
    @ColumnInfo(name = "left")
    private String leftThumb;
    private String question;
    private String answer;
    private String password;

    public EOAuthModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRightThumb() {
        return rightThumb;
    }

    public void setRightThumb(String rightThumb) {
        this.rightThumb = rightThumb;
    }

    public String getLeftThumb() {
        return leftThumb;
    }

    public void setLeftThumb(String leftThumb) {
        this.leftThumb = leftThumb;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
