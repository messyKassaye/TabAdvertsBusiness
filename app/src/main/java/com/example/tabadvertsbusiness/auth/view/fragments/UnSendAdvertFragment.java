package com.example.tabadvertsbusiness.auth.view.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.commons.Helpers;
import com.example.tabadvertsbusiness.auth.dialogs.LoadingDialog;
import com.example.tabadvertsbusiness.auth.model.AdvertView;
import com.example.tabadvertsbusiness.auth.model.AdvertViewObject;
import com.example.tabadvertsbusiness.auth.model.AdvertViewSendObject;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.response.TabletResponse;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertRoomVIewModel;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertViewsViewModel;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.TabletAssignViewModel;
import com.example.tabadvertsbusiness.auth.utils.ApiResponse;
import com.example.tabadvertsbusiness.auth.view_model.AdvertViewRetrofitViewModel;
import com.example.tabadvertsbusiness.auth.view_model.TabletViewModel;
import com.example.tabadvertsbusiness.constants.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UnSendAdvertFragment extends Fragment {


    private CardView advertCard;
    private TextView headerTitle;
    private TextView totalAdvert;
    private AdvertViewsViewModel viewModel;
    private ArrayList<AdvertViewsRoom> yesterdayAdvert;
    private AdvertViewsViewModel viewsViewModel;
    private ArrayList<AdvertViewsRoom> yesterdayAdvertData = new ArrayList<>();

    private ArrayList<AdvertViewsRoom> allAdverts = new ArrayList<>();
    private JSONArray mainJSON;

    private LinearLayout mainLayout,noAdvertLayout;
    private Button showAll,sendAll;
    private LinearLayout advertContentLayout;
    private LinearLayout progressLayout;
    private ProgressBar progressBar;
    private TextView progressValue;
    private TabletViewModel tabletViewModel;
    private AdvertViewRetrofitViewModel retrofitViewModel;
    private int progressStatus = 1;
    private TabletAssignViewModel tabletAssignViewModel;



    public UnSendAdvertFragment() {
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
        return inflater.inflate(R.layout.fragment_unsend_advert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(AdvertViewsViewModel.class);

        tabletAssignViewModel = ViewModelProviders.of(this).get(TabletAssignViewModel.class);
        tabletViewModel = ViewModelProviders.of(getActivity()).get(TabletViewModel.class);
        retrofitViewModel = ViewModelProviders.of(getActivity()).get(AdvertViewRetrofitViewModel.class);

        try{
            mainJSON = new JSONArray();
        }catch (Exception e){}


        advertCard = getView().findViewById(R.id.advertCard);
        advertCard.setCardBackgroundColor(Color.parseColor("#2B2B2B"));


        mainLayout = getView().findViewById(R.id.advertContentLayout);
        noAdvertLayout = getView().findViewById(R.id.noAdvertIsFound);

        headerTitle = getView().findViewById(R.id.headerTitle);
        headerTitle.setText(R.string.unsend_adverts);

        totalAdvert= getView().findViewById(R.id.totalAdvert);

        advertContentLayout = getView().findViewById(R.id.advertContentLayout);
        showAll = getView().findViewById(R.id.showUnsendAdvert);
        sendAll = getView().findViewById(R.id.sendUnsendAdvert);
        //percentage layout
        progressLayout = getView().findViewById(R.id.progressShowerLayout);
        progressBar = getView().findViewById(R.id.progressPercentage);
        progressValue = getView().findViewById(R.id.percentageValue);


        sendAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofitViewModel.storeResponse().observe(getActivity(), UnSendAdvertFragment.this::consumeResponse);
                advertContentLayout.setVisibility(View.GONE);
                progressLayout.setVisibility(View.VISIBLE);
                progressValue.setText("Sending data... 0%");
                System.out.println("AdvertSize: "+allAdverts.size());

                if (allAdverts.size()<=0){
                    Toast.makeText(getActivity(),"There is no data to send.",Toast.LENGTH_LONG).show();
                }else {
                    String serial_number = Build.SERIAL;
                    tabletViewModel.show(serial_number)
                            .enqueue(new Callback<TabletResponse>() {
                                @Override
                                public void onResponse(Call<TabletResponse> call, Response<TabletResponse> response) {
                                    if (response.body().getData().size()>0){
                                        sendRecursively(response.body().getData().get(0).getCar_id(),0);
                                    }
                                }

                                @Override
                                public void onFailure(Call<TabletResponse> call, Throwable t) {

                                }
                            });
                }


            }
        });
        displayAdvertData();
    }


    public void displayAdvertData() {
        viewsViewModel = ViewModelProviders.of(getActivity()).get(AdvertViewsViewModel.class);
        viewsViewModel.showUnsendAdvert(false).observe(getActivity(), advertViewsRooms -> {

            allAdverts.addAll(advertViewsRooms);
            totalAdvert.setText("" + advertViewsRooms.size());

            if (allAdverts.size()<=0){
                mainLayout.setVisibility(View.GONE);
                noAdvertLayout.setVisibility(View.VISIBLE);
            }else {
                mainLayout.setVisibility(View.VISIBLE);
                noAdvertLayout.setVisibility(View.GONE);
                sendAll.setVisibility(View.VISIBLE);
            }
        });

    }

    public int getPerSendPercentage(){
        return Math.round(100/allAdverts.size());
    }

    private void consumeResponse(ApiResponse apiResponse) {

        switch (apiResponse.status) {

            case LOADING:
                break;

            case SUCCESS:
                //progressDialog.dismiss();
                renderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                break;

            default:
                break;
        }
    }

    public void sendRecursively(int car_id,int index){
        AdvertViewsRoom advertViewsRoom = allAdverts.get(index);

        AdvertViewObject viewObject = new AdvertViewObject();
        viewObject.setIndex(index);
        viewObject.setCar_id(car_id);
        viewObject.setAdvert_id(advertViewsRoom.getAdvertId());
        viewObject.setAdvert_time(advertViewsRoom.getAdvertTime());
        viewObject.setNumber_of_viewers(advertViewsRoom.getNumberOfViewers());
        viewObject.setPicture(advertViewsRoom.getPicture());
        retrofitViewModel.storeView(viewObject);

    }
    private void renderSuccessResponse(SuccessResponse response) {
        if(response.isStatus()&&response.getIndex()<=allAdverts.size()){
            AdvertViewsRoom advertViewsRoom = allAdverts.get(response.getIndex()-1);
            updateAdvert(advertViewsRoom);
            sendRecursively(response.getCar_id(),response.getIndex());
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    progressValue.setText("Sending data... "+response.getIndex()+"%");
                }
            });

        }
        if (response.getIndex()==allAdverts.size()-1){
            progressBar.setVisibility(View.GONE);
            totalAdvert.setText("0");
            progressValue.setText("Completed");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressLayout.setVisibility(View.GONE);
                    //updateAdvertView(allAdverts);
                }
            }, 3000);
        }
    }


    public void updateAdvertView(List<AdvertViewsRoom> advertViews){
        totalAdvert.setText("0");
        TabletAdsRoomDatabase db = TabletAdsRoomDatabase.getDatabase(getContext());
        for (int i=0;i<advertViews.size();i++){
            AdvertViewsRoom advertViewsRoom = new AdvertViewsRoom();
            advertViewsRoom.setSend(true);
            advertViewsRoom.setId(advertViews.get(i).getId());
            advertViewsRoom.setAdvertId(advertViews.get(i).getAdvertId());
            advertViewsRoom.setNumberOfViewers(advertViews.get(i).getNumberOfViewers());
            advertViewsRoom.setPicture(advertViews.get(i).getPicture());
            advertViewsRoom.setAdvertTime(advertViews.get(i).getAdvertTime());
            viewsViewModel.store(advertViewsRoom);
        }
    }

    public void updateAdvert(AdvertViewsRoom advertViews){
        AdvertViewsRoom advertViewsRoom = new AdvertViewsRoom();
        advertViewsRoom.setSend(true);
        advertViewsRoom.setId(advertViews.getId());
        advertViewsRoom.setAdvertId(advertViews.getAdvertId());
        advertViewsRoom.setNumberOfViewers(advertViews.getNumberOfViewers());
        advertViewsRoom.setPicture("");
        advertViewsRoom.setAdvertTime(advertViews.getAdvertTime());
        viewsViewModel.store(advertViewsRoom);
    }

}