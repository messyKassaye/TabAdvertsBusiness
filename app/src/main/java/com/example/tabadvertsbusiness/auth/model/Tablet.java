package com.example.tabadvertsbusiness.auth.model;

import java.util.List;

public class Tablet {
    private int id;
    private String serial_number;
    private Car cars;
    private int car_id;
    private int user_id;
    private Attribute user;
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

    public Car getCars() {
        return cars;
    }

    public void setCars(Car cars) {
        this.cars = cars;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Attribute getUser() {
        return user;
    }

    public void setUser(Attribute user) {
        this.user = user;
    }
}
