package com.nicare.ves.persistence.local.databasemodels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "benefactor", indices = @Index(value = {"name"}, unique = true))
public class Benefactor implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Expose
    @SerializedName("benefactor")
    private String name;


    public Benefactor() { }

    @Ignore
    public Benefactor(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHcpcount(int hcpcount) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Benefactor)) return false;
        Benefactor product = (Benefactor) obj;
        return  name.equalsIgnoreCase(product.getName());
    }

    @Override
    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }

}
