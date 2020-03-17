package com.example.tabadvertsbusiness.auth.view.downloaderView;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.example.tabadvertsbusiness.constants.Constants;

public class DownloadManagerClass {

    public void download(Context context,String filePath){

        String url = Constants.getDownloadPath()+"Zips/"+filePath;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Downloading "+filePath);
        request.setTitle("Tab adverts download");
   // in order for this if to run, you must use the android 3.2 to compile your app
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filePath);

// get download service and enqueue file
        DownloadManager manager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }
}
