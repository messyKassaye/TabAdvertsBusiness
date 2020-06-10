package com.example.tabadvertsbusiness.auth.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.adapter.CarsAdapter;
import com.example.tabadvertsbusiness.auth.model.Car;
import com.example.tabadvertsbusiness.auth.response.CarResponse;
import com.example.tabadvertsbusiness.auth.view.DriverDashboard;
import com.example.tabadvertsbusiness.auth.view_model.CarViewModel;
import com.example.tabadvertsbusiness.auth.view_model.MeViewModel;

import org.xmlpull.v1.sax2.Driver;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabletNotAssignedToCarFragment extends Fragment {

    private WebView webView;
    private MeViewModel viewModel;
    private String firstName;
    private List<Car> carList;

    private LinearLayout registerCarLayout;
    private TextView registerCarInfo;
    private Button registerCar;
    private CarsAdapter carsAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Car> carArrayList = new ArrayList<>();
    private ProgressBar progressBar;
    private LinearLayout mainLayout;
    private CarViewModel carViewModel;
    public TabletNotAssignedToCarFragment() {
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
        View view = inflater.inflate(R.layout.fragment_tablet_not_assigned_to_car, container, false);

        viewModel = ViewModelProviders.of(getActivity()).get(MeViewModel.class);
        viewModel.me().observe(getActivity(),meResponse -> {
            if (meResponse!=null){
                firstName = meResponse.getData().getAttribute().getFirst_name();
            }
        });

        progressBar = view.findViewById(R.id.tabletNotAssignedPr);
        mainLayout = view.findViewById(R.id.tabletNotAssignedMainLayout);

        carViewModel = ViewModelProviders.of(getActivity()).get(CarViewModel.class);
        carViewModel.index().enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                progressBar.setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);
                if (response.body().getData().size()>0){
                    carList = response.body().getData();
                    showView(getView());
                }
            }

            @Override
            public void onFailure(Call<CarResponse> call, Throwable t) {

            }
        });

        return view;
    }


    public void showView(View view){

        registerCarLayout = view.findViewById(R.id.registerCarLayout);
        registerCarInfo = view.findViewById(R.id.registerCarInfo);
        registerCar = view.findViewById(R.id.registerNewCar);

        carsAdapter = new CarsAdapter(getContext(),carArrayList);

        recyclerView = view.findViewById(R.id.carRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        webView = view.findViewById(R.id.infoWebView);
        String message = "<div style='display:flex;flex-direction:column'>" +
                "<span style='font-size:1.1em;font-style:bold;color:#1976d2'>Hello "+firstName+" Welcome to Ride ads.</span> " +
                "<p style='text-align: justify;'>Ride ads is a company which starts advertising different companies product on cars using tablet computers." +
                "We use 10 and above inch tablet computer  to play advert videos, audios and images of advertisers and also use all public transportation cars" +
                ".</p>" +
                "<p>Cars allowed to join our companies are Taxi and cross country bus</p>"+
                "<span style='font-size:1.1em;font-style:bold;text-decoration:underline;color:#1976d2'>" +
                "Taxi category includes</span>" +
                "<div style='display:flex;flex-direction:column'>" +
                "<span>Lada, Mini bus, Meter taxi and Bajajas</span>" +
                "</div>" +
                "<span style='margin-top:20px;font-size:1.1em;font-style:bold;text-decoration:underline;color:#1976d2'>" +
                "Bus category includes</span>"+
                "<span>All cross country bus</span>"+
                "<span style='margin-top:20px'>According to our data this tablet is not assigned to any car. Assign it to your Taxi or to your bus and let it make money.</span>"+
                "</div>";
        String html = "<html><head></head><body>"+message+"</body></html>";
        webView.loadDataWithBaseURL("", html, "text/html", "utf-8", "");

        /*
          This handler will help us to delay the layout until Webview loads its data
         */
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        registerCarLayout.setVisibility(View.VISIBLE);
                        if (carList.size()<=0){
                            registerCarInfo.setText("There is no registered car by your name. Register your car now and get more income");
                        }else {
                            registerCar.setVisibility(View.GONE);
                            for (int i=0;i<carList.size();i++){
                                if (carList.get(i).getWorking_tablet().size()<=0){
                                    carArrayList.add(carList.get(i));
                                }
                            }
                            if (carArrayList.size()<=0){
                                registerCarInfo.setText("There is no car to assign for this tablet. All your cars are assigned a tablet. If you have more car register it now and assign this tablet for it");
                                registerCar.setVisibility(View.VISIBLE);
                            }else {
                                registerCarInfo.setTextColor(Color.GREEN);
                                registerCarInfo.setText("According to our data we found "+carArrayList.size()+" cars without tablet. Assign this tablet to one of the following cars");
                                recyclerView.setAdapter(carsAdapter);
                                carsAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                },
                1000);


        registerCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverDashboard dashboard = (DriverDashboard)getActivity();
                dashboard.showCarRegisterationFragment();
            }
        });
    }

}
