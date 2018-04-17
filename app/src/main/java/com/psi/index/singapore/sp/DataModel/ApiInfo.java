package com.psi.index.singapore.sp.DataModel;

import com.google.gson.annotations.SerializedName;

public class ApiInfo {
    @SerializedName("status")
    private String status ;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
