package com.example.tabadvertsbusiness.auth.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.view.DriverDashboard;

public class DownloadRequestSubmittedFragment extends Fragment {

    private String message;
    private TextView textView;
    private Button backToHome;
    public DownloadRequestSubmittedFragment(String message) {
        // Required empty public constructor
        this.message = message;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_download_request_submitted, container, false);

        textView = view.findViewById(R.id.requestSubmittedInfo);
        textView.setText(message);

        backToHome = view.findViewById(R.id.backToHome);
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverDashboard dashboard = (DriverDashboard)getContext();
                dashboard.showHome();
            }
        });
        return view;
    }


}
