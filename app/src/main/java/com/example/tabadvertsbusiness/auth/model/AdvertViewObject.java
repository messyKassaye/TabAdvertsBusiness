package com.example.tabadvertsbusiness.auth.model;

public class AdvertViewObject {
    private int index;
    private int view_id;
    private int car_id;
    private int advert_id;
    private String advert_time;
    private int number_of_viewers;
    private String picture;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getView_id() {
        return view_id;
    }

    public void setView_id(int view_id) {
        this.view_id = view_id;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public int getAdvert_id() {
        return advert_id;
    }

    public void setAdvert_id(int advert_id) {
        this.advert_id = advert_id;
    }

    public String getAdvert_time() {
        return advert_time;
    }

    public void setAdvert_time(String advert_time) {
        this.advert_time = advert_time;
    }

    public int getNumber_of_viewers() {
        return number_of_viewers;
    }

    public void setNumber_of_viewers(int number_of_viewers) {
        this.number_of_viewers = number_of_viewers;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
