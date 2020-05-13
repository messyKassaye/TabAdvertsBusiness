package com.example.tabadvertsbusiness.auth.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tabadvertsbusiness.auth.helpers.SuperApplication;
import com.example.tabadvertsbusiness.constants.Constants;

public class ApplicationPreferenceCreator {

    public static boolean isDownloadRequestSubmitted() {
        SharedPreferences preferences = SuperApplication.getContext().getSharedPreferences(Constants.getDownloadingPreference(),0);
        return preferences.getBoolean("isRequestSubmitted",false);
    }
    public static void setDownloadingRequestSubmitted(boolean downloading) {
        SharedPreferences preferences = SuperApplication.getContext().getSharedPreferences(Constants.getDownloadingPreference(),0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isRequestSubmitted",downloading);
        editor.commit();
    }

    public static void setDownloadingStatus(Context context,String downloading){
        SharedPreferences preferences = context.getSharedPreferences(Constants.getDownloadingPreference(),0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("downloading",downloading);
        editor.commit();
    }

    public static String isDownloading(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.getDownloadingPreference(),0);
        return preferences.getString("downloading",null);
    }


}
