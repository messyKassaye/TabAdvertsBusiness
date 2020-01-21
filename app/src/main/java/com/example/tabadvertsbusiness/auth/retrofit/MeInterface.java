package com.example.tabadvertsbusiness.auth.retrofit;

import com.example.tabadvertsbusiness.auth.response.MeResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MeInterface {
    @GET("me")
    Call<MeResponse> me();
}
