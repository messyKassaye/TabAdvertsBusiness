package com.example.tabadvertsbusiness.auth.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.model.Category;
import com.example.tabadvertsbusiness.auth.model.Child;
import com.example.tabadvertsbusiness.auth.services.PlaceService;
import com.example.tabadvertsbusiness.auth.view.fragments.RegisterNewCar;

import java.util.ArrayList;

public class ChildrenAdapter extends RecyclerView.Adapter<ChildrenAdapter.ViewHolder> {

    private ArrayList<Child> categories;
    private Context context;
    private RegisterNewCar registerNewCar;
    public ChildrenAdapter(Context context, ArrayList<Child> category, RegisterNewCar car){
        this.categories = category;
        this.context = context;
        this.registerNewCar = car;
    }

    @NonNull
    @Override
    public ChildrenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_car_layout,viewGroup,false);
        return new ChildrenAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildrenAdapter.ViewHolder viewHolder, int i) {

        Child category=categories.get(i);
        viewHolder.cardView.setTag(category.getId());
        viewHolder.carType.setText(category.getName());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#2E8B57"));
                registerNewCar.showPlateNumberLayout(category.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView carType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            carType = (TextView)itemView.findViewById(R.id.childName);
            cardView = (CardView)itemView.findViewById(R.id.childTypeCard);

        }
    }
}
