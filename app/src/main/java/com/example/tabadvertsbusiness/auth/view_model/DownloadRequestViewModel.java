package com.example.tabadvertsbusiness.auth.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tabadvertsbusiness.auth.model.DownloadRequests;
import com.example.tabadvertsbusiness.auth.model.Tablet;
import com.example.tabadvertsbusiness.auth.repository.DownloadRequestRepository;
import com.example.tabadvertsbusiness.auth.repository.TabletRepository;
import com.example.tabadvertsbusiness.auth.response.DownloadRequestResponse;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.utils.ApiResponse;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

public class DownloadRequestViewModel extends AndroidViewModel {

    private DownloadRequestRepository tabletRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();
    public DownloadRequestViewModel(@NonNull Application application) {
        super(application);

        tabletRepository = new DownloadRequestRepository();

    }

    public MutableLiveData<ApiResponse> storeResponse() {
        return responseLiveData;
    }

    /*
     * method to call normal login api with $(mobileNumber + password)
     * */
    public LiveData<DownloadRequestResponse> index(){
       return tabletRepository.index();
    }
    public void store(DownloadRequests requests) {

        Observable.just(tabletRepository.store(requests)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));

    }

    public Call<DownloadRequestResponse> show(String status){
        return  tabletRepository.show(status);
    }

    public Call<SuccessResponse> update(DownloadRequests downloadRequests,int id){
        return tabletRepository.update(downloadRequests,id);
    }

}
