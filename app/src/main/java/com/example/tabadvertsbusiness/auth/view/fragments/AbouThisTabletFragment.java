package com.example.tabadvertsbusiness.auth.view.fragments;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.commons.Helpers;
import com.example.tabadvertsbusiness.auth.commons.MainDialog;
import com.example.tabadvertsbusiness.auth.model.Car;
import com.example.tabadvertsbusiness.auth.response.TabletResponse;
import com.example.tabadvertsbusiness.auth.services.PlaceService;
import com.example.tabadvertsbusiness.auth.view_model.TabletViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AbouThisTabletFragment extends Fragment {

    private Response<TabletResponse> tabletResponseResponse;

    private  TextView plate_number;
    private  TextView carImage;
    private  TextView carType;
    private  TextView workingPlace;
    private  Button setWorkingPlace;
    private  Button assignButton;

    private TextView assignedText,workPlaceInfo;

    private MainDialog mainDialog;
    private CardView carsCard;
    private TabletViewModel tabletViewModel;

    private LinearLayout assignedLayout,tabletNotAssingedLayout;
    private ProgressBar progressBar;
    private PlaceService placeService;
    public AbouThisTabletFragment() {

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
        return inflater.inflate(R.layout.fragment_abou_this_tablet, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assignedLayout = getView().findViewById(R.id.assignedTabletLayout);
        tabletNotAssingedLayout = getView().findViewById(R.id.tablet_not_assigned_layout);

        placeService = new PlaceService(getContext());

        progressBar = getView().findViewById(R.id.aboutBar);

        tabletViewModel = ViewModelProviders.of(getActivity()).get(TabletViewModel.class);
        String serial_number = Build.SERIAL;
        tabletViewModel.show(serial_number).enqueue(new Callback<TabletResponse>() {
            @Override
            public void onResponse(Call<TabletResponse> call, Response<TabletResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.body().getData().size()<=0){
                    tabletNotAssingedLayout.setVisibility(View.VISIBLE);
                }else {
                    assignTablet(response);
                    assignedLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TabletResponse> call, Throwable t) {

            }
        });

    }

    public void assignTablet(Response<TabletResponse> response){
        tabletResponseResponse = response;
        carsCard = getView().findViewById(R.id.cars_card);
        carsCard.setLayoutParams(new LinearLayout.LayoutParams(Helpers.deviceWidth((AppCompatActivity)getContext()), LinearLayout.LayoutParams.WRAP_CONTENT));
        plate_number=getView().findViewById(R.id.plate_number);
        carImage = getView().findViewById(R.id.taxi_image);
        carType = getView().findViewById(R.id.car_type);

        workingPlace = getView().findViewById(R.id.working_place);
        setWorkingPlace = getView().findViewById(R.id.set_working_place_button);

        assignedText = getView().findViewById(R.id.assigned_text);
        assignButton = getView().findViewById(R.id.assign_button);
        assignButton.setVisibility(View.GONE);

        Car car = tabletResponseResponse.body().getData().get(0).getCars();
        plate_number.setText("Plate number: "+car.getPlate_number());
        carImage.setText(String.valueOf(car.getCar_category().get(0).getName().charAt(0)));
        carType.setText(car.getCar_category().get(0).getName());
        assignedText.setText("I'm assigned to this tablet");
        assignedText.setTextColor(Color.GREEN);

        workPlaceInfo = getView().findViewById(R.id.workPlaceInfo);

        if(car.getWorking_place().size()>0){
            workingPlace.setText("Work place: "+car.getWorking_place().get(0).getCity());
        }else {
            workingPlace.setText("Work place: ");
            workPlaceInfo.setVisibility(View.VISIBLE);
            setWorkingPlace.setVisibility(View.VISIBLE);
            workPlaceInfo.setText(R.string.workinPlaceInfo);
        }

        mainDialog = new MainDialog();

        setWorkingPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeService.setType(2);
                mainDialog.display(
                        getActivity().getSupportFragmentManager(),
                        "Set car working place",
                        R.layout.set_address_dialog_layout);
            }
        });
    }

}
