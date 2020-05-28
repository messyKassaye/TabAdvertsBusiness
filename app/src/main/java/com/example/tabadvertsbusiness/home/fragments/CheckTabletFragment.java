package com.example.tabadvertsbusiness.home.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.model.Tablet;
import com.example.tabadvertsbusiness.auth.response.TabletResponse;
import com.example.tabadvertsbusiness.auth.view.DriverDashboard;
import com.example.tabadvertsbusiness.auth.view_model.MeViewModel;
import com.example.tabadvertsbusiness.auth.view_model.TabletViewModel;
import com.example.tabadvertsbusiness.constants.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CheckTabletFragment extends Fragment {


    private RelativeLayout checkLayout;
    private ProgressBar progressBar;
    private TextView textViewInfo;
    private Button exit;
    private TabletViewModel tabletViewModel;
    private MeViewModel meViewModel;

    public CheckTabletFragment() {
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
        View view= inflater.inflate(R.layout.fragment_check_tablet, container, false);

        progressBar = view.findViewById(R.id.checkPr);
        textViewInfo = view.findViewById(R.id.checkingInfo);
        exit = view.findViewById(R.id.checkingExit);

        tabletViewModel = ViewModelProviders.of(this).get(TabletViewModel.class);
        meViewModel = ViewModelProviders.of(this).get(MeViewModel.class);
        String serail_number = Build.SERIAL;
        tabletViewModel.show(serail_number).enqueue(new Callback<TabletResponse>() {
            @Override
            public void onResponse(Call<TabletResponse> call, Response<TabletResponse> response) {
                if (response.body().getData().size()<=0){
                    progressBar.setVisibility(View.GONE);
                    showDriverDashboard();
                }else {
                    checkOwnerOfThisTablet(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<TabletResponse> call, Throwable t) {

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearToken();
                getActivity().finish();
            }
        });
        return view;
    }

    public void showDriverDashboard(){
        Intent intent = new Intent(getContext(), DriverDashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void clearToken(){
        SharedPreferences preferences =getActivity().getSharedPreferences(Constants.getTokenPrefence(), 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public void checkOwnerOfThisTablet(List<Tablet> tablets){
        meViewModel.me().observe(this,meResponse -> {
            progressBar.setVisibility(View.GONE);
            if (meResponse.getData().getAttribute().getId()==tablets.get(0).getUser_id()){
                showDriverDashboard();
            }else {
                textViewInfo.setTextColor(Color.RED);
                textViewInfo.setText("Sorry. This tablet is assigned for some one else not for you.");
                exit.setVisibility(View.VISIBLE);
                clearToken();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearToken();
                getActivity().finish();
            }
        });
    }



}
