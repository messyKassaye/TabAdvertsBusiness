package com.example.tabadvertsbusiness.auth.repository;

import com.example.tabadvertsbusiness.auth.model.DownloadedAdverts;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.retrofit.RetrofitRequest;
import com.example.tabadvertsbusiness.auth.retrofit.interfaces.DownloadedAdvertsInterface;

import retrofit2.Call;

public class DownloadedAdvertsRespository {

    private DownloadedAdvertsInterface downloadedAdvertsInterface;

    public DownloadedAdvertsRespository(){
        downloadedAdvertsInterface = RetrofitRequest.getApiInstance()
                .create(DownloadedAdvertsInterface.class);

    }
    public Call<SuccessResponse> store(DownloadedAdverts downloadedAdverts){
        return  downloadedAdvertsInterface.store(downloadedAdverts);
    }
}
