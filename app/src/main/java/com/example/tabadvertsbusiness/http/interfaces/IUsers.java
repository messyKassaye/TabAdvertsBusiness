package com.example.tabadvertsbusiness.http.interfaces;

import com.example.tabadvertsbusiness.models.Message;

import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IUsers {
    @GET("{hello}")
    void login(@Path("hello") String path,Callback<Message> callback);
}
