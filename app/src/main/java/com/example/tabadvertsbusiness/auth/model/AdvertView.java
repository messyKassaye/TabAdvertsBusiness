package com.example.tabadvertsbusiness.auth.model;

public class AdvertView {
    private int car_id;
    private int advert_id;
    private String advert_time;
    private int number_of_viewers;
    private String picture;

    public AdvertView() {
    }

    public AdvertView(int car_id, int advert_id, String advert_time, int number_of_viewers, String picture) {
        this.car_id = car_id;
        this.advert_id = advert_id;
        this.advert_time = advert_time;
        this.number_of_viewers = number_of_viewers;
        this.picture = picture;
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
