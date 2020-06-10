package com.example.tabadvertsbusiness.auth.roomDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tabadvertsbusiness.auth.roomDB.entity.TabletAssignation;

import java.util.List;

@Dao
public interface TabletAssignDAO {

    @Query("select * from tablet_assignation where serial_number=:serial_number")
    public LiveData<List<TabletAssignation>> show(String serial_number);

    @Insert
    public void store(TabletAssignation tabletAssignation);
}
