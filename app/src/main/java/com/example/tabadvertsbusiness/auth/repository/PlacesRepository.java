package com.example.tabadvertsbusiness.auth.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tabadvertsbusiness.auth.response.MeResponse;
import com.example.tabadvertsbusiness.auth.response.PlacesResponse;
import com.example.tabadvertsbusiness.auth.retrofit.RetrofitRequest;
import com.example.tabadvertsbusiness.auth.retrofit.interfaces.PlacesInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesRepository {
    private PlacesInterface placesInterface;

    public PlacesRepository(){
        placesInterface = RetrofitRequest.getApiInstance().create(PlacesInterface.class);
    }

    public LiveData<PlacesResponse> index(){
        final MutableLiveData<PlacesResponse> data = new MutableLiveData<>();
        placesInterface.index().enqueue(new Callback<PlacesResponse>() {
            @Override
            public void onResponse(Call<PlacesResponse> call, Response<PlacesResponse> response) {
                if(response.body()!=null){
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<PlacesResponse> call, Throwable t) {
                data.setValue(null);

            }
        });
        return data;
    }
}
