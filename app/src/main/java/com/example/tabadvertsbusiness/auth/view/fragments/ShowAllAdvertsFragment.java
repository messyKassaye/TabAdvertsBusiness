package com.example.tabadvertsbusiness.auth.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.model.AdvertView;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;

import java.util.ArrayList;


public class ShowAllAdvertsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private ArrayList<AdvertViewsRoom> advertViewsList;
    private RecyclerView recyclerView;
    public ShowAllAdvertsFragment(ArrayList<AdvertViewsRoom> advertViews) {
        // Required empty public constructor
        this.advertViewsList = advertViews;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_show_all_adverts, container, false);

        recyclerView = view.findViewById(R.id.showAllAdvertsRecyclerView);
        

        return view;
    }


}
