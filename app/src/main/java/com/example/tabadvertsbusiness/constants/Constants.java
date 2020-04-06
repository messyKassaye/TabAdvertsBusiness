package com.example.tabadvertsbusiness.constants;

import android.content.Context;

import java.io.File;

public class Constants {
    //10.0.2.2
    private static final String TOKEN_PREFENCE = "token";
    private static final String DOWNLOAD_PATH = "http://10.0.2.2:8000/";
    private static final String API_URL="http://10.0.2.2:8000/api/";
    private static final String API_AUTH_URL="http://10.0.2.2:8000/api/auth/";
    private static final String DATA_PATH = "advertData";
    private static final String DB_NAME = "TabletAdsDB";
    public static String getBaseAPiURL(){
        return API_URL;
    }

    public static String getAPIAuthURL(){
        return API_AUTH_URL;
    }

    public static String getTokenPrefence(){
        return  TOKEN_PREFENCE;
    }

    public static String getDownloadPath() {
        return DOWNLOAD_PATH;
    }

    public static String getDataPath(){
        return DATA_PATH;
    }

    public static String getDbName() {
        return DB_NAME;
    }

    public static boolean doesDatabaseExist(Context context) {
        File dbFile = context.getDatabasePath(DB_NAME);
        return dbFile.exists();
    }
}
