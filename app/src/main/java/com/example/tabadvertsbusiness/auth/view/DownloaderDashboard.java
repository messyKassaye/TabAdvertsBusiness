package com.example.tabadvertsbusiness.auth.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.commons.MainDialog;
import com.example.tabadvertsbusiness.auth.view.downloaderView.DownloadHistoryFragment;
import com.example.tabadvertsbusiness.auth.view.downloaderView.NewDownloadFragment;
import com.example.tabadvertsbusiness.auth.view.downloaderView.TotalDownloadSizeFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.AddressFragment;
import com.example.tabadvertsbusiness.auth.view_model.MeViewModel;
import com.google.android.material.button.MaterialButton;

public class DownloaderDashboard extends AppCompatActivity implements
        NewDownloadFragment.OnFragmentInteractionListener,
        TotalDownloadSizeFragment.OnFragmentInteractionListener,
        DownloadHistoryFragment.OnFragmentInteractionListener {
    MeViewModel viewModel;
    Toolbar toolbar,dialogToolbar;
    LinearLayout mainLayout,workingPlaceLayout;
    String firstname;
    TextView setPlaceInfo;
    MaterialButton setMyPlaceBtn;
    MainDialog dialog;

    GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader_dashboard);

         toolbar = findViewById(R.id.downloader_toolbar);
         toolbar.setTitleTextColor(Color.WHITE);
         toolbar.setTitle("Dashboard");
         setSupportActionBar(toolbar);

         //initialize ui
        mainLayout = findViewById(R.id.mainLayout);
        workingPlaceLayout = findViewById(R.id.workingCardPlaceLayout);


        setPlaceInfo = findViewById(R.id.set_place_info);


        gridLayout = findViewById(R.id.downloaderGridLayout);
        gridLayout.setColumnCount(getColumnCount());
        if(getColumnCount()>=12){
            gridLayout.setOrientation(GridLayout.HORIZONTAL);
        }else {
            gridLayout.setOrientation(GridLayout.VERTICAL);
        }

        //view model initialization
        this.init();

        viewModel.me().observe(this,meResponse->{
            if(meResponse!=null){
                firstname = meResponse.getData().getAttribute().getFirst_name();
                toolbar.setTitle(meResponse.getData().getAttribute().getFirst_name()+" "
                        +meResponse.getData().getAttribute().getLast_name()+" | Downloader");

                //check if this driver sets his working places
                if(meResponse.getData().getRelations().getPlace().size()<=0){
                    workingPlaceLayout.setVisibility(View.VISIBLE);
                    setPlaceInfo.setText("Hello, "+firstname+"\n Thank you for being our family " +
                            "\nYou are registered as file downloader of our company. In order to download file and sell to drivers you have to set your location or your address.\n" +
                            "Please set your address and start earning more income\n.");
                 setMyPlaceBtn = findViewById(R.id.setPlaceBtn);
                 setMyPlaceBtn.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         setPlace();
                     }
                 });
                }else {
                    mainLayout.setVisibility(View.VISIBLE);
                }
            }
        });



    }

    public void init(){
        viewModel = ViewModelProviders.of(this).get(MeViewModel.class);
        dialog = new MainDialog();

    }


    //finding column count for each devices
    public int getColumnCount(){
        float scalefactor = getResources().getDisplayMetrics().density * 100;
        int width = getWindowManager().getDefaultDisplay().getWidth();
        int columns = (int) ((float)width / (float) scalefactor);

        return columns;
    }


    public void setPlace(){
       dialog.display(getSupportFragmentManager(),"Set your address",R.layout.set_address_dialog_layout);
    }

    public void closeDialog(){
         this.dialog.dismiss();
         workingPlaceLayout.setVisibility(View.GONE);
         mainLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
