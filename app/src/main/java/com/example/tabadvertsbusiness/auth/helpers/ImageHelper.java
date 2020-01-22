package com.example.tabadvertsbusiness.auth.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ImageHelper {

    public static Bitmap convertBase64ToImage(String encodeString){
        byte[] decodedString = Base64.decode(encodeString.substring(encodeString.indexOf(",")+1), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

}
