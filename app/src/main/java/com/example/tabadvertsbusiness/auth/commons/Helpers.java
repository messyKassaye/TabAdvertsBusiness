package com.example.tabadvertsbusiness.auth.commons;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;

public class Helpers {

    private static AppCompatActivity  context;

    public static int deviceWidth(AppCompatActivity contexts){
        context = contexts;
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

}
