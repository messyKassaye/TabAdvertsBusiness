package com.example.tabadvertsbusiness.auth.roomDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;

import java.util.List;

@Dao
public interface AdvertViewDAO {

    @Query("select * from advertViews")
    public LiveData<List<AdvertViewsRoom>> index();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void store(AdvertViewsRoom advertViewsRoom);

    @Query("select * from advertViews where advertTime=:date")
    public LiveData<List<AdvertViewsRoom>> show(String date);

    @Update
    public void update(AdvertViewsRoom... advertViewsRoom);

}
