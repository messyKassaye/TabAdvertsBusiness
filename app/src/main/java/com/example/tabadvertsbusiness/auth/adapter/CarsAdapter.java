package com.example.tabadvertsbusiness.auth.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.commons.Helpers;
import com.example.tabadvertsbusiness.auth.commons.MainDialog;
import com.example.tabadvertsbusiness.auth.dialogs.LoadingDialog;
import com.example.tabadvertsbusiness.auth.model.Car;
import com.example.tabadvertsbusiness.auth.model.Tablet;
import com.example.tabadvertsbusiness.auth.repository.TabletRepository;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.roomDB.entity.TabletAssignation;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.TabletAssignViewModel;
import com.example.tabadvertsbusiness.auth.services.PlaceService;
import com.example.tabadvertsbusiness.auth.utils.ApiResponse;
import com.example.tabadvertsbusiness.auth.view.DriverDashboard;
import com.example.tabadvertsbusiness.auth.view_model.MeViewModel;
import com.example.tabadvertsbusiness.auth.view_model.TabletViewModel;

import java.util.ArrayList;


public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.ViewHolder> {

    private Context context;
    ArrayList<Car> carsArraylist;
    private ProgressDialog progressDialog;
    private TabletViewModel viewModel;
    private TabletAssignViewModel tabletAssignViewModel;
    private MeViewModel meViewModel;

    private CardView cardView;
    private MainDialog mainDialog;
    private PlaceService placeService;


    public CarsAdapter(Context context, ArrayList<Car> articleArrayList) {
        this.context = context;
        this.carsArraylist = articleArrayList;
        progressDialog = LoadingDialog.loadingDialog(context,"Assigning....");

        AppCompatActivity activity = (AppCompatActivity)context;
        viewModel = ViewModelProviders.of(activity).get(TabletViewModel.class);
        tabletAssignViewModel = ViewModelProviders.of(activity).get(TabletAssignViewModel.class);
        meViewModel = ViewModelProviders.of(activity).get(MeViewModel.class);

        viewModel.storeResponse().observe(activity, this::consumeResponse);


    }

    @NonNull
    @Override
    public CarsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cars_card_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarsAdapter.ViewHolder viewHolder, int i) {
        Car car=carsArraylist.get(i);
        Tablet tablet = new Tablet();

        mainDialog = new MainDialog();

        placeService = new PlaceService(this.context);

        String serial_number = Build.SERIAL;
        tablet.setSerial_number(serial_number);
        tablet.setCar_id(car.getId());

        viewHolder.cardView.setLayoutParams(
                new RecyclerView.LayoutParams(Helpers.deviceWidth((AppCompatActivity) context),
                        RecyclerView.LayoutParams.WRAP_CONTENT));
        viewHolder.carImage.setText(String.valueOf(car.getCar_category().get(0).getName().charAt(0)));
        viewHolder.plate_number.setText("Plate number: "+car.getPlate_number());
        viewHolder.carType.setText(car.getCar_category().get(0).getName());
        viewHolder.totalAdvert.setText("Total adverts: "+car.getAdverts());
        if(car.getWorking_place().size()>0){
            viewHolder.workingPlace.setText("Work place: "+car.getWorking_place().get(0).getCity());
        }else {
            viewHolder.workingPlace.setText("Work place: No assigned");
            viewHolder.setWorkingPlace.setVisibility(View.VISIBLE);

        }

        if(car.getWorking_tablet().size()>0){
            viewHolder.assignButton.setVisibility(View.GONE);
            String serialNumber = Build.SERIAL;
            if (serialNumber.equalsIgnoreCase(car.getWorking_tablet().get(0).getSerial_number())){
                viewHolder.assigned_text.setText(Html.fromHtml("working tablet: <span style='color:#1976d2;'>On this tablet</span>"));
            }else {
                viewHolder.assigned_text.setText("Working tablet : "+car.getWorking_tablet()
                        .get(0).getSerial_number());
            }
        }

        viewHolder.assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.store(tablet);
            }
        });

        viewHolder.setWorkingPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverDashboard driverDashboard = (DriverDashboard)context;
                driverDashboard.showRegisterCarWorkPlaceFragment(car);
            }
        });

    }

    @Override
    public int getItemCount() {
        return carsArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView plate_number;
        private final TextView carImage;
        private final TextView carType;
        private final TextView totalAdvert;
        private final TextView workingPlace;
        private final Button setWorkingPlace;
        private final Button assignButton;
        private final TextView assigned_text;
        private final CardView cardView;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = (CardView)itemView.findViewById(R.id.cars_card);
            plate_number=(TextView) itemView.findViewById(R.id.plate_number);
            carImage = (TextView)itemView.findViewById(R.id.taxi_image);
            carType = (TextView)itemView.findViewById(R.id.car_type);

            totalAdvert = (TextView)itemView.findViewById(R.id.total_advert);
            workingPlace = (TextView)itemView.findViewById(R.id.working_place);
            setWorkingPlace = (Button)itemView.findViewById(R.id.set_working_place_button);

            assignButton = (Button)itemView.findViewById(R.id.assign_button);
            assigned_text = (TextView)itemView.findViewById(R.id.assigned_text);



        }
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
            TabletAssignation assignation = new TabletAssignation();
            assignation.setSerial_number(response.getTablet().getSerial_number());
            assignation.setUser_id(response.getTablet().getUser_id());
            assignation.setCar_id(response.getTablet().getCar_id());
            tabletAssignViewModel.store(assignation);
            DriverDashboard dashboard = (DriverDashboard)context;
            dashboard.showHome();
        }else {
            Toast.makeText(context,response.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
}
