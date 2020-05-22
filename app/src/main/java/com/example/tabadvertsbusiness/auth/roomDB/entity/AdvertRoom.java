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

    @ColumnInfo(name = "company_name")
    private String company_name;

    @ColumnInfo(name = "product_name")
    private String product_name;

    @ColumnInfo(name = "maximumViewPerDay")
    private int maximumViewPerDay;

    @ColumnInfo(name = "privilege")
    private String privilege;

    @ColumnInfo(name = "fileName")
    private String fileName;

    @ColumnInfo(name = "filePath")
    private String filePath;

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

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
