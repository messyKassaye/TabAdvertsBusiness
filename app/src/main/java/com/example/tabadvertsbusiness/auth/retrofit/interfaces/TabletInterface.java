package com.example.tabadvertsbusiness.auth.retrofit.interfaces;

import com.example.tabadvertsbusiness.auth.model.Tablet;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.response.TabletResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TabletInterface {
    @POST("tablets")
    Observable<SuccessResponse> store(@Body Tablet tablet);

    @GET("tablets/{id}")
    Call<TabletResponse> show(@Path("id") String id);
}
