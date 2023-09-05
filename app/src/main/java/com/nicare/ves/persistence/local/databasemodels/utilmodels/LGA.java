package com.nicare.ves.persistence.local.databasemodels.utilmodels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "lga_table", indices = @Index(value = {"lgaId"}, unique = true))
public class LGA {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("id")
    @Expose
    String lgaId;
    @Expose
    @SerializedName("lga")
    String name;

    public LGA() {}

    @Ignore
    public LGA(String lgaId) {
        this.lgaId = lgaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLgaId() {
        return lgaId;
    }

    public void setLgaId(String lgaId) {
        this.lgaId = lgaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LGA)) return false;

        LGA product = (LGA) obj;
        boolean isEqual = lgaId.equals(product.getLgaId());
        return isEqual;
    }

    @Override
    public int hashCode() {
        return lgaId == null ? 0 : lgaId.hashCode();
    }

}
