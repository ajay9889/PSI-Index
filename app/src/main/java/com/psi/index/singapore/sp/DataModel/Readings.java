package com.psi.index.singapore.sp.DataModel;


import com.google.gson.annotations.SerializedName;

public class Readings {
    @SerializedName("psi_twenty_four_hourly")
    private PsiTwentyFourHourly gPsiTwentyFourHourly;

    public void setgPsiTwentyFourHourly(PsiTwentyFourHourly gPsiTwentyFourHourly) {
        this.gPsiTwentyFourHourly = gPsiTwentyFourHourly;
    }

    public PsiTwentyFourHourly getgPsiTwentyFourHourly() {
        return gPsiTwentyFourHourly;
    }
}
