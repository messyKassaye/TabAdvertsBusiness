package com.example.tabadvertsbusiness.auth.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.adapter.PlacesAdapter;
import com.example.tabadvertsbusiness.auth.commons.MainDialog;
import com.example.tabadvertsbusiness.auth.dialogs.LoadingDialog;
import com.example.tabadvertsbusiness.auth.model.Address;
import com.example.tabadvertsbusiness.auth.model.Car;
import com.example.tabadvertsbusiness.auth.model.Place;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.services.PlaceService;
import com.example.tabadvertsbusiness.auth.utils.ApiResponse;
import com.example.tabadvertsbusiness.auth.view.DownloaderDashboard;
import com.example.tabadvertsbusiness.auth.view_model.AddressViewModel;
import com.example.tabadvertsbusiness.auth.view_model.CarViewModel;
import com.example.tabadvertsbusiness.auth.view_model.PlacesViewModel;
import com.example.tabadvertsbusiness.auth.view_model.TabletViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;


public class AddressFragment extends Fragment {

    private PlacesViewModel viewModel;

    private RecyclerView recyclerView;
    private PlacesAdapter adapter;
    private ArrayList<Place> arrayList = new ArrayList<>();

    private MaterialButton registerButton;
    private TextView errorTextView;
    private PlaceService placeService;

    private int type;

    private AddressViewModel addressViewModel;
    private ProgressDialog progressDialog;
    private DownloaderDashboard dialog;

    private CarViewModel carViewModel;
    public AddressFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewModel = ViewModelProviders.of(getActivity()).get(PlacesViewModel.class);
        carViewModel = ViewModelProviders.of(getActivity()).get(CarViewModel.class);

        recyclerView = getView().findViewById(R.id.places_recyler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new PlacesAdapter(getActivity(),arrayList);
        recyclerView.setAdapter(adapter);

        viewModel.index().observe(this,placesResponse -> {
            if(placesResponse!=null){
                List<Place> list = placesResponse.getData();
                if(list.size()>0){
                    arrayList.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            }else {
                System.out.println("There is no data");
            }
        });

        addressViewModel = ViewModelProviders.of(getActivity()).get(AddressViewModel.class);
        progressDialog = LoadingDialog.loadingDialog(getActivity(),"Assigning....");
        addressViewModel.storeResponse().observe(getActivity(), this::consumeResponse);

        dialog = new DownloaderDashboard();
        placeService = new PlaceService(getActivity());

        type = placeService.getType();


        errorTextView = (TextView)getView().findViewById(R.id.my_place_error);
        registerButton = (MaterialButton)getView().findViewById(R.id.register_my_place);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (type){
                    case 1:
                        Address address = new Address();
                        address.setPlace_id(placeService.getPlaceId());
                        addressViewModel.store(address);

                    case 2:
                        System.out.println("Car");
                        Car car = new Car();
                        car.setId(placeService.getCarId());
                        car.setPlace_id(placeService.getPlaceId());
                        carViewModel.update(car,placeService.getCarId());
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_address, container, false);
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
          DownloaderDashboard dashboard = (DownloaderDashboard) getActivity();
          dashboard.closeDialog();
        }
    }
}
