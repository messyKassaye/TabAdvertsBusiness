package com.example.tabadvertsbusiness.auth.services;

import android.content.Context;
import android.content.SharedPreferences;

public class CommonServices {

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferenceEditor;

    public CommonServices(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("place",Context.MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }

    public void setAdvertlayout(String layout){
        preferenceEditor.putString("advertLayout",layout);
        preferenceEditor.commit();
    }

    public String getAdvertLayout(){
        return preferences.getString("advertLayout",null);
    }


}
