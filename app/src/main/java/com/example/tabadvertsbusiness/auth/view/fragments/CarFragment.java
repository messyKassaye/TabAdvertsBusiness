package com.example.tabadvertsbusiness.auth.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.adapter.CarsAdapter;
import com.example.tabadvertsbusiness.auth.adapter.ShimmerRecyclerViewAdapter;
import com.example.tabadvertsbusiness.auth.helpers.GridSpacingItemDecoration;
import com.example.tabadvertsbusiness.auth.model.Car;
import com.example.tabadvertsbusiness.auth.view.DriverDashboard;
import com.example.tabadvertsbusiness.auth.view_model.MeViewModel;
import java.util.ArrayList;
import java.util.List;
import io.supercharge.shimmerlayout.ShimmerLayout;



public class CarFragment extends Fragment {

    private MeViewModel viewModel;
    private RecyclerView recyclerView;
    private ArrayList<Car> arrayList = new ArrayList<>();
    private CarsAdapter adapter;

    //shimmer pre-loading
    private RecyclerView skeletonLayout;
    private ShimmerLayout shimmer;
    private ShimmerRecyclerViewAdapter shimmerRecyclerViewAdapter;
    private ArrayList<Car> shimmerList = new ArrayList<>();

    //
    public FrameLayout frameLayout;
    public LayoutInflater inflater;
    private List<Car> carList;

    private LinearLayout noCarLayout;
    private Button registerCarButton;


    public CarFragment() {
        // Required empty public constructor
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //pre loading for shimmer
        shimmer = getView().findViewById(R.id.shimmerSkeleton);
        skeletonLayout = getView().findViewById(R.id.skeletonLayout);
        skeletonLayout.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        skeletonLayout.setItemAnimator(new DefaultItemAnimator());
        shimmerRecyclerViewAdapter = new ShimmerRecyclerViewAdapter(getActivity(),shimmerList);
        this.showSkeleton(true);



        noCarLayout = getView().findViewById(R.id.noCarLayout);
        registerCarButton = getView().findViewById(R.id.registerNewCars);

        //cars recycler view
        recyclerView = (RecyclerView)getView().findViewById(R.id.cars_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, GridSpacingItemDecoration.dpToPx(10,getContext()), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //recycler view adapter
        adapter = new CarsAdapter(getActivity(),arrayList);
        recyclerView.setAdapter(adapter);


        viewModel = ViewModelProviders.of(getActivity()).get(MeViewModel.class);
        viewModel.me().observe(getActivity(),meResponse -> {
            if(meResponse!=null){
                this.showSkeleton(false);
                carList = meResponse.getData().getRelations().getCars();
                if(carList.size()>0){
                    arrayList.addAll(carList);
                    adapter.notifyDataSetChanged();
                }else {
                    noCarLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }
        });

        registerCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverDashboard dashboard = (DriverDashboard)getContext();
                dashboard.showCarRegisterationFragment();
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car, container, false);
    }




    public int getSkeletonRowCount(Context context) {
        int pxHeight = getDeviceHeight(context);
        int skeletonRowHeight = (int) getResources()
                .getDimension(R.dimen.row_layout_height); //converts to pixel
        return (int) Math.ceil(pxHeight / skeletonRowHeight);
    }
    public int getDeviceHeight(Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return metrics.heightPixels;
    }


    public void showSkeleton(boolean show) {

        if (show) {

            skeletonLayout.setAdapter(shimmerRecyclerViewAdapter);
            shimmerRecyclerViewAdapter.notifyDataSetChanged();
            shimmer.setVisibility(View.VISIBLE);
        } else {
            shimmer.setVisibility(View.GONE);
        }
    }


}
