package com.example.tabadvertsbusiness.player.players;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.tabadvertsbusiness.home.HomeActivity;
import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.dialogs.LoadingDialog;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.EntertainmentRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertViewsViewModel;
import com.example.tabadvertsbusiness.player.PlayerController;
import com.example.tabadvertsbusiness.player.helpers.FaceDetectionStarter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.facebook.FacebookSdk.getApplicationContext;

public class EntertainmentPlayer extends Fragment implements View.OnTouchListener {


    private ProgressDialog progressDialog;
    private ArrayList<EntertainmentRoom> playList = new ArrayList<>();
    private MediaPlayer player;

    private SurfaceView surfaceView;

    private RelativeLayout playerController;
    private Button closePlayer,hidePlayer;
    private ImageButton playButton;
    private int i=0;
    private AdvertViewsViewModel vIewModel;
    private Button closeMedia;


    public EntertainmentPlayer() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.entertainment_player,container,false);

        //land scape
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        vIewModel = ViewModelProviders.of(this).get(AdvertViewsViewModel.class);

        progressDialog = LoadingDialog.loadingDialog(getActivity(),"Please wait...." +
                "we are processing your data");
        progressDialog.show();

        //surface view for place of media player
        surfaceView = view.findViewById(R.id.playerSurfaceView);
        surfaceView.setZOrderOnTop(false);

        closeMedia = view.findViewById(R.id.closeMedia);
        //media player controller
        playerController = view.findViewById(R.id.mediaController);
        closePlayer = view.findViewById(R.id.closePlayer);
        hidePlayer = view.findViewById(R.id.hideController);
        playButton = view.findViewById(R.id.playButton);

        closePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player.isPlaying()){
                    player.release();
                }

                getActivity().finish();
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


        prepareEntertainmentFile();

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

        closeMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                Intent intent = new Intent(getActivity(),HomeActivity.class);
                getActivity().startActivity(intent);
            }
        });


        return  view;
    }


    public void prepareEntertainmentFile(){

        TabletAdsRoomDatabase db = TabletAdsRoomDatabase.getDatabase(getApplicationContext());
        db.getEntertainmentDAO().index()
                .observe(this,entertainmentRooms -> {
                    preparePlayList(entertainmentRooms);
                });

    }

    public void preparePlayList(List<EntertainmentRoom> entertainment){

        progressDialog.dismiss();
        playList.addAll(entertainment);

        Random random = new Random();
        int randomNumber = random.nextInt(playList.size());
        playRecursively(playList.get(randomNumber).getFilePath());


    }

    public void playRecursively(String path){
        try {

            player = new MediaPlayer();
            player.setDisplay(surfaceView.getHolder());
            player.setDataSource(path);
            player.prepare();
            player.start();
            long faceDetectionTime = player.getDuration()-20000;
            FaceDetectionStarter counter = new FaceDetectionStarter(faceDetectionTime,1000);
            counter.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    prepareAdvertDatas();
                }
            });

        }catch (Exception e){

        }
    }


    @Override
    public void onPause() {
        super.onPause();

        if(player!=null){
            player.release();
            player=null;
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        playerController.setVisibility(View.VISIBLE);
        return false;
    }

    public void prepareAdvertDatas(){
        PlayerController controller = (PlayerController)getActivity();
        controller.preparedAdvertData();
    }

    public class CameraStarter extends CountDownTimer {

        public CameraStarter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long elapsedhour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));

            long elapsedMinute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));

            long elapsedSecond = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
            String elapsedTime = "" + elapsedhour + ":" + elapsedMinute + ":" + elapsedSecond;
        }

        @Override
        public void onFinish() {
            System.out.println("FaceDetection started");
        }
    }
}