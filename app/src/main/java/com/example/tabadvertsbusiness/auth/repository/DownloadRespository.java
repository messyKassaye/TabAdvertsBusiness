package com.example.tabadvertsbusiness.auth.repository;

import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.retrofit.RetrofitRequest;
import com.example.tabadvertsbusiness.auth.retrofit.interfaces.DownloadInterface;
import com.example.tabadvertsbusiness.auth.retrofit.interfaces.MeInterface;

import io.reactivex.Observable;

public class DownloadRespository  {
    private DownloadInterface downloadInterface;

    public DownloadRespository(){
        downloadInterface = RetrofitRequest.getApiInstance().create(DownloadInterface.class);
    }

    public Observable<SuccessResponse> store(){
        return downloadInterface.store();
    }
}
