package com.example.tabadvertsbusiness.constants;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.home.models.CallPhone;
import com.example.tabadvertsbusiness.home.models.Contact;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Constants {
    //10.0.2.2:8000
    //http://dev.tesfabunna.com
    private static final String TOKEN_PREFENCE = "token";
    private static final String DOWNLOAD_PATH = "http://dev.tesfabunna.com/";
    private static final String API_URL="http://dev.tesfabunna.com/api/";
    private static final String API_AUTH_URL="http://dev.tesfabunna.com/api/auth/";
    private static final String DATA_PATH = "advertData";
    private static final String DB_NAME = "TabletAdsDB";
    private static int realWidth,realHeight;
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

    public static int getRealWidth() {
        return realWidth;
    }

    public static void setRealWidth(int realWidth) {
        Constants.realWidth = realWidth;
    }

    public static int getRealHeight() {
        return realHeight;
    }

    public static void setRealHeight(int realHeight) {
        Constants.realHeight = realHeight;
    }

    public static  void calculateDeviceResolution(AppCompatActivity context) {
        Display display = context.getWindowManager().getDefaultDisplay();

        if (Build.VERSION.SDK_INT >= 17) {
            //new pleasant way to get real metrics
            DisplayMetrics realMetrics = new DisplayMetrics();
            display.getRealMetrics(realMetrics);
            setRealWidth(realMetrics.widthPixels);
            setRealHeight(realMetrics.heightPixels);

        } else {
            //This should be close, as lower API devices should not have window navigation bars
            setRealWidth(display.getWidth());
            setRealHeight(display.getHeight());
        }
    }

    public static String currentDate(){
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        return date;
    }

    public static String todaysDate(){
        String date = Constants.currentDate();
        String today = date.substring(0,date.lastIndexOf(" "));
        return  today;
    }

    public static String yesterdaysDate(){
        String date = Constants.todaysDate();
        String yesterdayDate = String.valueOf(Integer.valueOf(date.substring(0,date.indexOf("/")))-1);
        return yesterdayDate;
    }

    public static String getDownloadingPreference(){
        return "Downloads";
    }

    public static ArrayList<Contact> getContacts(){
        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("http://facebook.com","Facebook"));
        contacts.add(new Contact("https://www.linkedin.com/in/meseret-kassaye-013409123/","Linkedin"));
        contacts.add(new Contact("tg://resolve?domain=ethiobraves","Telegram"));
        contacts.add(new Contact("whatsapp://send?phone=+251923644545","WatsApp"));
        return contacts;
    }

    public static  ArrayList<CallPhone> getPhones(){
        ArrayList<CallPhone> phones = new ArrayList<>();
        phones.add(new CallPhone("0923 64 45 45"));
        phones.add(new CallPhone("0923 27 55 71"));

        return phones;
    }
}
