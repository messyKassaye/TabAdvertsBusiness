package com.example.tabadvertsbusiness.home.fragments;

import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.home.HomeActivity;
import com.example.tabadvertsbusiness.player.PlayerDashboard;

import java.util.Timer;

public class HomeFragment extends Fragment {

    private Timer timer;
    TextView welcome_and_wish;
    Button startAdvert,startUpdating,signUp;

    public HomeFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main,container,false);

        startAdvert = view.findViewById(R.id.start_advertising);

        startAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity home = (HomeActivity)getActivity();
                home.showPlay();
            }
        });

        startUpdating = view.findViewById(R.id.start_updating);
        startUpdating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity home = (HomeActivity)getActivity();
                home.showLogin();
            }
        });

        signUp = view.findViewById(R.id.signUP);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity home = (HomeActivity)getActivity();
                home.showSignUp();
            }
        });

        return view;
    }

}
