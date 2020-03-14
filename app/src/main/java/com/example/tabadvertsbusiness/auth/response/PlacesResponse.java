package com.example.tabadvertsbusiness.auth.response;

import com.example.tabadvertsbusiness.auth.model.Place;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlacesResponse {
    @SerializedName("data")
    private ArrayList<Place> data;

    public ArrayList<Place> getData() {
        return data;
    }

    public void setData(ArrayList<Place> data) {
        this.data = data;
    }
}
