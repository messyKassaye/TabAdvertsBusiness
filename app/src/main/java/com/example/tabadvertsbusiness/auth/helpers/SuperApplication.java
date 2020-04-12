package com.example.tabadvertsbusiness.auth.helpers;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;

public class SuperApplication extends Application {
private static Application instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    public static Context getContext(){
        return instance.getApplicationContext();
    }
}
