package com.example.tabadvertsbusiness.auth.roomDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.tabadvertsbusiness.auth.roomDB.entity.TabletAssignedCarWorkplace;

import java.util.List;

@Dao
public interface TabletAssignedCarWorkPlaceDAO {

    @Query("select * from tablet_assigned_car_work_place where serial_number=:serial_number")
    public LiveData<List<TabletAssignedCarWorkplace>> show(String serial_number);

    @Insert
    public void store(TabletAssignedCarWorkplace tabletAssignation);

}
