package com.example.tabadvertsbusiness.auth.retrofit.interfaces;

import com.example.tabadvertsbusiness.auth.model.AdvertViewSendObject;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AdvertViewInterface {

    @POST("car_adverts")
    Observable<SuccessResponse> store(@Body AdvertViewSendObject advertView);
}
