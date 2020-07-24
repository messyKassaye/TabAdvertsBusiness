package com.example.tabadvertsbusiness.auth.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.text.Html;
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
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
import com.example.tabadvertsbusiness.auth.services.PlaceService;
import com.example.tabadvertsbusiness.auth.utils.ApiResponse;
import com.example.tabadvertsbusiness.auth.view.DriverDashboard;
import com.example.tabadvertsbusiness.auth.view_model.TabletViewModel;

import java.util.ArrayList;

public class AllAdvertsAdapter extends RecyclerView.Adapter<AllAdvertsAdapter.ViewHolder> {

    private Context context;
    ArrayList<AdvertViewsRoom> carsArraylist;


    public AllAdvertsAdapter(Context context, ArrayList<AdvertViewsRoom> articleArrayList) {
        this.context = context;
        this.carsArraylist = articleArrayList;

    }

    @NonNull
    @Override
    public AllAdvertsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_adverts_card_layout, viewGroup, false);
        return new AllAdvertsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllAdvertsAdapter.ViewHolder viewHolder, int i) {
        AdvertViewsRoom adverts = carsArraylist.get(i);
        viewHolder.advertTime.setText("Advert time: "+adverts.getAdvertDate()+" "+adverts.getAdvertTime());

    }

    @Override
    public int getItemCount() {
        return carsArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView advertTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            advertTime = itemView.findViewById(R.id.advertTime);
        }
    }
}