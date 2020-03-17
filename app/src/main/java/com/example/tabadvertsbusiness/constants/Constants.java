package com.example.tabadvertsbusiness.constants;

public class Constants {
    //10.0.2.2
    private static final String TOKEN_PREFENCE = "token";
    private static final String DOWNLOAD_PATH = "http://10.5.59.226:8000/";
    private static final String API_URL="http://10.5.59.226:8000/api/";
    private static final String API_AUTH_URL="http://10.5.59.226:8000/api/auth/";
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
}
