package com.example.tabadvertsbusiness.auth.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertRoomVIewModel;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.EntertainmentViewModel;

public class MyFilesFragment extends Fragment {

    private EntertainmentViewModel entertainmentViewModel;
    private AdvertRoomVIewModel advertRoomVIewModel;
    private TextView totalEntertainmentFiles,totalAdvertFiles;
    public MyFilesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_files, container, false);

        entertainmentViewModel = ViewModelProviders.of(getActivity()).get(EntertainmentViewModel.class);
        advertRoomVIewModel = ViewModelProviders.of(getActivity()).get(AdvertRoomVIewModel.class);

        totalEntertainmentFiles = view.findViewById(R.id.totalEntertainmentFiles);
        totalAdvertFiles = view.findViewById(R.id.totalAdvertFiles);

        entertainmentViewModel.index().observe(getActivity(),entertainmentRooms -> {
            totalEntertainmentFiles.setText(""+entertainmentRooms.size());
        });

        advertRoomVIewModel.index().observe(getActivity(),advertRooms -> {
            totalAdvertFiles.setText(""+advertRooms.size());
        });


        return view;
    }


}
