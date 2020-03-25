package com.example.tabadvertsbusiness.auth.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.tabadvertsbusiness.auth.helpers.Unzipper;

import java.io.File;

public class DownloadCompletedBroadcastReceiver extends BroadcastReceiver {
    private Context context;
    private String fileName;
   private Unzipper unzipper;
   private File file;
    public DownloadCompletedBroadcastReceiver(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
        file = new File(context.getExternalFilesDir(null)+"/advertData/"+fileName);
        this.unzipper = new Unzipper(context,file);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        unzipper.unzip();

    }
}
