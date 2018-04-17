package com.psi.index.singapore.sp.DataModel;

import com.google.gson.annotations.SerializedName;

public class RegionMetadata {
    @SerializedName("label_location")
    private Location mLocation;

    @SerializedName("name")
    private String name;

    public Location getmLocation() {
        return mLocation;
    }

    public void setmLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
