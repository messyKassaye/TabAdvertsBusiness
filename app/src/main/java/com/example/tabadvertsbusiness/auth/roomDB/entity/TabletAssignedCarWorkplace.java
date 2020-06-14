package com.example.tabadvertsbusiness.auth.roomDB.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tablet_assigned_car_work_place")
public class TabletAssignedCarWorkplace {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("serial_number")
    @Expose
    private String serial_number;

    @SerializedName("place_id")
    @Expose
    private int place_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }
}
