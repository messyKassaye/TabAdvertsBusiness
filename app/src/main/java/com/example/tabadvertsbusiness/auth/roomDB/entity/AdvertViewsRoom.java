package com.example.tabadvertsbusiness.auth.roomDB.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "advertViews")
public class AdvertViewsRoom {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "advertId")
    private int advertId;

    @ColumnInfo(name = "advertTime")
    private String advertTime;

    @ColumnInfo(name = "numberOfViewers")
    private int numberOfViewers;

    @ColumnInfo(name = "picture")
    private String picture;

    @ColumnInfo(name = "isSend",defaultValue = "false")
    private boolean send;

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

    public String getAdvertTime() {
        return advertTime;
    }

    public void setAdvertTime(String advertTime) {
        this.advertTime = advertTime;
    }

    public int getNumberOfViewers() {
        return numberOfViewers;
    }

    public void setNumberOfViewers(int numberOfViewers) {
        this.numberOfViewers = numberOfViewers;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }
}
