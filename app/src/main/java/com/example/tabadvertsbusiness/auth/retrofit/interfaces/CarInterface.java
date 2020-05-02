package com.example.tabadvertsbusiness.auth.retrofit.interfaces;

import com.example.tabadvertsbusiness.auth.model.Car;
import com.example.tabadvertsbusiness.auth.model.CarStore;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CarInterface {
    @POST("cars")
    Observable<SuccessResponse> store(@Body CarStore carStore);

    @FormUrlEncoded
    @PATCH("cars/{id}")
    Observable<SuccessResponse> update(@Path("id") int id,
                                       @Field("plate_number")String plate_number,
                                       @Field("place_id") int place_id);
}
