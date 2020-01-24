package com.example.tabadvertsbusiness.auth.response;

import com.example.tabadvertsbusiness.auth.model.Category;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryResponse {
    @SerializedName("data")
    private ArrayList<Category> data;

    public ArrayList<Category> getData() {
        return data;
    }

    public void setData(ArrayList<Category> data) {
        this.data = data;
    }
}
