package com.example.tabadvertsbusiness.auth.roomDB.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tabadvertsbusiness.auth.roomDB.DAO.AdvertDAO;
import com.example.tabadvertsbusiness.auth.roomDB.DAO.AdvertViewDAO;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;

import java.util.List;

public class AdvertViewRepository {
    private AdvertViewDAO advertViewDAO;
    private LiveData<List<AdvertViewsRoom>> advertRooms;
    public AdvertViewRepository(Application application){
        TabletAdsRoomDatabase roomDatabase = TabletAdsRoomDatabase.getDatabase(application);
        advertViewDAO = roomDatabase.getAdvertViewDAO();

        advertRooms = advertViewDAO.index();
    }
    public LiveData<List<AdvertViewsRoom>> index(){
        return advertRooms;
    }

    public void insert(AdvertViewsRoom advertRoom){
        TabletAdsRoomDatabase.dbExecutorService.execute(()->{
            advertViewDAO.store(advertRoom);
        });
    }

    public void update(AdvertViewsRoom advertViewsRoom){
        advertViewDAO.update(advertViewsRoom);
    }

    public LiveData<List<AdvertViewsRoom>> showCompanyAdvert(int id){
        return advertViewDAO.showCompanyAdvertView(id);
    }

    public LiveData<List<AdvertViewsRoom>> showUnsendAdvert(boolean status){
        return  advertViewDAO.showUnsendAdvert(status);
    }

    public LiveData<List<AdvertViewsRoom>> todayAdvert(String date, int advertId){
        return advertViewDAO.todayAdvertView(date,advertId);
    }
}
