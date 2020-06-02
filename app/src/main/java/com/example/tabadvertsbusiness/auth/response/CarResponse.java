package com.example.tabadvertsbusiness.auth.response;

import com.example.tabadvertsbusiness.auth.model.Car;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CarResponse {

    @SerializedName("data")
    @Expose
    private List<Car> data;

    public CarResponse() {
    }

    public List<Car> getData() {
        return data;
    }

    public void setData(List<Car> data) {
        this.data = data;
    }
}
