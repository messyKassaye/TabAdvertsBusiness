package com.example.tabadvertsbusiness.auth.model;

import com.example.tabadvertsbusiness.models.Role;

import java.util.List;

public class Relations {
    private List<Role> role;
    private List<Car> cars;

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
}
