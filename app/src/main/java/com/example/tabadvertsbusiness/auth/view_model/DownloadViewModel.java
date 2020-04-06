package com.example.tabadvertsbusiness.auth.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tabadvertsbusiness.auth.model.DownloadedAdverts;
import com.example.tabadvertsbusiness.auth.model.Tablet;
import com.example.tabadvertsbusiness.auth.repository.DownloadRespository;
import com.example.tabadvertsbusiness.auth.repository.TabletRepository;
import com.example.tabadvertsbusiness.auth.utils.ApiResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DownloadViewModel extends AndroidViewModel {

    private DownloadRespository downloadRespository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();
    public DownloadViewModel(@NonNull Application application) {
        super(application);

        downloadRespository = new DownloadRespository();
    }

    public MutableLiveData<ApiResponse> storeResponse() {
        return responseLiveData;
    }

    /*
     * method to call normal login api with $(mobileNumber + password)
     * */
    public void store(DownloadedAdverts downloadedAdverts) {

        Observable.just(downloadRespository.store(downloadedAdverts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));

    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
