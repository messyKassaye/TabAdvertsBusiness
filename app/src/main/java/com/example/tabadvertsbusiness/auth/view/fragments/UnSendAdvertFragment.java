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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.commons.Helpers;
import com.example.tabadvertsbusiness.auth.dialogs.LoadingDialog;
import com.example.tabadvertsbusiness.auth.model.AdvertViewSendObject;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.response.TabletResponse;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertRoomVIewModel;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertViewsViewModel;
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

    private TabletViewModel tabletViewModel;
    private AdvertViewRetrofitViewModel retrofitViewModel;
    private ProgressDialog progressDialog;



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
        tabletViewModel = ViewModelProviders.of(getActivity()).get(TabletViewModel.class);
        retrofitViewModel = ViewModelProviders.of(getActivity()).get(AdvertViewRetrofitViewModel.class);

        try{
            mainJSON = new JSONArray();
        }catch (Exception e){}


        advertCard = getView().findViewById(R.id.advertCard);
        advertCard.setCardBackgroundColor(Color.parseColor("#2B2B2B"));
        int columns = Helpers.getColumn((AppCompatActivity)getContext());
        if(columns<12){
            advertCard.setLayoutParams(new LinearLayout.LayoutParams(
                    Helpers.deviceWidth((AppCompatActivity)getContext()),
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
        }

        mainLayout = getView().findViewById(R.id.advertContentLayout);
        noAdvertLayout = getView().findViewById(R.id.noAdvertIsFound);

        headerTitle = getView().findViewById(R.id.headerTitle);
        headerTitle.setText(R.string.unsend_adverts);

        totalAdvert= getView().findViewById(R.id.totalAdvert);

        showAll = getView().findViewById(R.id.showUnsendAdvert);
        sendAll = getView().findViewById(R.id.sendUnsendAdvert);

        sendAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = LoadingDialog.loadingDialog(getActivity(), "Sending your data....");
                retrofitViewModel.storeResponse().observe(getActivity(), UnSendAdvertFragment.this::consumeResponse);
                if (allAdverts.size()<=0){
                    Toast.makeText(getActivity(),"There is no data to send.",Toast.LENGTH_LONG).show();
                }else {
                    progressDialog.show();
                    String serial_number = Build.SERIAL;
                    tabletViewModel.show(serial_number).enqueue(new Callback<TabletResponse>() {
                        @Override
                        public void onResponse(Call<TabletResponse> call, Response<TabletResponse> response) {
                            int card_id = response.body().getData().get(0).getCar_id();
                            try {
                                for (int i = 0; i < allAdverts.size(); i++) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("car_id", card_id);
                                    jsonObject.put("advert_id", allAdverts.get(i).getAdvertId());
                                    jsonObject.put("advert_time", allAdverts.get(i).getAdvertTime());
                                    jsonObject.put("number_of_viewers", allAdverts.get(i).getNumberOfViewers());
                                    jsonObject.put("picture", allAdverts.get(i).getPicture());
                                    mainJSON.put(jsonObject);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            AdvertViewSendObject advertViewSendObject = new AdvertViewSendObject();
                            advertViewSendObject.setData(mainJSON.toString());

                            System.out.println("Data: "+mainJSON.toString());
                            retrofitViewModel.store(advertViewSendObject);
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


    private void consumeResponse(ApiResponse apiResponse) {

        switch (apiResponse.status) {

            case LOADING:
                progressDialog.show();
                break;

            case SUCCESS:
                progressDialog.dismiss();
                renderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                progressDialog.dismiss();
                break;

            default:
                break;
        }
    }

    /*
     * method to handle success response
     * */
    private void renderSuccessResponse(SuccessResponse response) {
        if(response.isStatus()){
            updateAdvertView(allAdverts);
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

}
