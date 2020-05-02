package com.example.tabadvertsbusiness.auth.retrofit.interfaces;

import com.example.tabadvertsbusiness.auth.model.CarWorkPlace;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CarWorkPlaceInterface {

    @POST("car_work_place")
    Observable<SuccessResponse> store(@Body CarWorkPlace carWorkPlace);
}
