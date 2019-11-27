package com.example.tabadvertsbusiness.http.interfaces;

import com.example.tabadvertsbusiness.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> login(@Field("email")String email, @Field("password")String password);
}
