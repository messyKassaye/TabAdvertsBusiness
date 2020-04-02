package com.example.tabadvertsbusiness.auth.roomDB.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "todayAdvert")
public class TodaysAdvert {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "advertId")
    private int advertId;

    private Date date;

    @ColumnInfo(name = "totalAdvert",defaultValue = "0")
    private int totalAdvert;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdvertId() {
        return advertId;
    }

    public void setAdvertId(int advertId) {
        this.advertId = advertId;
    }

    public int getTotalAdvert() {
        return totalAdvert;
    }

    public void setTotalAdvert(int totalAdvert) {
        this.totalAdvert = totalAdvert;
    }
}
