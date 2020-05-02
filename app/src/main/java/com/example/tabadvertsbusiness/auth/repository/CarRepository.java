package com.example.tabadvertsbusiness.auth.repository;

import com.example.tabadvertsbusiness.auth.model.Car;
import com.example.tabadvertsbusiness.auth.model.CarStore;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.retrofit.RetrofitRequest;
import com.example.tabadvertsbusiness.auth.retrofit.interfaces.CarInterface;

import io.reactivex.Observable;

public class CarRepository {
    private CarInterface carInterface;

    public CarRepository(){
        carInterface = RetrofitRequest.getApiInstance().create(CarInterface.class);
    }

    public Observable<SuccessResponse> store(CarStore carStore){
        return carInterface.store(carStore);
    }

    public Observable<SuccessResponse> update(Car car,int id){
        return carInterface.update(id,car.getPlate_number(),car.getPlace_id());
    }
}
