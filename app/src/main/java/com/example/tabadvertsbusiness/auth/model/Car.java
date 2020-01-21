package com.example.tabadvertsbusiness.auth.model;

import java.util.List;

public class Car {
    private int id;
    private String plate_number;
    private List<CarCategory> car_category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public List<CarCategory> getCar_category() {
        return car_category;
    }

    public void setCar_category(List<CarCategory> car_category) {
        this.car_category = car_category;
    }
}
