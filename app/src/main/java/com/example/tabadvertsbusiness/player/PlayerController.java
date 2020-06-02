package com.example.tabadvertsbusiness.player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.view.fragments.DownloadRequestSubmittedFragment;
import com.example.tabadvertsbusiness.player.players.AdvertPlayer;
import com.example.tabadvertsbusiness.player.players.BeforeStartingAdvertFindDataFragment;
import com.example.tabadvertsbusiness.player.players.EntertainmentPlayer;

public class PlayerController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //create fullscreen activity
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //create fullscreen activity
        setContentView(R.layout.activity_play_controller);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//make screen open always
        showEntertainment();
    }

    public void showEntertainment(){
        Fragment newFragment = new EntertainmentPlayer();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.player_content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void startAdvert(){
        Fragment newFragment = new AdvertPlayer();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.player_content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void preparedAdvertData(){
        Fragment newFragment = new BeforeStartingAdvertFindDataFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.player_content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
