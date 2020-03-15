package com.example.tabadvertsbusiness.auth.retrofit.interfaces;

import com.example.tabadvertsbusiness.auth.model.Place;
import com.example.tabadvertsbusiness.auth.response.PlacesResponse;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PlacesInterface {

    @GET("places")
    Call<PlacesResponse> index();

    @POST("places")
    Call<SuccessResponse> store(@Body Place place);
}
