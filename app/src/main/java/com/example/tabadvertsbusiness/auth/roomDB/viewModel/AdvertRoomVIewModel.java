package com.example.tabadvertsbusiness.auth.roomDB.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.auth.roomDB.repository.AdvertRoomRepository;

import java.util.List;

public class AdvertRoomVIewModel extends AndroidViewModel {
    private AdvertRoomRepository repository;
    private LiveData<List<AdvertRoom>>advertRoomList;

    public AdvertRoomVIewModel(@NonNull Application application) {
        super(application);

        repository = new AdvertRoomRepository(application);
        advertRoomList = repository.index();
    }

    public LiveData<List<AdvertRoom>> index(){
        return advertRoomList;
    }

    public void store(AdvertRoom advertRoom){
        repository.insert(advertRoom);
    }
    public AdvertRoom show(int id){
        return repository.show(id);
    }
}
