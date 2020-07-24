package com.example.tabadvertsbusiness.home.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.tabadvertsbusiness.R;

public class HowItWorksFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private WebView webView;
    private ProgressBar progressBar;
    public HowItWorksFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_how_it_works, container, false);

        webView = view.findViewById(R.id.howItWorksWebview);
        progressBar = view.findViewById(R.id.progressBar);

        String message = "<div>" +
                "<span style='font-size:1.1em;color:#1976d2'>How Ride ads work</span>"+
                "<p style='color:#242424;text-align: justify;line-height:1.2em;'>Smart display is an advertising company. "+
                "We have prepared an advertising platform for companies and for transportation service owners like " +
                "taxi or buses.</p>"+
                "<p style='color:#242424;text-align: justify;line-height:1.2em;'>Our platform contains " +
                "Web based and Tablet based applications to play different companies product on taxi and buses.</p>"+
                "<p style='color:#242424;text-align: justify;line-height:1.2em;'>Companies upload their " +
                "advertising video, audio and images into our server then drivers will download this " +
                "video using the prepared tablet application and play it to their passengers. When drivers " +
                "or car owners play companies advertising media they will be payed 60% of the media price.</P>"+
                "<p></p>"+
                "<p style='text-align: justify;color:#242424;'>Start advertising your product on our platform and make your company more productive.</P>"+
                "<p style='text-align: justify;color:#242424;'>Start adverting on your car and increase your daily income</p>"+
                "<p style='text-align: center;color:#FF8C00;'>Start adverting, start playing now</p>"+
                "</div>";
        String html = "<html><head></head><body>"+message+"</body></html>";
        webView.loadDataWithBaseURL("", html, "text/html", "utf-8", "");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);

            }
        },1000);
        return view;
    }


}
