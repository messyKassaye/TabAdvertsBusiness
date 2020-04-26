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

import java.util.ArrayList;
import java.util.List;

public class Player extends AppCompatActivity  {


    private ProgressDialog progressDialog;
    private ArrayList<Media> playList;
    private MediaPlayer player;

    private SurfaceView surfaceView;

    private RelativeLayout playerController;
    private Button closePlayer,hidePlayer;
    private int i =0;

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

            List<AdvertRoom> adverts = advertRooms;
            db.getEntertainmentDAO().index()
                    .observe(this,entertainmentRooms -> {
                        preparePlayList(adverts,entertainmentRooms);
                    });
        });

    }

    public void preparePlayList(List<AdvertRoom> adverts,List<EntertainmentRoom> entertainment){

        progressDialog.dismiss();

        playList = new ArrayList<>();
        for (int i=0;i<adverts.size();i++){
            AdvertRoom advertRoom = adverts.get(i);
            Media media = new Media();
            media.setFilePath(advertRoom.getFilePath());
            media.setMediaId(advertRoom.getId());
            media.setType("advert");
            playList.add(media);
        }

        for (int j=0;j<entertainment.size();j++){
            EntertainmentRoom entertainmentRoom = entertainment.get(j);
            Media media = new Media();
            media.setType("entertainment");
            media.setMediaId(entertainmentRoom.getId());
            media.setFilePath(entertainmentRoom.getFilePath());
            playList.add(media);
        }
        playRecursively(playList.get(i).getFilePath());


    }

    public void playRecursively(String path){
        try {

            player = new MediaPlayer();
            player.setDisplay(surfaceView.getHolder());
            player.setDataSource(path);
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    i++;
                    if (i==playList.size()-1){
                        i=0;
                        player.release();
                        playRecursively(playList.get(i).getFilePath());
                    }else {
                        player.release();
                        playRecursively(playList.get(i).getFilePath());
                    }
                }
            });


        }catch (Exception e){

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