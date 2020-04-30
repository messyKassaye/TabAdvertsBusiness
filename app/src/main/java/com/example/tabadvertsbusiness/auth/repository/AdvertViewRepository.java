package com.example.tabadvertsbusiness.auth.repository;

import com.example.tabadvertsbusiness.auth.model.AdvertView;
import com.example.tabadvertsbusiness.auth.model.AdvertViewSendObject;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.retrofit.RetrofitRequest;
import com.example.tabadvertsbusiness.auth.retrofit.interfaces.AdvertViewInterface;

import io.reactivex.Observable;

public class AdvertViewRepository {

    private AdvertViewInterface addressInterface;

    public AdvertViewRepository() {
        addressInterface = RetrofitRequest.getApiInstance().create(AdvertViewInterface.class);
    }

    public Observable<SuccessResponse> store(AdvertViewSendObject advertView){
        return addressInterface.store(advertView);
    }
}
