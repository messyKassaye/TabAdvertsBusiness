package com.example.tabadvertsbusiness.auth.roomDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tabadvertsbusiness.auth.roomDB.entity.EntertainmentRoom;

import java.util.List;

@Dao
public interface EntertainmentDAO {

    @Query("select * from entertainment")
    public LiveData<List<EntertainmentRoom>> index();

    @Query("select * from entertainment where filePath=:path")
    public EntertainmentRoom show(String path);

    @Insert
    public void store(EntertainmentRoom entertainmentRoom);

    @Update
    public void update(EntertainmentRoom entertainmentRoom);
}
