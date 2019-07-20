package com.example.cardscannertwo.data.remote;

import com.example.cardscannertwo.data.response.CardDetails;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NetworkInterface {

    @GET("GetCardDetails/{CARDNO}/{KEY}")
    Call<ArrayList<CardDetails>> getCardDetails(@Path("CARDNO")String cardNo
            , @Path("KEY")String key);

}
