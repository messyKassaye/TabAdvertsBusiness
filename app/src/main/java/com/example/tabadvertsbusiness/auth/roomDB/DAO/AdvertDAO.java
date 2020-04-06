package com.example.tabadvertsbusiness.auth.roomDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;

import java.util.List;

@Dao
public interface AdvertDAO {

    @Query("select * from adverts")
    public LiveData<List<AdvertRoom>> index();

    @Insert
    public void store(AdvertRoom advertRoom);

    /*
    public void update(int id);

    public void show(int id);


    public void delete(int id);*/
}
