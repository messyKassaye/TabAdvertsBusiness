package com.example.tabadvertsbusiness.auth.view.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.adapter.CarWorkPlaceAdapter;
import com.example.tabadvertsbusiness.auth.dialogs.LoadingDialog;
import com.example.tabadvertsbusiness.auth.model.Car;
import com.example.tabadvertsbusiness.auth.model.CarWorkPlace;
import com.example.tabadvertsbusiness.auth.model.Place;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.utils.ApiResponse;
import com.example.tabadvertsbusiness.auth.view.DriverDashboard;
import com.example.tabadvertsbusiness.auth.view_model.CarViewModel;
import com.example.tabadvertsbusiness.auth.view_model.CarWorkPlaceVIewModel;
import com.example.tabadvertsbusiness.auth.view_model.PlacesViewModel;

import java.util.ArrayList;


public class RegisterCarWorkPlace extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private RecyclerView recyclerView;
    private Button setWorkPlaceButton;
    private PlacesViewModel placesViewModel;
    private ArrayList<Place> arrayList = new ArrayList<>();
    private CarWorkPlaceAdapter adapter;
    private CarWorkPlaceVIewModel carViewModel;

    private Car car;
    private int placeId;
    private ProgressDialog progressDialog;
    public RegisterCarWorkPlace(Car car) {
        // Required empty public constructor
        this.car = car;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_register_car_work_place, container, false);

        carViewModel = ViewModelProviders.of(getActivity()).get(CarWorkPlaceVIewModel.class);
        progressDialog = LoadingDialog.loadingDialog(getActivity(), "Sending your data....");
        carViewModel.storeResponse().observe(getActivity(), this::consumeResponse);

        adapter = new CarWorkPlaceAdapter(getContext(),this,arrayList);
        recyclerView = view.findViewById(R.id.carWorkPlacesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        setWorkPlaceButton = view.findViewById(R.id.setCarWorkPlace);

        placesViewModel = ViewModelProviders.of(getActivity()).get(PlacesViewModel.class);
        placesViewModel.index().observe(getActivity(),placesResponse -> {
            if (placesResponse.getData().size()>0){
                arrayList.addAll(placesResponse.getData());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        setWorkPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarWorkPlace cars = new CarWorkPlace();
                cars.setCar_id(car.getId());
                cars.setPlace_id(placeId);
                cars.setPlate_number(car.getPlate_number());
                carViewModel.store(cars);
            }
        });

        return view;
    }


    public void showRegisterButton(int placeId){
        this.placeId = placeId;
        getView().findViewById(R.id.setCarWorkPlace).setVisibility(View.VISIBLE);
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
                Toast.makeText(getActivity(),"Something is not Good ):", Toast.LENGTH_SHORT).show();
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
            DriverDashboard dashboard = (DriverDashboard)getActivity();
            dashboard.showHome();
        }
    }

}
