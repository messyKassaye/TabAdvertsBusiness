package com.example.tabadvertsbusiness.auth.roomDB.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.tabadvertsbusiness.auth.roomDB.entity.TabletAssignedCarWorkplace;
import com.example.tabadvertsbusiness.auth.roomDB.repository.TabletAssignedCarRepository;
import java.util.List;

public class TabletAssignedCarWorkPlaceViewModel extends AndroidViewModel {
    private TabletAssignedCarRepository repository;
    public TabletAssignedCarWorkPlaceViewModel(@NonNull Application application) {
        super(application);

        repository = new TabletAssignedCarRepository(application);

    }

    public void store(TabletAssignedCarWorkplace advertRoom){
        repository.insert(advertRoom);
    }
    public LiveData<List<TabletAssignedCarWorkplace>> show(String id){
        return repository.show(id);
    }
}
