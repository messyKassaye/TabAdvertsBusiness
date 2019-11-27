package com.example.tabadvertsbusiness.constants;

public class Constants {
    private static final String API_URL="http://10.0.2.2:8000/api/";
    private static final String API_AUTH_URL="http://10.0.2.2:8000/api/auth/";
    public static String getBaseAPiURL(){
        return API_URL;
    }

    public static String getAPIAuthURL(){
        return API_AUTH_URL;
    }
}
