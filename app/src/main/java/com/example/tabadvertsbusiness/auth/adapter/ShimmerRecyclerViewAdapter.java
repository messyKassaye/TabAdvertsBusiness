package com.example.tabadvertsbusiness.auth.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.dialogs.LoadingDialog;
import com.example.tabadvertsbusiness.auth.model.Car;
import com.example.tabadvertsbusiness.auth.model.Tablet;
import com.example.tabadvertsbusiness.auth.repository.TabletRepository;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.utils.ApiResponse;
import com.example.tabadvertsbusiness.auth.view_model.TabletViewModel;

import java.util.ArrayList;

public class ShimmerRecyclerViewAdapter extends RecyclerView.Adapter<ShimmerRecyclerViewAdapter.ViewHolder> {

    private Context context;
    ArrayList<Car> carList;
    private TabletRepository tabletRepository;
    private ProgressDialog progressDialog;
    private TabletViewModel viewModel;

    public ShimmerRecyclerViewAdapter(Context context, ArrayList<Car> articleArrayList) {
        this.context = context;

    }

    @NonNull
    @Override
    public ShimmerRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skeleton_row_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShimmerRecyclerViewAdapter.ViewHolder viewHolder, int i) {



    }

    @Override
    public int getItemCount() {
        if(carList == null){
            return 3;
        }else {
            return carList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {




        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }


}
