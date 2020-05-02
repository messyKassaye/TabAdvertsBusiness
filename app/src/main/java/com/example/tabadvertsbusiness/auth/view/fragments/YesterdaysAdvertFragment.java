package com.example.tabadvertsbusiness.auth.view.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.commons.Helpers;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertViewsViewModel;
import com.example.tabadvertsbusiness.constants.Constants;

import java.util.ArrayList;


public class YesterdaysAdvertFragment extends Fragment {


    private CardView advertCard;
    private TextView headerTitle;
    private TextView totalAdvert;
    private AdvertViewsViewModel viewModel;
    private ArrayList<AdvertViewsRoom> yesterdayAdvert;
    private AdvertViewsViewModel viewsViewModel;
    private ArrayList<AdvertViewsRoom> yesterdayAdvertData = new ArrayList<>();



    public YesterdaysAdvertFragment() {
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
        return inflater.inflate(R.layout.fragment_yesterdays_advert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(AdvertViewsViewModel.class);

        advertCard = getView().findViewById(R.id.advertCard);
        advertCard.setCardBackgroundColor(Color.parseColor("#2B2B2B"));
        int columns = Helpers.getColumn((AppCompatActivity)getContext());
        if(columns<12){
            advertCard.setLayoutParams(new LinearLayout.LayoutParams(
                    Helpers.deviceWidth((AppCompatActivity)getContext()),
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
        }

        headerTitle = getView().findViewById(R.id.headerTitle);
        headerTitle.setText(R.string.yesterday_advert);

        totalAdvert= getView().findViewById(R.id.totalAdvert);
        displayAdvertData();
    }


    public void displayAdvertData() {
        viewsViewModel = ViewModelProviders.of(getActivity()).get(AdvertViewsViewModel.class);
        viewsViewModel.index().observe(getActivity(), advertViewsRooms -> {
            String date = Constants.currentDate();
            String currentDate = date.substring(0, date.lastIndexOf(" "));
            for (int i = 0; i < advertViewsRooms.size(); i++) {
                String advertTime = advertViewsRooms.get(i).getAdvertTime();
                String advertDate = advertTime.substring(0, advertTime.lastIndexOf(" "));
                if (currentDate.equals(Constants.yesterdaysDate()) && !advertViewsRooms.get(i).isSend()) {
                    yesterdayAdvertData.add(advertViewsRooms.get(i));
                }
            }
            totalAdvert.setText("" + yesterdayAdvertData.size());
        });
    }
}
