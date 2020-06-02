package com.example.tabadvertsbusiness.player.players;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.roomDB.DAO.AdvertDAO;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.EntertainmentRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertViewsViewModel;
import com.example.tabadvertsbusiness.constants.Constants;
import com.example.tabadvertsbusiness.player.PlayerController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AdvertPlayer extends Fragment {
    private ArrayList<AdvertRoom> advertPlaylist = new ArrayList<>();
    private MediaPlayer player;
    private AdvertViewsViewModel vIewModel;
    private SurfaceView surfaceView;
    public AdvertPlayer() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_advert_player,container,false);

        vIewModel = ViewModelProviders.of(getActivity()).get(AdvertViewsViewModel.class);

        surfaceView = view.findViewById(R.id.advertPlayerSurfaceView);
        surfaceView.setZOrderOnTop(false);
        prepareAdvertData();
        return view;
    }

    public void prepareAdvertData(){

        TabletAdsRoomDatabase db = TabletAdsRoomDatabase.getDatabase(getApplicationContext());

        AdvertDAO advertDAO = db.getAdvertDAO();
        advertDAO.index().observe(this,advertRooms -> {
            preparePlayList(advertRooms);
        });

    }

    public void preparePlayList(List<AdvertRoom> adverts){

        advertPlaylist.addAll(adverts);

        Random random = new Random();
        int randomNumber = random.nextInt(advertPlaylist.size());
        playRecursively(advertPlaylist.get(randomNumber).getFilePath(),advertPlaylist.get(randomNumber).getAdvertId());


    }

    public void playRecursively(String path,int advertId){
        try {

            player = new MediaPlayer();
            player.setDisplay(surfaceView.getHolder());
            player.setDataSource(path);
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    saveAdvertViewData(advertId);

                    //back to entertainment
                    PlayerController controller = (PlayerController)getActivity();
                    controller.showEntertainment();

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

    public void saveAdvertViewData(int advertId){
        String date = Constants.currentDate();
        AdvertViewsRoom advertViewsRoom = new AdvertViewsRoom();
        advertViewsRoom.setAdvertId(advertId);
        advertViewsRoom.setNumberOfViewers(4);
        advertViewsRoom.setPicture(Constants.getImage(getContext()));
        advertViewsRoom.setAdvertTime(date);
        vIewModel.store(advertViewsRoom);
    }

}
