package com.example.tabadvertsbusiness.auth.roomDB.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tabadvertsbusiness.auth.roomDB.entity.Advert;

import java.util.List;

@Dao
public interface AdvertDAO {

    @Query("select * from adverts")
    public List<Advert> index();

   /* @Insert()
    public void store(Advert advert);

    public void update(int id);

    public void show(int id);


    public void delete(int id);*/
}
