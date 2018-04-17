package com.psi.index.singapore.sp.DataModel;

import com.google.gson.annotations.SerializedName;

public class PsiTwentyFourHourly {

    @SerializedName("west")
    private int west;
    @SerializedName("national")
    private int national;
    @SerializedName("east")
    private int east;
    @SerializedName("central")
    private int central;
    @SerializedName("south")
    private int south;
    @SerializedName("north")
    private int north;

    public void setWest(int west) {
        this.west = west;
    }

    public int getWest() {
        return west;
    }

    public void setNational(int national) {
        this.national = national;
    }

    public int getNational() {
        return national;
    }

    public void setEast(int east) {
        this.east = east;
    }

    public int getEast() {
        return east;
    }

    public void setCentral(int central) {
        this.central = central;
    }

    public int getCentral() {
        return central;
    }

    public void setNorth(int north) {
        this.north = north;
    }

    public int getNorth() {
        return north;
    }

    public void setSouth(int south) {
        this.south = south;
    }

    public int getSouth() {
        return south;
    }
}

