package com.example.tabadvertsbusiness.player;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import com.example.tabadvertsbusiness.MainActivity;
import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.dialogs.LoadingDialog;
import com.example.tabadvertsbusiness.auth.roomDB.DAO.AdvertDAO;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.EntertainmentRoom;
import com.example.tabadvertsbusiness.player.model.Media;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Player extends AppCompatActivity  {


    private ProgressDialog progressDialog;
    private ArrayList<Media>  playList = new ArrayList<>();
    private MediaPlayer player;

    private SurfaceView surfaceView;

    private RelativeLayout playerController;
    private Button closePlayer,hidePlayer;
    private boolean exit = false;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);

        //land scape
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        progressDialog = LoadingDialog.loadingDialog(this,"Please wait...." +
                "we are processing your data");
        progressDialog.show();

        //surface view for place of media player
        surfaceView = findViewById(R.id.playerSurfaceView);
        surfaceView.setZOrderOnTop(false);

        //media player controller
        playerController = findViewById(R.id.mediaController);
        closePlayer = findViewById(R.id.closePlayer);
        hidePlayer = findViewById(R.id.hideController);

        closePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player.isPlaying()){
                    player.release();
                }

                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        hidePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerController.setVisibility(View.GONE);
            }
        });



        prepareAdvertData();

    }

    public void prepareAdvertData(){

        TabletAdsRoomDatabase db = TabletAdsRoomDatabase.getDatabase(getApplicationContext());

        AdvertDAO advertDAO = db.getAdvertDAO();
        advertDAO.index().observe(this,advertRooms -> {
            db.getEntertainmentDAO().index()
                    .observe(this,entertainmentRooms -> {
                        preparePlayList(advertRooms,entertainmentRooms);
                    });
        });

    }

    public void preparePlayList(List<AdvertRoom> adverts,List<EntertainmentRoom> entertainment){

        progressDialog.dismiss();

        for (int i=0;i<adverts.size();i++){
            Media media = new Media();
            media.setFilePath(adverts.get(i).getFilePath());
            media.setMediaId(adverts.get(i).getId());
            media.setType("advert");
            if (checkMedialFileExistence(adverts.get(i).getFilePath())) {
                playList.add(media);
            }
        }

        for (int j=0;j<entertainment.size();j++){
            Media media= new Media();
            media.setType("entertainment");
            media.setMediaId(entertainment.get(j).getId());
            media.setFilePath(entertainment.get(i).getFilePath());
            if (checkMedialFileExistence(entertainment.get(i).getFilePath())) {
                playList.add(media);
            }
        }

        for (int i=0;i<playList.size();i++){
            System.out.println("Care: "+playList.get(i).getFilePath());
        }

        playRecursively(playList.get(i).getFilePath());

    }

    public void playRecursively(String  path){
        try {

            player = new MediaPlayer();
            player.setDisplay(surfaceView.getHolder());
            player.setDataSource(playList.get(i).getFilePath());
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    i+=1;
                    System.out.println("Type: "+playList.get(i).getType());
                    if (i==playList.size()-1){
                        i=0;
                        player.release();
                        playRecursively(playList.get(i).getFilePath());
                    }else{
                        player.release();
                        playRecursively(playList.get(i).getFilePath());
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkMedialFileExistence(String path){
        File file = new File(path);
        if (file.isFile()){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        playerController.setVisibility(View.VISIBLE);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(player!=null){
            player.release();
            player=null;
        }
    }
}