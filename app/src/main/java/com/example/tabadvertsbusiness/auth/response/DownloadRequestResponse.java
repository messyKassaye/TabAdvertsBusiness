package com.example.tabadvertsbusiness.auth.response;

import com.example.tabadvertsbusiness.auth.model.DownloadRequests;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DownloadRequestResponse {
    @SerializedName("data")
    private List<DownloadRequests> data;

    @SerializedName("status")
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<DownloadRequests> getData() {
        return data;
    }

    public void setData(ArrayList<DownloadRequests> data) {
        this.data = data;
    }
}
