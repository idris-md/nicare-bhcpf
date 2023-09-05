package com.nicare.ves.persistence.remote.apimodels.requestmodel;

import com.google.gson.annotations.Expose;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;

import java.util.List;

public class VulnerableRequest {
    @Expose
    private List<Vulnerable> vulnerables;

    public VulnerableRequest() {
    }

    public VulnerableRequest( List<Vulnerable> vulnerables) {
        this.vulnerables = vulnerables;
    }

    public List<Vulnerable> getVulnerables() {
        return vulnerables;
    }

    public void setVulnerables(List<Vulnerable> vulnerables) {
        this.vulnerables = vulnerables;
    }

}
