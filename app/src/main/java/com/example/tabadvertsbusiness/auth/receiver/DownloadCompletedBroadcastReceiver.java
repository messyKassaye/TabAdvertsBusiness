package com.example.tabadvertsbusiness.auth.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.tabadvertsbusiness.MainActivity;
import com.example.tabadvertsbusiness.auth.helpers.Unzipper;

import java.io.File;

public class DownloadCompletedBroadcastReceiver extends BroadcastReceiver {
    private Context context;
    private String fileName;
   private Unzipper unzipper;
   private File file;
   private Long downloadID;
   private boolean downloading = false;
    public DownloadCompletedBroadcastReceiver(Context context, String fileName,Long downloadId) {
        this.context = context;
        this.fileName = fileName;
        file = new File(context.getExternalFilesDir(null)+"/advertData/"+fileName);
        this.unzipper = new Unzipper(context,file);
        this.downloadID = downloadId;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        //Checking if the received broadcast is for our enqueued download by matching download id
        if (downloadID == id) {
            File downloadedFile = new File(context.getExternalFilesDir(null)+"/advertData/"+fileName);
            if(downloadedFile.exists()){
                unzipper.unzip();
            }else {
                System.out.println("Download is not completed");
            }

        }
        //
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }
}
