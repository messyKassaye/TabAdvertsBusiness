package com.example.tabadvertsbusiness.auth.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tabadvertsbusiness.auth.model.AdvertView;
import com.example.tabadvertsbusiness.auth.model.AdvertViewObject;
import com.example.tabadvertsbusiness.auth.model.AdvertViewSendObject;
import com.example.tabadvertsbusiness.auth.repository.AdvertViewRepository;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.utils.ApiResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AdvertViewRetrofitViewModel extends AndroidViewModel {
    private AdvertViewRepository addressRepository;
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();

    public AdvertViewRetrofitViewModel(@NonNull Application application) {
        super(application);

        addressRepository = new AdvertViewRepository();
    }

    public void store(AdvertViewSendObject advertView) {
        Observable.just(addressRepository.store(advertView)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));
    }

    public void storeView(AdvertViewObject advertViewObject){
        Observable.just(addressRepository.storeView(advertViewObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));
    }

    public MutableLiveData<ApiResponse> storeResponse() {
        return responseLiveData;
    }
}
