package com.example.tabadvertsbusiness.auth.model;

import java.util.List;

public class Car {
    private int id;
    private String plate_number;
    private List<CarCategory> car_category;
    private List<Advert> adverts;
    private List<Place> working_place;
    private List<Tablet> working_tablet;
    private int place_id;

    public Car() {
    }

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

    public List<Tablet> getWorking_tablet() {
        return working_tablet;
    }

    public void setWorking_tablet(List<Tablet> working_tablet) {
        this.working_tablet = working_tablet;
    }

    public List<Advert> getAdverts() {
        return adverts;
    }

    public void setAdverts(List<Advert> adverts) {
        this.adverts = adverts;
    }

    public List<Place> getWorking_place() {
        return working_place;
    }

    public void setWorking_place(List<Place> working_place) {
        this.working_place = working_place;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }
}
