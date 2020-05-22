package com.example.tabadvertsbusiness.player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.tabadvertsbusiness.home.HomeActivity;
import com.example.tabadvertsbusiness.home.fragments.HomeFragment;
import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.dialogs.LoadingDialog;
import com.example.tabadvertsbusiness.auth.roomDB.DAO.AdvertDAO;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.EntertainmentRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertViewsViewModel;
import com.example.tabadvertsbusiness.constants.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player extends AppCompatActivity {


    private ProgressDialog progressDialog;
    private ArrayList<EntertainmentRoom> playList = new ArrayList<>();
    private ArrayList<AdvertRoom> advertPlaylist = new ArrayList<>();
    private MediaPlayer player;

    private SurfaceView surfaceView;

    private RelativeLayout playerController;
    private Button closePlayer,hidePlayer;
    private ImageButton playButton;
    private int i=0;
    private int entertainmentCounter=1;

    private AdvertViewsViewModel vIewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //create fullscreen activity
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //create fullscreen activity
        setContentView(R.layout.activity_player);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//make screen open always

        //land scape
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        vIewModel = ViewModelProviders.of(this).get(AdvertViewsViewModel.class);

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
        playButton = findViewById(R.id.playButton);

        closePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player.isPlaying()){
                    player.release();
                }

                finish();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
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

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player.isPlaying()){
                    playerController.setVisibility(View.GONE);
                }else {
                    player.start();
                }
            }
        });

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

        advertPlaylist.addAll(adverts);
        playList.addAll(entertainment);

        playRecursively(playList.get(i).getFilePath(),"entertainment",0);


    }

    public void playRecursively(String path,String type,int id){
        try {

            player = new MediaPlayer();
            player.setDisplay(surfaceView.getHolder());
            player.setDataSource(path);
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    if (type.equalsIgnoreCase("advert")){
                        entertainmentCounter = 1;
                        saveAdvertViewData(id);
                    }

                    entertainmentCounter++;
                    if (entertainmentCounter==3){
                        Random random = new Random();
                        int nextAdvertPlayId = random.nextInt(advertPlaylist.size()-1);
                        player.release();
                        playRecursively(
                                advertPlaylist.get(nextAdvertPlayId).getFilePath(),
                                "advert",advertPlaylist.get(nextAdvertPlayId).getAdvertId());
                    }else {
                        i++;
                        if (i==playList.size()-1){
                            i=0;
                            player.release();
                            playRecursively(playList.get(i).getFilePath(),"entertainment",0);
                        }else {
                            player.release();
                            playRecursively(playList.get(i).getFilePath(),"entertainment",0);
                        }
                    }

                }
            });

        }catch (Exception e){

        }
    }

    public void saveAdvertViewData(int advertId){
        String date = Constants.currentDate();
        AdvertViewsRoom advertViewsRoom = new AdvertViewsRoom();
        advertViewsRoom.setAdvertId(advertId);
        advertViewsRoom.setNumberOfViewers(4);
        advertViewsRoom.setPicture("No picture is found. because openCV camera doesn't work on emulator");
        advertViewsRoom.setAdvertTime(date);
        vIewModel.store(advertViewsRoom);
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