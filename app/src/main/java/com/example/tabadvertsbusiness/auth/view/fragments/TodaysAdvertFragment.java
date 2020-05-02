package com.example.tabadvertsbusiness.auth.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.commons.Helpers;
import com.example.tabadvertsbusiness.auth.dialogs.LoadingDialog;
import com.example.tabadvertsbusiness.auth.model.AdvertView;
import com.example.tabadvertsbusiness.auth.model.AdvertViewSendObject;
import com.example.tabadvertsbusiness.auth.repository.AdvertViewRepository;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.response.TabletResponse;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
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

public class TodaysAdvertFragment extends Fragment {

    private CardView advertCard;
    private TextView headerTitle;
    private TextView totalAdvert;
    private AdvertViewsViewModel viewsViewModel;
    private ArrayList<AdvertViewsRoom> todayAdvertData = new ArrayList<>();
    private Button showAll, sendAll;
    private AdvertViewRetrofitViewModel retrofitViewModel;
    private ProgressDialog progressDialog;
    private JSONArray mainJSON;

    private TabletViewModel tabletViewModel;

    public TodaysAdvertFragment() {
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
        return inflater.inflate(R.layout.fragment_todays_advert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //find car id of this tablet
        tabletViewModel = ViewModelProviders.of(getActivity()).get(TabletViewModel.class);


        try{
            mainJSON = new JSONArray();
        }catch (Exception e){}
        advertCard = getView().findViewById(R.id.advertCard);
        advertCard.setCardBackgroundColor(Color.parseColor("#2E8B57"));

        showAll = getView().findViewById(R.id.showAll);
        sendAll = getView().findViewById(R.id.sendAll);

        headerTitle = getView().findViewById(R.id.headerTitle);
        headerTitle.setText(R.string.today_advert);
        totalAdvert = getView().findViewById(R.id.totalAdvert);

        retrofitViewModel = ViewModelProviders.of(getActivity()).get(AdvertViewRetrofitViewModel.class);
        progressDialog = LoadingDialog.loadingDialog(getActivity(), "Sending your data....");
        retrofitViewModel.storeResponse().observe(getActivity(), this::consumeResponse);

        displayAdvertData();

        sendAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (todayAdvertData.size()<=0){
                    Toast.makeText(getActivity(),"There is no data to send.",Toast.LENGTH_LONG).show();
                }else {
                    progressDialog.show();
                    String serial_number = Build.SERIAL;
                    System.out.println("Serial: " + serial_number);
                    tabletViewModel.show(serial_number).enqueue(new Callback<TabletResponse>() {
                        @Override
                        public void onResponse(Call<TabletResponse> call, Response<TabletResponse> response) {
                            int card_id = response.body().getData().get(0).getCar_id();
                            try {
                                for (int i = 0; i < todayAdvertData.size(); i++) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("car_id", card_id);
                                    jsonObject.put("advert_id", todayAdvertData.get(i).getAdvertId());
                                    jsonObject.put("advert_time", todayAdvertData.get(i).getAdvertTime());
                                    jsonObject.put("number_of_viewers", todayAdvertData.get(i).getNumberOfViewers());
                                    jsonObject.put("picture", todayAdvertData.get(i).getPicture());
                                    mainJSON.put(jsonObject);
                                }
                                System.out.println("Object: " + mainJSON.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            AdvertViewSendObject advertViewSendObject = new AdvertViewSendObject();
                            advertViewSendObject.setData(mainJSON.toString());
                            retrofitViewModel.store(advertViewSendObject);
                        }

                        @Override
                        public void onFailure(Call<TabletResponse> call, Throwable t) {

                        }
                    });
                }

            }
        });


    }

    public void displayAdvertData() {
        viewsViewModel = ViewModelProviders.of(getActivity()).get(AdvertViewsViewModel.class);
        viewsViewModel.index().observe(getActivity(), advertViewsRooms -> {
            String date = Constants.currentDate();
            String currentDate = date.substring(0, date.lastIndexOf(" "));
            for (int i = 0; i < advertViewsRooms.size(); i++) {
                String advertTime = advertViewsRooms.get(i).getAdvertTime();
                String advertDate = advertTime.substring(0, advertTime.lastIndexOf(" "));
                if (currentDate.equals(advertDate) && !advertViewsRooms.get(i).isSend()) {
                    todayAdvertData.add(advertViewsRooms.get(i));
                }
                System.out.println("Status: "+advertViewsRooms.get(i).isSend());
            }
            totalAdvert.setText("" + todayAdvertData.size());
        });
    }


    private void consumeResponse(ApiResponse apiResponse) {

        switch (apiResponse.status) {

            case LOADING:
                progressDialog.show();
                break;

            case SUCCESS:
                progressDialog.dismiss();
                updateAdvertView(todayAdvertData);
                renderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT).show();
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
            totalAdvert.setText("0");
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
