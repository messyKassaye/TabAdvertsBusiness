package com.example.tabadvertsbusiness.player.adverts;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.player.players.AdvertPlayer;
import com.example.tabadvertsbusiness.player.players.BeforeStartingAdvertFindDataFragment;
import com.example.tabadvertsbusiness.player.players.EntertainmentPlayer;


public class MainAdvertContainer extends Fragment {

    private LinearLayout beforeAdvertFrament,advertFragment;
    public MainAdvertContainer() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_main_advert_container, container, false);
        return  view;
    }

    public void showAdvert(){
        AdvertPlayer newFragment = new AdvertPlayer();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.advert_content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

}
