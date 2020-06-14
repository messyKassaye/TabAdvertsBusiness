package com.example.tabadvertsbusiness.auth.roomDB.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tabadvertsbusiness.auth.roomDB.DAO.TabletAssignDAO;
import com.example.tabadvertsbusiness.auth.roomDB.DAO.TabletAssignedCarWorkPlaceDAO;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.TabletAssignation;
import com.example.tabadvertsbusiness.auth.roomDB.entity.TabletAssignedCarWorkplace;

import java.util.List;

public class TabletAssignedCarRepository {

    private TabletAssignedCarWorkPlaceDAO advertDAO;
    public TabletAssignedCarRepository(Application application){
        TabletAdsRoomDatabase roomDatabase = TabletAdsRoomDatabase.getDatabase(application);
        advertDAO = roomDatabase.getTabletCarWorkPlaceDAO();

    }

    public void insert(TabletAssignedCarWorkplace advertRoom){
        TabletAdsRoomDatabase.dbExecutorService.execute(()->{
            advertDAO.store(advertRoom);
        });
    }

    public LiveData<List<TabletAssignedCarWorkplace>> show(String serial_number){
        return advertDAO.show(serial_number);
    }
}
