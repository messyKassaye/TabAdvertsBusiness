package com.example.tabadvertsbusiness.auth.roomDB.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "adverts")
public class AdvertRoom {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "advertId")
    private int advertId;

    @ColumnInfo(name = "maximumViewPerDay")
    private int maximumViewPerDay;

    @ColumnInfo(name = "privilege")
    private String privilege;

    @ColumnInfo(name = "fileName")
    private String fileName;

    @ColumnInfo(name = "status",defaultValue = "active")
    private String status;

    public int getId() {
        return id;
    }

    public int getAdvertId() {
        return advertId;
    }

    public void setAdvertId(int advertId) {
        this.advertId = advertId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaximumViewPerDay() {
        return maximumViewPerDay;
    }

    public void setMaximumViewPerDay(int maximumViewPerDay) {
        this.maximumViewPerDay = maximumViewPerDay;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
