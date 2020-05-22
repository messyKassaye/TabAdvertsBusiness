package com.example.tabadvertsbusiness.auth.view.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.adapter.MyAdvertsAdapter;
import com.example.tabadvertsbusiness.auth.helpers.GridSpacingItemDecoration;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertRoomVIewModel;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertViewsViewModel;

import java.util.ArrayList;
import java.util.List;


public class MyAdvertsFragment extends Fragment {
    private ProgressBar progressBar;
    private LinearLayout noAdvertsFound;
    private RecyclerView recyclerView;
    private AdvertRoomVIewModel advertRoomVIewModel;
    private MyAdvertsAdapter advertsAdapter;
    private ArrayList<AdvertRoom> arrayList = new ArrayList<>();
    public MyAdvertsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_adverts, container, false);

        advertRoomVIewModel = ViewModelProviders.of(getActivity()).get(AdvertRoomVIewModel.class);


        progressBar = view.findViewById(R.id.myAdvertPbr);
        noAdvertsFound = view.findViewById(R.id.noAdvertsFound);

        recyclerView = view.findViewById(R.id.advertsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, GridSpacingItemDecoration.dpToPx(10,getContext()), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        advertsAdapter =new MyAdvertsAdapter(getActivity(),arrayList);


        advertRoomVIewModel.index().observe(getActivity(),advertViewsRooms -> {
            progressBar.setVisibility(View.GONE);
            if (advertViewsRooms.size()<=0){
                noAdvertsFound.setVisibility(View.VISIBLE);
            }else {
                recyclerView.setVisibility(View.VISIBLE);
                arrayList.addAll(advertViewsRooms);
                recyclerView.setAdapter(advertsAdapter);
                advertsAdapter.notifyDataSetChanged();

            }
        });
        return view;
    }

}
