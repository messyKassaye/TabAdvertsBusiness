package com.example.tabadvertsbusiness.auth.roomDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;

import java.util.List;

public interface AdvertViewDAO {

    @Query("select * from advertViews")
    public LiveData<List<AdvertViewsRoom>> index();

    @Insert
    public void store(AdvertViewsRoom advertViewsRoom);

    @Query("select * from advertViews where advertTime=:date")
    public LiveData<List<AdvertViewsRoom>> show(String date);

}
