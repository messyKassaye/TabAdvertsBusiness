package com.example.tabadvertsbusiness.auth.roomDB.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tablet_assignation")
public class TabletAssignation {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("user_id")
    @Expose
    private int user_id;

    @SerializedName("serial_number")
    private String serial_number;

    @SerializedName("car_id")
    private int car_id;

    public int getUser_id() {
        return user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }
}
