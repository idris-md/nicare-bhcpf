package com.nicare.ves.persistence.remote.apimodels.ApiResponse;

import com.google.gson.annotations.Expose;

public class RowID {

    @Expose
    private String rid;

    @Expose
    private long id;

    @Expose
    private String message;

    public RowID() {
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
