package com.psi.index.singapore.sp.DataModel;

import com.google.gson.annotations.SerializedName;

public class Items {
    @SerializedName("timestamp")
   private String timestamp ;

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @SerializedName("update_timestamp")
    private String update_timestamp ;

    public void setUpdate_timestamp(String update_timestamp) {
        this.update_timestamp = update_timestamp;
    }

    public String getUpdate_timestamp() {
        return update_timestamp;
    }

    @SerializedName("readings")
    private Readings mReading ;
    public void setmReading(Readings mReading) {
        this.mReading = mReading;
    }

    public Readings getmReading() {
        return mReading;
    }

}
