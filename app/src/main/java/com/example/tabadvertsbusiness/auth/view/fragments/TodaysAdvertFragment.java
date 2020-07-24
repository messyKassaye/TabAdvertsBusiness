package com.example.tabadvertsbusiness.auth.view.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.commons.MainDialog;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertViewsViewModel;
import com.example.tabadvertsbusiness.constants.Constants;

import java.util.ArrayList;

public class TodaysAdvertFragment extends Fragment {

    private CardView advertCard;
    private TextView headerTitle;
    private TextView totalAdvert;
    private AdvertViewsViewModel viewsViewModel;
    private ArrayList<AdvertViewsRoom> todayAdvertData = new ArrayList<>();
    private LinearLayout mainLayout,noAdvertLayout;
    private Button showAll;
    public TodaysAdvertFragment() {
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
        return inflater.inflate(R.layout.fragment_todays_advert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //find car id of this tablet
        mainLayout = getView().findViewById(R.id.advertContentLayout);
        noAdvertLayout = getView().findViewById(R.id.noAdvertIsFound);

        advertCard = getView().findViewById(R.id.advertCard);
        advertCard.setCardBackgroundColor(Color.parseColor("#2E8B57"));

        showAll = getView().findViewById(R.id.showAll);

        headerTitle = getView().findViewById(R.id.headerTitle);
        headerTitle.setText(R.string.today_advert);
        totalAdvert = getView().findViewById(R.id.totalAdvert);

        displayAdvertData();

        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShowAllAdvertsTab fragment = new ShowAllAdvertsTab(todayAdvertData);
                MainDialog dialog = new MainDialog();
                dialog.display(
                        getFragmentManager(),
                        "Today's advert",
                        fragment
                );
            }
        });



    }

    public void displayAdvertData() {
        viewsViewModel = ViewModelProviders.of(getActivity()).get(AdvertViewsViewModel.class);
        viewsViewModel.index().observe(getActivity(), advertViewsRooms -> {
            String currentDate = Constants.currentDate();
            for (int i = 0; i < advertViewsRooms.size(); i++) {
                String advertDate = advertViewsRooms.get(i).getAdvertDate();
                if (currentDate.equals(advertDate)) {
                    todayAdvertData.add(advertViewsRooms.get(i));
                }

            }
            totalAdvert.setText("" + todayAdvertData.size());
            if (todayAdvertData.size()<=0){
                mainLayout.setVisibility(View.GONE);
                noAdvertLayout.setVisibility(View.VISIBLE);
            }else {
                mainLayout.setVisibility(View.VISIBLE);
                noAdvertLayout.setVisibility(View.GONE);
            }
        });

    }




}
