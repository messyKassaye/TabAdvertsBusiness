package com.example.tabadvertsbusiness.auth.receiver;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.tabadvertsbusiness.auth.dialogs.LoadingDialog;
import com.example.tabadvertsbusiness.auth.helpers.Unzipper;
import com.example.tabadvertsbusiness.auth.model.DownloadRequests;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.roomDB.DAO.DownloadDAO;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.Download;
import com.example.tabadvertsbusiness.auth.sharedPreferences.ApplicationPreferenceCreator;
import com.example.tabadvertsbusiness.auth.view.fragments.DriverDownloadRequestFragment;
import com.example.tabadvertsbusiness.auth.view_model.DownloadRequestViewModel;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadCompletedBroadcastReceiver extends BroadcastReceiver {
    private Context context;
    private String fileName;
   private File file;
   private Long downloadID;
   private boolean downloading = false;
   private DownloadRequestViewModel viewModel;
   private DownloadRequests processedFile;
   private ProgressDialog progressDialog;
   private DriverDownloadRequestFragment downloadRequestFragment;
    public DownloadCompletedBroadcastReceiver(Context context, String fileName, Long downloadId,
                                              DownloadRequests processedFile, DriverDownloadRequestFragment downloadRequest) {
        this.context = context;
        this.downloadRequestFragment = downloadRequest;
        this.fileName = fileName;
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/advertData", fileName);
        this.downloadID = downloadId;
        this.processedFile = processedFile;
        progressDialog = LoadingDialog.loadingDialog(context, "Downloading your file....");

        viewModel = ViewModelProviders.of((AppCompatActivity)context).get(DownloadRequestViewModel.class);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        //Checking if the received broadcast is for our enqueued download by matching download id
        if (downloadID == id) {
            File downloadedFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/advertData", fileName);

            //File downloadedFile = new File(context.getExternalFilesDir(null)+"/advertData/"+fileName);
            if(downloadedFile.exists()){
                new Unzipper(context,file).execute();
                Download download = new Download();
                download.setFileName(fileName);
                download.setDownloadStatus("Completed");
                download.setProcess("Processed");
                saveDownloadStatus(download);
                ApplicationPreferenceCreator.setDownloadingStatus(context,null);
                viewModel.update(processedFile,processedFile.getId()).enqueue(new Callback<SuccessResponse>() {
                    @Override
                    public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                    }

                    @Override
                    public void onFailure(Call<SuccessResponse> call, Throwable t) {

                    }
                });
            }else {
                Download download = new Download();
                ApplicationPreferenceCreator.setDownloadingStatus(context,null);
                download.setFileName(fileName);
                download.setDownloadStatus("Not completed");
                download.setProcess("Not processed");
                saveDownloadStatus(download);
                progressDialog.dismiss();
            }
        }


        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = manager.query(query);
            if (cursor.moveToFirst()) {
                if (cursor.getCount() > 0) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        downloadRequestFragment.downloadSuccessful();
                        ApplicationPreferenceCreator.setDownloadingStatus(context,null);
                        Toast.makeText(context,"Congratulations.Downloading is completed",Toast.LENGTH_LONG).show();

                    } else {
                        int message = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
                        // So something here on failed.
                        downloadRequestFragment.downloadUnsucessful();
                        ApplicationPreferenceCreator.setDownloadingStatus(context,null);
                        Toast.makeText(context,"Downloading is not completed. please try again",Toast.LENGTH_LONG).show();
                        setDownloading(false);
                    }
                }
            }
        }
        //
    }

    public void saveDownloadStatus(Download download){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DownloadDAO downloadDAO = TabletAdsRoomDatabase.getDatabase(context).getDownloadDAO();
                downloadDAO.store(download);
            }
        });
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }


}
