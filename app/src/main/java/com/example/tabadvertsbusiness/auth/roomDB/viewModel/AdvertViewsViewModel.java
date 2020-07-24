package com.example.tabadvertsbusiness.auth.roomDB.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
import com.example.tabadvertsbusiness.auth.roomDB.repository.AdvertViewRepository;

import java.util.List;

public class AdvertViewsViewModel extends AndroidViewModel {

    private AdvertViewRepository repository;
    private LiveData<List<AdvertViewsRoom>> advertRoomList;
    public AdvertViewsViewModel(@NonNull Application application) {
        super(application);
        repository = new AdvertViewRepository(application);
        advertRoomList = repository.index();
    }

    public LiveData<List<AdvertViewsRoom>> index(){
        return advertRoomList;
    }

    public void store(AdvertViewsRoom advertRoom){
        repository.insert(advertRoom);
    }
    public void update(AdvertViewsRoom advertViewsRoom){
        repository.update(advertViewsRoom);
    }

    public LiveData<List<AdvertViewsRoom>> showCompany(int id){
        return  repository.showCompanyAdvert(id);
    }

    public LiveData<List<AdvertViewsRoom>> showUnsendAdvert(boolean status){
        return  repository.showUnsendAdvert(status);
    }

    public LiveData<List<AdvertViewsRoom>> todayAdvert(String date, int advertId){
        return  repository.todayAdvert(date,advertId);
    }
}
