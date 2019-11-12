package com.example.tabadvertsbusiness.http.interfaces;

import com.example.tabadvertsbusiness.models.Message;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

public interface GitHubClient {
    @GET("hello")
    Call<ResponseBody> reposForUser();
}
