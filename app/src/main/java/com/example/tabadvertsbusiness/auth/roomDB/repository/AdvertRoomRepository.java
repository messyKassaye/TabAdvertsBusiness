package com.example.tabadvertsbusiness.auth.roomDB.repository;


import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tabadvertsbusiness.auth.roomDB.DAO.AdvertDAO;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;

import java.util.List;

public class AdvertRoomRepository {
    private AdvertDAO advertDAO;
    private LiveData<List<AdvertRoom>> advertRooms;
   public AdvertRoomRepository(Application application){
        TabletAdsRoomDatabase roomDatabase = TabletAdsRoomDatabase.getDatabase(application);
        advertDAO = roomDatabase.getAdvertDAO();

        advertRooms = advertDAO.index();
    }
    public LiveData<List<AdvertRoom>> index(){
    return advertRooms;
    }

    public void insert(AdvertRoom advertRoom){
        TabletAdsRoomDatabase.dbExecutorService.execute(()->{
            advertDAO.store(advertRoom);
        });
    }

    public LiveData<List<AdvertRoom>> show(int id){
       return advertDAO.show(id);
    }

}
