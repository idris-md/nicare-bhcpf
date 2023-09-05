package com.nicare.ves.persistence.remote.apimodels;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "activity_log_table", indices = @Index(value = {"date"}, unique = true))
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String date;
    private String lastSyncTime;
    private int registration;
    private int sync;

    public Transaction() {
    }

    @Ignore
    public Transaction(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(String lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public int getRegistration() {
        return registration;
    }

    public void setRegistration(int registration) {
        this.registration = registration;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

}
