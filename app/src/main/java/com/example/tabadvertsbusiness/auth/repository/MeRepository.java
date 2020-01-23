package com.example.tabadvertsbusiness.auth.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tabadvertsbusiness.auth.response.MeResponse;
import com.example.tabadvertsbusiness.auth.retrofit.interfaces.MeInterface;
import com.example.tabadvertsbusiness.auth.retrofit.RetrofitRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeRepository {
    private MeInterface meInterface;

    public MeRepository(){
        meInterface = RetrofitRequest.getApiAuthInstance().create(MeInterface.class);
    }

    public LiveData<MeResponse> me(){
        final MutableLiveData<MeResponse> data = new MutableLiveData<>();
        meInterface.me().enqueue(new Callback<MeResponse>() {
            @Override
            public void onResponse(Call<MeResponse> call, Response<MeResponse> response) {
                if(response.body()!=null){
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MeResponse> call, Throwable t) {
                data.setValue(null);

            }
        });
        return data;
    }
}
