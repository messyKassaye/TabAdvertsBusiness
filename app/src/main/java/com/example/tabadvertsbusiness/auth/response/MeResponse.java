package com.example.tabadvertsbusiness.auth.response;

import com.example.tabadvertsbusiness.auth.model.MeData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeResponse {

    @SerializedName("data")
    @Expose
   private MeData data;

    public MeData getData() {
        return data;
    }

    public void setData(MeData data) {
        this.data = data;
    }
}
