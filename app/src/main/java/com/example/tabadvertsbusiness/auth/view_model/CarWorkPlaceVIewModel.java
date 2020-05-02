package com.example.tabadvertsbusiness.auth.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tabadvertsbusiness.auth.model.CarStore;
import com.example.tabadvertsbusiness.auth.model.CarWorkPlace;
import com.example.tabadvertsbusiness.auth.repository.CarRepository;
import com.example.tabadvertsbusiness.auth.repository.CarWorkPlaceRepository;
import com.example.tabadvertsbusiness.auth.utils.ApiResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CarWorkPlaceVIewModel extends AndroidViewModel {

    private CarWorkPlaceRepository carRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();
    public CarWorkPlaceVIewModel(@NonNull Application application) {
        super(application);

        carRepository = new CarWorkPlaceRepository();

    }

    public MutableLiveData<ApiResponse> storeResponse() {
        return responseLiveData;
    }

    /*
     * method to call normal login api with $(mobileNumber + password)
     * */
    public void store(CarWorkPlace carStore) {

        Observable.just(carRepository.store(carStore)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));

    }
}
