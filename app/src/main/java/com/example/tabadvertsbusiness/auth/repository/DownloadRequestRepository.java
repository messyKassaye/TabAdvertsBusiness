package com.example.tabadvertsbusiness.auth.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tabadvertsbusiness.auth.model.DownloadRequests;
import com.example.tabadvertsbusiness.auth.model.DownloadedAdverts;
import com.example.tabadvertsbusiness.auth.response.DownloadRequestResponse;
import com.example.tabadvertsbusiness.auth.response.PlacesResponse;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.retrofit.RetrofitRequest;
import com.example.tabadvertsbusiness.auth.retrofit.interfaces.DownloadRequestInterface;
import com.example.tabadvertsbusiness.auth.retrofit.interfaces.DownloadedAdvertsInterface;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadRequestRepository {

    private DownloadRequestInterface downloadedAdvertsInterface;

    public DownloadRequestRepository(){
        downloadedAdvertsInterface = RetrofitRequest.getApiInstance()
                .create(DownloadRequestInterface.class);

    }

    public LiveData<DownloadRequestResponse> index(){
        final MutableLiveData<DownloadRequestResponse> data = new MutableLiveData<>();
        downloadedAdvertsInterface.index().enqueue(new Callback<DownloadRequestResponse>() {
            @Override
            public void onResponse(Call<DownloadRequestResponse> call, Response<DownloadRequestResponse> response) {
                if(response.body()!=null){
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<DownloadRequestResponse> call, Throwable t) {
                data.setValue(null);

            }
        });
        return data;
    }
    public Observable<SuccessResponse> store(DownloadRequests downloadedAdverts){
        return  downloadedAdvertsInterface.store(downloadedAdverts);
    }

    public Call<DownloadRequestResponse> show(String status){
        return downloadedAdvertsInterface.show(status);
    }

    public Call<SuccessResponse> update(DownloadRequests downloadRequests,int id){
        return  downloadedAdvertsInterface.update(downloadRequests,id);
    }
}
