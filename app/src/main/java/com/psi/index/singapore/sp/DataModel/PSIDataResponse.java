package com.psi.index.singapore.sp.DataModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PSIDataResponse {
    @SerializedName("region_metadata")
    private List<RegionMetadata> mRegionMetadata;
    @SerializedName("items")
    private List<Items> mItems;
    @SerializedName("api_info")
    private ApiInfo mApiInfo;
    public void setmApiInfo(ApiInfo mApiInfo) {
        this.mApiInfo = mApiInfo;
    }
    public ApiInfo getmApiInfo() {
        return mApiInfo;
    }

    public void setmItems(List<Items> mItems) {
        this.mItems = mItems;
    }

    public List<Items> getmItems() {
        return mItems;
    }

    public void setmRegionMetadata(List<RegionMetadata> mRegionMetadata) {
        this.mRegionMetadata = mRegionMetadata;
    }

    public List<RegionMetadata> getmRegionMetadata() {
        return mRegionMetadata;
    }
}
