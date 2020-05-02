package com.example.tabadvertsbusiness.auth.roomDB.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tabadvertsbusiness.auth.roomDB.DAO.AdvertViewDAO;
import com.example.tabadvertsbusiness.auth.roomDB.DAO.EntertainmentDAO;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.EntertainmentRoom;

import java.util.List;

public class EntertainmentRepository {
    private EntertainmentDAO advertViewDAO;
    private LiveData<List<EntertainmentRoom>> advertRooms;
    public EntertainmentRepository(Application application){
        TabletAdsRoomDatabase roomDatabase = TabletAdsRoomDatabase.getDatabase(application);
        advertViewDAO = roomDatabase.getEntertainmentDAO();

        advertRooms = advertViewDAO.index();
    }
    public LiveData<List<EntertainmentRoom>> index(){
        return advertRooms;
    }

}
