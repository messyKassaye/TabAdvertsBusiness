package com.example.tabadvertsbusiness.auth.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.adapter.CarCategoryAdapter;
import com.example.tabadvertsbusiness.auth.adapter.ChildrenAdapter;
import com.example.tabadvertsbusiness.auth.dialogs.LoadingDialog;
import com.example.tabadvertsbusiness.auth.model.Car;
import com.example.tabadvertsbusiness.auth.model.CarStore;
import com.example.tabadvertsbusiness.auth.model.Category;
import com.example.tabadvertsbusiness.auth.model.Child;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.utils.ApiResponse;
import com.example.tabadvertsbusiness.auth.view.DownloaderDashboard;
import com.example.tabadvertsbusiness.auth.view.DriverDashboard;
import com.example.tabadvertsbusiness.auth.view_model.CarViewModel;
import com.example.tabadvertsbusiness.auth.view_model.CategoryViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class RegisterNewCar extends Fragment {

    private MaterialButton registerNewCarBtn;
    private LinearLayout firstLayout,newCarRegistrationLayout;
    private RecyclerView carTypeRecyclerview,carCategoryRecyclerView;

    private CategoryViewModel carTypeViewModel;
    private ArrayList<Category> cartypeArrayList = new ArrayList<>();
    private CarCategoryAdapter cartypeAdapter;

    private LinearLayout carCategoryLayout;
    private ArrayList<Child> childArrayList = new ArrayList<>();
    private ChildrenAdapter childrenAdapter;

    private LinearLayout plateInputLayout;
    private EditText plateNUmber;
    private TextView errorShower;
    private Button registerButton;

    private int carCategory = 0;
    private ProgressDialog progressDialog;
    private CarViewModel carViewModel;
    private DriverDashboard driverDashboard;
    private String plateNumber;

    private ProgressBar progressBar;
    private LinearLayout mainLayout;

    public RegisterNewCar(DriverDashboard dashboard) {
        // Required empty public constructor
        driverDashboard = dashboard;
    }

    public RegisterNewCar(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        carTypeViewModel = ViewModelProviders.of(getActivity()).get(CategoryViewModel.class);
        carViewModel = ViewModelProviders.of(getActivity()).get(CarViewModel.class);
        progressDialog = LoadingDialog.loadingDialog(getActivity(),"Registering new car....");
        carViewModel.storeResponse().observe(getActivity(), this::consumeResponse);

        mainLayout = getView().findViewById(R.id.carsMainLayout);
        progressBar = getView().findViewById(R.id.progressBar);

        firstLayout = getView().findViewById(R.id.firstLayout);
        registerNewCarBtn = getView().findViewById(R.id.registerCar);

        plateInputLayout = getView().findViewById(R.id.plateInputLayout);

        newCarRegistrationLayout = getView().findViewById(R.id.carRegistrationLayout);

        registerNewCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegistration();
            }
        });

        carTypeRecyclerview = getView().findViewById(R.id.car_type_recyclerview);
        carTypeRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        carTypeRecyclerview.setItemAnimator(new DefaultItemAnimator());
        cartypeAdapter = new CarCategoryAdapter(getContext(),cartypeArrayList,this);
        carTypeRecyclerview.setAdapter(cartypeAdapter);

        carTypeViewModel.index().observe(this,categoryResponse -> {
            progressBar.setVisibility(View.GONE);
            if (categoryResponse.getData().size()>0) {
                mainLayout.setVisibility(View.VISIBLE);
                cartypeArrayList.addAll(categoryResponse.getData());
                cartypeAdapter.notifyDataSetChanged();
            }
        });

        carCategoryLayout = getView().findViewById(R.id.carCategoryLayout);
        carCategoryLayout.setVisibility(View.VISIBLE);


        carCategoryRecyclerView = getView().findViewById(R.id.car_category_recyclerview);
        carCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        carCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        childrenAdapter = new ChildrenAdapter(getContext(),childArrayList,this);
        carCategoryRecyclerView.setAdapter(childrenAdapter);

        plateNUmber = getView().findViewById(R.id.plateNumber);

        errorShower =getView().findViewById(R.id.carErrorShower);
        registerButton = getView().findViewById(R.id.registerCarButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String plateNumber = plateNUmber.getText().toString();
                if(plateNumber.equals("")){
                    errorShower.setText("Please enter your car plate number");
                }else {
                    CarStore carStore = new CarStore();
                    carStore.setCategory_id(carCategory);
                    carStore.setPlate_number(plateNumber);
                    setPlateNumber(plateNumber);
                    carViewModel.store(carStore);
                }
            }
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_new_car, container, false);
    }



    public void startRegistration(){

        firstLayout.setVisibility(View.GONE);
        newCarRegistrationLayout.setVisibility(View.VISIBLE);
    }

    public void showCarCategory(ArrayList<Child> children){
        childArrayList.addAll(children);
        childrenAdapter.notifyDataSetChanged();

    }

    public void showPlateNumberLayout(int categoryId){
        this.carCategory = categoryId;
        plateInputLayout.setVisibility(View.VISIBLE);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                Toast.makeText(getActivity(),"Something is not good please check your connection ):", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    private void renderSuccessResponse(SuccessResponse response) {
        if(response.isStatus()){
            driverDashboard.showRegisterCarWorkPlaceFragment(response.getCar());
        }
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
}
