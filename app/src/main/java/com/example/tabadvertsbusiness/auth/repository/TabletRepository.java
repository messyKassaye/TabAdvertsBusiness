package com.example.tabadvertsbusiness.auth.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tabadvertsbusiness.auth.model.Tablet;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.retrofit.RetrofitRequest;
import com.example.tabadvertsbusiness.auth.retrofit.interfaces.TabletInterface;
import com.google.gson.JsonElement;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabletRepository {
    private TabletInterface tabletInterface;

    public TabletRepository() {
        tabletInterface = RetrofitRequest.getApiInstance().create(TabletInterface.class);
    }

    public Observable<SuccessResponse> store(Tablet tablet){
        return tabletInterface.store(tablet);
    }
}
