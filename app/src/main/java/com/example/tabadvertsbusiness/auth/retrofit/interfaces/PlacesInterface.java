package com.example.tabadvertsbusiness.auth.retrofit.interfaces;

import com.example.tabadvertsbusiness.auth.response.PlacesResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PlacesInterface {

    @GET("places")
    Call<PlacesResponse> index();
}
