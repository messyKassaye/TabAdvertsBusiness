package com.example.tabadvertsbusiness.player.players;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import com.example.tabadvertsbusiness.constants.Constants;
import com.example.tabadvertsbusiness.home.HomeActivity;
import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.dialogs.LoadingDialog;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.EntertainmentRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertViewsViewModel;
import com.example.tabadvertsbusiness.player.PlayerController;
import com.example.tabadvertsbusiness.player.helpers.FaceDetectionStarted;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.facebook.FacebookSdk.getApplicationContext;

public class EntertainmentPlayer extends Fragment implements View.OnTouchListener, SurfaceHolder.Callback, MediaPlayer.OnPreparedListener,
        MediaController.MediaPlayerControl {


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

    private MediaController mediaController;
    private Handler handler = new Handler();
    private SurfaceHolder surfaceHolder;



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
        player = new MediaPlayer();
        player.setOnPreparedListener(this::onPrepared);
        mediaController = new MediaController(getActivity());
        surfaceHolder = surfaceView.getHolder();
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mediaController!=null){
                    mediaController.show();
                }
                return false;
            }
        });


        playerController = view.findViewById(R.id.mediaController);
        closePlayer = view.findViewById(R.id.closePlayer);
        hidePlayer = view.findViewById(R.id.hideController);
        playButton = view.findViewById(R.id.playButton);

        closeMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                Intent intent = new Intent(getActivity(),HomeActivity.class);
                getActivity().startActivity(intent);
            }
        });

        prepareEntertainmentFile();


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
        startPlaying(playList.get(0).getFilePath());

    }


    public void startPlaying(String path) {
        try {

            player.setDataSource(path);
            player.setDisplay(surfaceView.getHolder());
            player.prepare();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override public void onCompletion(MediaPlayer mp) {
                    player.reset();
                    startPlaying(playList.get(getRandomPlayId()).getFilePath());
                }
            });
            player.start();

        }catch (Exception e){

        }
    }

    public int getRandomPlayId(){
        Random random = new Random();
        return random.nextInt(playList.size());
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
        mediaController.show();
        return false;
    }




    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        player.start();
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(surfaceView);
        handler.post(new Runnable() {

            public void run() {
                mediaController.setEnabled(true);
                mediaController.show();
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return player.getAudioSessionId();
    }
}