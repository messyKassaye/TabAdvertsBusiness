package com.example.tabadvertsbusiness.auth.retrofit.interfaces;

import com.example.tabadvertsbusiness.auth.model.DownloadRequests;
import com.example.tabadvertsbusiness.auth.response.DownloadRequestResponse;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DownloadRequestInterface {

    @GET("download_request")
    Call<DownloadRequestResponse> index();

    @POST("download_request")
    Observable<SuccessResponse> store(@Body DownloadRequests downloadRequests);

    @GET("download_request/{device_id}/{status}")
    Call<DownloadRequestResponse> show(@Path("device_id") String id,@Path("status") String status);

    @PUT("download_request/{id}")
    Call<SuccessResponse> update(@Body DownloadRequests downloadRequests,@Path("id") int id);
}
