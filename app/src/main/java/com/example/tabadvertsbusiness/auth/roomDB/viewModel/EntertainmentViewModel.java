package com.example.tabadvertsbusiness.auth.roomDB.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tabadvertsbusiness.auth.roomDB.entity.EntertainmentRoom;
import com.example.tabadvertsbusiness.auth.roomDB.repository.EntertainmentRepository;

import java.util.List;

public class EntertainmentViewModel extends AndroidViewModel {

    private EntertainmentRepository repository;
    private LiveData<List<EntertainmentRoom>> advertRoomList;
    public EntertainmentViewModel(@NonNull Application application) {
        super(application);
        repository = new EntertainmentRepository(application);
        advertRoomList = repository.index();
    }

    public LiveData<List<EntertainmentRoom>> index(){
        return advertRoomList;
    }

}
