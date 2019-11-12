package com.example.tabadvertsbusiness.http;

import com.example.tabadvertsbusiness.constants.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainHttpAdapter {

    public static Retrofit getAdapter(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(Constants.getBaseAPiURL())
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit = builder.client(httpClient.build()).build();

        return retrofit;
    }
}
