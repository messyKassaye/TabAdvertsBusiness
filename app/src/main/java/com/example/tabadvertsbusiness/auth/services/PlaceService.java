package com.example.tabadvertsbusiness.auth.services;

import android.content.Context;
import android.content.SharedPreferences;

public class PlaceService {
    private int placeId = 0;
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferenceEditor;
    public PlaceService(Context context){
        this.context = context;

        preferences = context.getSharedPreferences("place",Context.MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }

    public int getPlaceId() {
        return preferences.getInt("placeId",0);
    }

    public void setPlaceId(int placeId) {
        preferenceEditor.putInt("placeId",placeId);
        preferenceEditor.commit();
    }

    public void setType(int type){
        preferenceEditor.putInt("types",type);
        preferenceEditor.commit();

    }

    public int getType() {
        return preferences.getInt("types",0);
    }

    public void setCarId(int id){
        preferenceEditor.putInt("carId",id);
        preferenceEditor.commit();
    }

    public int getCarId(){
        return preferences.getInt("carId",0);
    }

}
