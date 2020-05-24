package com.example.tabadvertsbusiness.player;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tabadvertsbusiness.home.HomeActivity;
import com.example.tabadvertsbusiness.home.fragments.LoginFragment;
import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import java.util.List;

public class PlayerDashboard extends Fragment {

    private LinearLayout playerInfoLayout;
    private Button login;
    private TextView infoText,headerText;

    private LinearLayout mainPlayertStarterLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**/

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_player_dashboard,container,false);

        playerInfoLayout = view.findViewById(R.id.playerInfoLayout);
        infoText = view.findViewById(R.id.playerInfo);
        headerText = view.findViewById(R.id.noAdvertData);

        login = view.findViewById(R.id.playerInfoButton);

        mainPlayertStarterLayout = view.findViewById(R.id.mainPlayerStarterLayout);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity homeActivity = (HomeActivity)getActivity();
                homeActivity.showLogin();
            }
        });



        TabletAdsRoomDatabase.getDatabase(getContext())
                .getAdvertDAO().index().observe(this,advertRooms -> {

            if (advertRooms.size()<=0){
                playerInfoLayout.setVisibility(View.VISIBLE);
                headerText.setText("Advert data is not found in this tablet.");
                infoText.setText("Login and download or receive advert data then you can " +
                        "start playing advert videos.");
            }else {
                startPlayingAdvert(advertRooms);
            }
        });
        return view;
    }

    public void startPlayingAdvert(List<AdvertRoom> advertRooms){
        mainPlayertStarterLayout.setVisibility(View.VISIBLE);
    }

}
