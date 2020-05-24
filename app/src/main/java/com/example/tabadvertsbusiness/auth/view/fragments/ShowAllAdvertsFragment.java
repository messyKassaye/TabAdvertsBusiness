package com.example.tabadvertsbusiness.auth.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.adapter.AllAdvertsAdapter;
import com.example.tabadvertsbusiness.auth.helpers.GridSpacingItemDecoration;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertViewsViewModel;

import java.util.ArrayList;

public class ShowAllAdvertsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private int advertId;
    private RecyclerView recyclerView;
    private ArrayList<AdvertViewsRoom> advertViewsRoomArrayList = new ArrayList<>();
    private AdvertViewsViewModel viewsViewModel;
    private AllAdvertsAdapter adapter;

    public ShowAllAdvertsFragment(int advertId) {
        // Required empty public constructor
        this.advertId = advertId;
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

        viewsViewModel = ViewModelProviders.of(getActivity()).get(AdvertViewsViewModel.class);

        recyclerView = view.findViewById(R.id.allAdvertsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new AllAdvertsAdapter(getActivity(),advertViewsRoomArrayList);

        viewsViewModel.showCompany(advertId).observe(getActivity(),advertViewsRooms -> {
            advertViewsRoomArrayList.addAll(advertViewsRooms);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });

        return view;
    }


}
