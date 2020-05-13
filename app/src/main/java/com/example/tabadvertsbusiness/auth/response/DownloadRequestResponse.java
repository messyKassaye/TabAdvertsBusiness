package com.example.tabadvertsbusiness.auth.response;

import com.example.tabadvertsbusiness.auth.model.DownloadRequests;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DownloadRequestResponse {
    @SerializedName("data")
    private ArrayList<DownloadRequests> data;

    public ArrayList<DownloadRequests> getData() {
        return data;
    }

    public void setData(ArrayList<DownloadRequests> data) {
        this.data = data;
    }
}
