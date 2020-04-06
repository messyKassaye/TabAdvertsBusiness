package com.example.tabadvertsbusiness.auth.retrofit.interfaces;

import com.example.tabadvertsbusiness.auth.model.DownloadedAdverts;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DownloadInterface {

    @POST("file_handler")
    Observable<SuccessResponse> store(@Body DownloadedAdverts downloadedAdverts);

}
