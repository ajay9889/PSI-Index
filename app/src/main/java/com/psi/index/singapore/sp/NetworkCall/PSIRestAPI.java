package com.psi.index.singapore.sp.NetworkCall;

import com.psi.index.singapore.sp.DataModel.PSIDataResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface PSIRestAPI {

    @Headers({
            "Accept: application/json",
            "User-Agent: Retrofit-PSI-APP"
    })
    @GET("psi")
    Call<PSIDataResponse> getPSIData( @Query("date_time") String date_time,
                                           @Query("date") String date);
}
