package com.example.tabadvertsbusiness.auth.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tabadvertsbusiness.auth.repository.PlacesRepository;
import com.example.tabadvertsbusiness.auth.response.PlacesResponse;

public class PlacesViewModel extends AndroidViewModel {
    private PlacesRepository placesRepository;
    private LiveData<PlacesResponse> responseLiveData;
    public PlacesViewModel(@NonNull Application application) {
        super(application);

        placesRepository = new PlacesRepository();
        responseLiveData = placesRepository.index();
    }

    public LiveData<PlacesResponse> index(){
        return  responseLiveData;
    }
}
