package com.example.tabadvertsbusiness.auth.helpers;

import android.content.Context;
import android.net.Uri;
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
import java.util.zip.ZipInputStream;

public class Unzipper {
    private File _zipFile;
    private InputStream _zipFileStream;
    private Context context;
    private  String ROOT_LOCATION;
    private static final String TAG = "UNZIPUTIL";

    public Unzipper(Context context, File zipFile) {
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

    public String unzip() {
        String fileOnUnzip = null;
        String JSONfileName= "";
        try  {
            Log.i(TAG, "Starting to unzip");
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
                fileOnUnzip  = ze.getName();
                if(ze.isDirectory()) {
                    _dirChecker(ROOT_LOCATION + "/" + ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(new File(ROOT_LOCATION, ze.getName()));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int count;
                    // reading and writing
                    while((count = zin.read(buffer)) != -1)
                    {
                        baos.write(buffer, 0, count);
                        byte[] bytes = baos.toByteArray();
                        fout.write(bytes);
                        baos.reset();

                    }
                    fout.close();
                    zin.closeEntry();
                }

            }

            zin.close();
            handleJSONfile(JSONfileName);
            this.deleteZipFile(_zipFile);
            return fileOnUnzip;
        } catch(Exception e) {
            Log.e(TAG, "Unzip Error", e);
            return null;
        }

    }
    public void handleJSONfile(String file){
        System.out.println("Ze: "+this.readFromFile(file));
        try {
            JSONObject jsonObject = new JSONObject(this.readFromFile(file));

            JSONObject userInfo = jsonObject.getJSONObject("userInfo");
            JSONArray advertData = jsonObject.getJSONArray("advertData");
            for (int i=0;i<=advertData.length();i++){
                JSONObject advertJSON = advertData.getJSONObject(i);
                AdvertRoom advertRoom = new AdvertRoom();
                advertRoom.setId(advertJSON.getInt("id"));
                advertRoom.setFileName(advertJSON.getString("fileName"));
                advertRoom.setPrivilege(advertJSON.getString("privilege"));
                advertRoom.setMaximumViewPerDay(advertJSON.getInt("maximumViewPerDay"));
                AdvertDAO advertDAO = TabletAdsRoomDatabase.getDatabase(context).getAdvertDAO();
                advertDAO.store(advertRoom);

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