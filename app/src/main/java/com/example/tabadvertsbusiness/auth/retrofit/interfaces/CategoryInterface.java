package com.example.tabadvertsbusiness.auth.retrofit.interfaces;

import androidx.lifecycle.LiveData;

import com.example.tabadvertsbusiness.auth.response.CategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryInterface {

    @GET("categories")
    Call<CategoryResponse> get();
}
