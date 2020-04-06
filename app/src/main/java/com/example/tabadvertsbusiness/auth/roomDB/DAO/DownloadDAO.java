package com.example.tabadvertsbusiness.auth.roomDB.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tabadvertsbusiness.auth.roomDB.entity.Download;

import java.util.List;

@Dao
public interface DownloadDAO {
    @Query("select * from downloads")
    public List<Download> index();

    @Insert
    public void store(Download download);

    @Update
    public void update(Download download);

    @Query("select * from downloads where id=:ids")
    public Download show(int ids);

    @Delete
    public void delete(Download download);
}
