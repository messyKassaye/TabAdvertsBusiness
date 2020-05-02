package com.example.tabadvertsbusiness.auth.repository;

import com.example.tabadvertsbusiness.auth.model.Car;
import com.example.tabadvertsbusiness.auth.model.CarStore;
import com.example.tabadvertsbusiness.auth.model.CarWorkPlace;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.retrofit.RetrofitRequest;
import com.example.tabadvertsbusiness.auth.retrofit.interfaces.CarInterface;
import com.example.tabadvertsbusiness.auth.retrofit.interfaces.CarWorkPlaceInterface;

import io.reactivex.Observable;

public class CarWorkPlaceRepository {

    private CarWorkPlaceInterface carInterface;

    public CarWorkPlaceRepository(){
        carInterface = RetrofitRequest.getApiInstance().create(CarWorkPlaceInterface.class);
    }

    public Observable<SuccessResponse> store(CarWorkPlace carStore){
        return carInterface.store(carStore);
    }

}
