package com.example.tabadvertsbusiness.auth.view_model;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.example.tabadvertsbusiness.auth.repository.MeRepository;
import com.example.tabadvertsbusiness.auth.response.MeResponse;

public class MeViewModel extends AndroidViewModel {
   private MeRepository repository;
   private LiveData<MeResponse> response;
    public MeViewModel(@NonNull Application application) {
        super(application);

        repository = new MeRepository();
        response = repository.me();
    }

    public LiveData<MeResponse> me(){
        return response;
    }
}
