package com.nicare.ves.persistence.local.databasemodels.utilmodels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

@Entity(tableName = "nin_table", indices = @Index(value = {"nin"}, unique = true))
public class NIN implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    @Expose
    private String nicareId;
    @Expose
    private String nin;
    private boolean isUpdated;


    public NIN() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getNicareId() {
        return nicareId;
    }

    public void setNicareId(@NonNull String nicareId) {
        this.nicareId = nicareId;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }
}
