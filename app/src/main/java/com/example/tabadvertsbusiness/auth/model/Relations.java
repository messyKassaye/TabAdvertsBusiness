package com.example.tabadvertsbusiness.auth.model;

import com.example.tabadvertsbusiness.models.Role;

import java.util.List;

public class Relations {
    private List<Role> role;
    private List<Car> cars;
    private List<Place> place;
    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }


    public List<Place> getPlace() {
        return place;
    }

    public void setPlace(List<Place> place) {
        this.place = place;
    }
}
