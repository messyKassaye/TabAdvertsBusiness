package com.example.tabadvertsbusiness.player.players;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
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
import android.widget.Toast;

import com.example.tabadvertsbusiness.auth.commons.MainDialog;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertRoomVIewModel;
import com.example.tabadvertsbusiness.constants.Constants;
import com.example.tabadvertsbusiness.home.HomeActivity;
import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.EntertainmentRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertViewsViewModel;
import com.example.tabadvertsbusiness.player.PlayerController;
import com.example.tabadvertsbusiness.player.adverts.AdvertDialog;
import com.example.tabadvertsbusiness.player.adverts.MainAdvertContainer;
import com.example.tabadvertsbusiness.player.helpers.ScalingUtilities;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.facebook.FacebookSdk.getApplicationContext;

public class EntertainmentPlayer extends Fragment implements View.OnTouchListener, SurfaceHolder.Callback, MediaPlayer.OnPreparedListener,
        MediaController.MediaPlayerControl {


    private ArrayList<EntertainmentRoom> playList = new ArrayList<>();
    private MediaPlayer player;

    private SurfaceView surfaceView;

    private RelativeLayout playerController;
    private Button closePlayer,hidePlayer;
    private ImageButton playButton;
    private int i=0;
    private AdvertViewsViewModel vIewModel;
    private Button closeMedia;
    private SurfaceHolder surfaceHolder;
    private MediaController mediaController;
    private Handler handler = new Handler();
    private AdvertDialog advertDialog;
    private  AdverTimeCounter adverTimeCounter;
    private RelativeLayout advertStartIndicator;
    private Button addCounterButton;

    private int advertStartingDelay = 0;



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
        vIewModel = ViewModelProviders.of(this).get(AdvertViewsViewModel.class);

        closeMedia = view.findViewById(R.id.closeMedia);

        //surface view for place of media player
        surfaceView = view.findViewById(R.id.playerSurfaceView);
        surfaceView.setZOrderOnTop(false);

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

        closeMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                Intent intent = new Intent(getActivity(),HomeActivity.class);
                getActivity().startActivity(intent);
            }
        });

        advertStartIndicator = view.findViewById(R.id.addStarterView);
        addCounterButton = view.findViewById(R.id.addCounterButton);




        //advert starter
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
        playList.addAll(entertainment);
        startPlaying(playList.get(getRandomPlayId()).getFilePath());
        adverTimeCounter = new AdverTimeCounter(120000,1000);
        adverTimeCounter.start();


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



    public class AdverTimeCounter extends CountDownTimer {

        public AdverTimeCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long elapsedhour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));

            long elapsedMinute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));

            long elapsedSecond = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
            System.out.println("ELAPSED: "+elapsedSecond);
            if (millisUntilFinished<=15000){
                advertStartIndicator.setVisibility(View.VISIBLE);
                addCounterButton.setText(""+elapsedSecond);
            }


        }

        @Override
        public void onFinish() {
            player.pause();
            advertStartIndicator.setVisibility(View.GONE);
            advertDialog = new AdvertDialog();
            AdvertPlayer advertPlayer = new AdvertPlayer();
            advertDialog.display(getFragmentManager(),advertPlayer);

            AdvertDelayCounter advertCloseTimer =new AdvertDelayCounter(10000,1000);
            advertCloseTimer.start();
            adverTimeCounter.cancel();
        }
    }

    class AdvertDelayCounter extends CountDownTimer{


        public AdvertDelayCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            long elapsedSecond = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
            advertStartingDelay +=millisUntilFinished;
            if (advertStartingDelay>0){
                AdvertCloseTimer advertCloseTimer= new AdvertCloseTimer(
                        Constants.getAdvertPlayTimer(getContext())+advertStartingDelay,
                        1000);
                advertCloseTimer.start();
                advertStartingDelay = 0;
            }
        }

        @Override
        public void onFinish() {
            this.cancel();
        }
    }

    class AdvertCloseTimer extends CountDownTimer{


        public AdvertCloseTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            advertDialog.closeDialog();
            player.start();

            //restart advert time counter
            adverTimeCounter.cancel();
            adverTimeCounter.start();

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