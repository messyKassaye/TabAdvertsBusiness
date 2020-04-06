package com.example.tabadvertsbusiness.auth.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.tabadvertsbusiness.auth.model.DownloadedAdverts;
import com.example.tabadvertsbusiness.auth.repository.DownloadedAdvertsRespository;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;

import retrofit2.Call;

public class DownloadedaAdvertsViewModel extends AndroidViewModel {
    private DownloadedAdvertsRespository downloadedAdvertsRespository;
    public DownloadedaAdvertsViewModel(@NonNull Application application) {
        super(application);

        downloadedAdvertsRespository = new DownloadedAdvertsRespository();
    }

    public Call<SuccessResponse> store(DownloadedAdverts downloadedAdverts){
        return downloadedAdvertsRespository.store(downloadedAdverts);
    }
}
