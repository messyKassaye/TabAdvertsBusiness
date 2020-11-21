package com.example.tabadvertsbusiness.auth.roomDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;

import java.util.List;

import retrofit2.Response;

@Dao
public interface AdvertDAO {

    @Query("select * from adverts")
    public LiveData<List<AdvertRoom>> index();



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void store(AdvertRoom advertRoom);

    @Query("select * from adverts where id=:ids")
    public LiveData<List<AdvertRoom>> show(int ids);

}
