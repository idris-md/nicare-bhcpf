package com.nicare.ves.persistence.remote.apimodels.ApiResponse;

import com.google.gson.annotations.Expose;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Reproductive;
import java.util.List;

public class FetchReproductiveResponse extends BaseResponse {


    @Expose
    private List<Reproductive> enrollees;

    public FetchReproductiveResponse() {
    }

    public List<Reproductive> getEnrollees() {
        return enrollees;
    }

    public void setEnrollees(List<Reproductive> enrollees) {
        this.enrollees = enrollees;
    }
}
