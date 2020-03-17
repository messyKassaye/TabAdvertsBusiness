package com.example.tabadvertsbusiness.auth.response;

import com.example.tabadvertsbusiness.auth.model.Tablet;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TabletResponse {

    @SerializedName("data")
    private List<Tablet> data;

    public List<Tablet> getData() {
        return data;
    }

    public void setData(List<Tablet> data) {
        this.data = data;
    }
}
