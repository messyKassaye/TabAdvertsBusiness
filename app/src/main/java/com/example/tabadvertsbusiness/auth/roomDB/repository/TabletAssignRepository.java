package com.example.tabadvertsbusiness.auth.roomDB.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tabadvertsbusiness.auth.roomDB.DAO.AdvertDAO;
import com.example.tabadvertsbusiness.auth.roomDB.DAO.TabletAssignDAO;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.TabletAssignation;

import java.util.List;

public class TabletAssignRepository {

    private TabletAssignDAO advertDAO;
    public TabletAssignRepository(Application application){
        TabletAdsRoomDatabase roomDatabase = TabletAdsRoomDatabase.getDatabase(application);
        advertDAO = roomDatabase.getTabletAssignation();

    }

    public void insert(TabletAssignation advertRoom){
        TabletAdsRoomDatabase.dbExecutorService.execute(()->{
            advertDAO.store(advertRoom);
        });
    }

    public LiveData<List<TabletAssignation>> show(String serial_number){
        return advertDAO.show(serial_number);
    }
}
