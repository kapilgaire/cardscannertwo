package com.example.cardscannertwo.data.remote;

import com.example.cardscannertwo.data.request.RequestBuilder;
import com.example.cardscannertwo.data.response.CardDetails;
import com.example.cardscannertwo.data.response.ReportDetails;
import com.example.cardscannertwo.data.response.SiteName;
import com.example.cardscannertwo.data.response.SuccessResponse;
import com.example.cardscannertwo.data.response.TransactionDetails;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NetworkInterface {

    @GET("GetCardDetails/{CARDNO}/{KEY}/{SITENAME}")
    Call<ArrayList<CardDetails>> getCardDetails(@Path("CARDNO")String cardNo
            , @Path("KEY")String key, @Path("SITENAME")String siteName);

    @POST("SaveTransaction")
    Call<SuccessResponse> save(@Body RequestBuilder requestBuilder);


    @GET("GetCardFuelDetails/{CARDNO}/{KEY}/{SITENAME}")
    Call<ArrayList<TransactionDetails>> getCardFuelDetails(@Path("CARDNO")String cardNo
            , @Path("KEY")String key , @Path("SITENAME")String siteName);


    @GET("getDayWiseReport/{SITENAME}/{KEY}")
    Call<ArrayList<ReportDetails>> getDayWiseReport(@Path("SITENAME")String siteName
            , @Path("KEY")String key);




    @GET("GetsiteDetails")
    Call<ArrayList<SiteName>> getsiteDetails();

}
