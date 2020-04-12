package com.example.tabadvertsbusiness.player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tabadvertsbusiness.LoginActivity;
import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.player.fragment.EntertainmentFilesFragment;
import com.example.tabadvertsbusiness.constants.Constants;

import java.util.List;

public class PlayerDashboard extends AppCompatActivity implements EntertainmentFilesFragment.OnFragmentInteractionListener {

    private LinearLayout playerInfoLayout;
    private Button login;
    private TextView infoText,headerText;

    private LinearLayout mainPlayertStarterLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**/
        setContentView(R.layout.activity_player_dashboard);
        Toolbar toolbar =(Toolbar)findViewById(R.id.playerToolbar);
        toolbar.setTitle("Tab adverts business");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        playerInfoLayout = findViewById(R.id.playerInfoLayout);
        infoText = findViewById(R.id.playerInfo);
        headerText = findViewById(R.id.noAdvertData);

        login = findViewById(R.id.playerInfoButton);

        mainPlayertStarterLayout = findViewById(R.id.mainPlayerStarterLayout);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });



        TabletAdsRoomDatabase.getDatabase(getApplicationContext())
                .getAdvertDAO().index().observe(this,advertRooms -> {

            System.out.println("Size: "+advertRooms.size());
            if (advertRooms.size()<=0){
                playerInfoLayout.setVisibility(View.VISIBLE);
                headerText.setText("Advert data is not found in this tablet.");
                infoText.setText("Please login and download or receive advert data then you can " +
                        "start playing advert videos.");
            }else {
                startPlayingAdvert(advertRooms);
            }
        });


    }

    public void startPlayingAdvert(List<AdvertRoom> advertRooms){
        mainPlayertStarterLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
