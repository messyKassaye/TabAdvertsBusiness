package com.example.tabadvertsbusiness.auth.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.adapter.TabViewPagerAdapter;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertRoomVIewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class ShowAllAdvertsTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private TabLayout tab;
    private ViewPager viewPager;
    private TabViewPagerAdapter viewPageAdapter;
    private AdvertRoomVIewModel advertRoomVIewModel;
    private ArrayList<AdvertRoom> advertRoomArrayList = new ArrayList<>();

    private ProgressBar progressBar;
    private LinearLayout mainLayout;
    public ShowAllAdvertsTab(ArrayList<AdvertViewsRoom> advertViews) {
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
        View view= inflater.inflate(R.layout.fragment_show_all_tab, container, false);

        progressBar = view.findViewById(R.id.tabPr);
        mainLayout = view.findViewById(R.id.tabMainLayout);

        advertRoomVIewModel = ViewModelProviders.of(getActivity()).get(AdvertRoomVIewModel.class);

        advertRoomVIewModel.index().observe(getActivity(),advertRooms -> {
            progressBar.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
            viewPageAdapter = new TabViewPagerAdapter(getFragmentManager(),advertRooms.size());
            for (int i=0;i<advertRooms.size();i++){
                viewPageAdapter.addFragment(new ShowAllAdvertsFragment(advertRooms.get(i).getId()),advertRooms.get(i).getProduct_name()+" ");
            }
            tab = view.findViewById(R.id.advertTab);
            viewPager = view.findViewById(R.id.advertsViewPager);
            viewPager.setAdapter(viewPageAdapter);
            tab.setupWithViewPager(viewPager);
        });




        return view;
    }


}
