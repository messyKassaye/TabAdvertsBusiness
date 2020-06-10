package com.example.tabadvertsbusiness.auth.roomDB.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.TabletAssignation;
import com.example.tabadvertsbusiness.auth.roomDB.repository.AdvertRoomRepository;
import com.example.tabadvertsbusiness.auth.roomDB.repository.TabletAssignRepository;

import java.util.List;

public class TabletAssignViewModel  extends AndroidViewModel {

    private TabletAssignRepository repository;
    public TabletAssignViewModel(@NonNull Application application) {
        super(application);

        repository = new TabletAssignRepository(application);
    }

    public void store(TabletAssignation advertRoom){
        repository.insert(advertRoom);
    }
    public LiveData<List<TabletAssignation>> show(String id){
        return repository.show(id);
    }


}
