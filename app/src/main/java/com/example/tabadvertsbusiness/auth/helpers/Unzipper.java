package com.example.tabadvertsbusiness.auth.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.tabadvertsbusiness.auth.roomDB.DAO.AdvertDAO;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertRoomVIewModel;
import com.example.tabadvertsbusiness.constants.Constants;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import io.reactivex.Observable;

public class Unzipper extends AsyncTask<Void, Integer, Integer>{
    private File _zipFile;
    private InputStream _zipFileStream;
    private Context context;
    private  String ROOT_LOCATION;
    private static final String TAG = "UNZIPUTIL";
    private String JSONfileName = null;
    ProgressDialog myProgressDialog;
    private int newFileNumber=0;
    private int noNewFileFound =0;

    public Unzipper(Context context, File zipFile) {
        super();
        _zipFile = zipFile;
        this.context = context;

        ROOT_LOCATION = context.getExternalFilesDir(null)+"/advertData";

        _dirChecker("");
    }

    public Unzipper(Context context, InputStream zipFile) {
        _zipFileStream = zipFile;
        this.context = context;

        _dirChecker("");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        myProgressDialog = new ProgressDialog(context);
        myProgressDialog.setMessage("Please Wait.... Unzipping");
        myProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        myProgressDialog.setCancelable(false);
        myProgressDialog.show();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int count = 0;
        try  {
            ZipFile zip = new ZipFile(_zipFile);
            myProgressDialog.setMax(zip.size());
            InputStream fin = _zipFileStream;
            if(fin == null) {
                fin = new FileInputStream(_zipFile);
            }
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v(TAG, "Unzipping " + ze.getName());
                Uri uri = Uri.fromFile(new File(ze.getName()));
                String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
                if(extension.equals("json")){
                    JSONfileName = ze.getName();

                }
                if(ze.isDirectory()) {
                    _dirChecker(ROOT_LOCATION + "/" + ze.getName());
                } else {
                    File previouslyDownloadedFile = new File(ROOT_LOCATION+"/"+ze.getName());
                    if(!previouslyDownloadedFile.exists()){
                        FileOutputStream fout = new FileOutputStream(new File(ROOT_LOCATION, ze.getName()));
                        byte[] buffer = new byte[8192];
                        int len;
                        // reading and writing
                        while((len = zin.read(buffer)) != -1)
                        {
                            fout.write(buffer, 0, len);
                        }
                        fout.close();
                        zin.closeEntry();
                        newFileNumber++;
                    }else {
                        noNewFileFound++;
                    }
                    count++;
                    publishProgress(count);
                }

            }

            zin.close();

        } catch(Exception e) {
            Log.e(TAG, "Unzip Error", e);
        }

        return count;
    }


    protected void onProgressUpdate(Integer... progress) {
        myProgressDialog.setProgress(progress[0]); //Since it's an inner class, Bar should be able to be called directly
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        handleJSONfile(JSONfileName);
        deleteZipFile(_zipFile);

        if(newFileNumber>0){
            myProgressDialog.setMessage("We found "+newFileNumber+" new file");
        }else {
            myProgressDialog.setMessage("We couldn't found any file this file contains for you");
        }
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        myProgressDialog.dismiss();
                    }
                },
                5000);
    }

    /*public String unzip() {

    }*/

    public void handleJSONfile(String file){
        try {
            JSONObject jsonObject = new JSONObject(this.readFromFile(file));

            JSONObject userInfo = jsonObject.getJSONObject("userInfo");
            JSONArray advertData = jsonObject.getJSONArray("advertData");
            for (int i=0;i<=advertData.length();i++){
                JSONObject advertJSON = advertData.getJSONObject(i);
                AdvertRoom advertRoom = new AdvertRoom();
                advertRoom.setAdvertId(advertJSON.getInt("id"));
                advertRoom.setFileName(advertJSON.getString("fileName"));
                advertRoom.setPrivilege(advertJSON.getString("privilege"));
                advertRoom.setMaximumViewPerDay(advertJSON.getInt("maximumViewPerDay"));
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                       TabletAdsRoomDatabase tabletAdsRoomDatabase= TabletAdsRoomDatabase.getDatabase(context);
                       AdvertDAO advertDAO = tabletAdsRoomDatabase.getAdvertDAO();

                       AdvertRoom storedAdvert = advertDAO.show(advertRoom.getAdvertId());
                       if(storedAdvert==null){
                           advertDAO.store(advertRoom);
                       }
                    }
                });

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String readFromFile(String fileName) {

        String ret = "";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(ROOT_LOCATION+"/"+fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    public void deleteZipFile(File file){
        if(file.exists()){
            if(file.delete()){
                System.out.println("deleted");
            }else {
                System.out.println("not deleted");
            }
        }
    }

    private void _dirChecker(String dir) {
        File f = new File(dir);
        Log.i(TAG, "creating dir " + dir);

        if(dir.length() >= 0 && !f.isDirectory() ) {
            f.mkdirs();
        }
    }


}