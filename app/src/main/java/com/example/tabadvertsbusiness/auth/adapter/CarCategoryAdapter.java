package com.example.tabadvertsbusiness.auth.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

public class CarCategoryAdapter extends RecyclerView.Adapter<CarCategoryAdapter.ViewHolder> {
    private ArrayList<Category> categories;
    private PlaceService placeService;
    private Context context;
    private RegisterNewCar registerNewCar;


    public CarCategoryAdapter(Context context, ArrayList<Category> category,RegisterNewCar car){
        this.categories = category;
        this.context = context;
        this.registerNewCar = car;
    }

    @NonNull
    @Override
    public CarCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.car_type_layout,viewGroup,false);
        return new CarCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarCategoryAdapter.ViewHolder viewHolder, int i) {
        Category category=categories.get(i);
        placeService = new PlaceService(this.context);
        viewHolder.cardView.setTag(category.getId());
        viewHolder.carType.setText(category.getName());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Child> arrayList = new ArrayList<>();
                arrayList.addAll(category.getChild());
                registerNewCar.showCarCategory(arrayList);
                viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#2E8B57"));
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
        private final LinearLayout carCategoryLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            carType = (TextView)itemView.findViewById(R.id.carType);
            cardView = (CardView)itemView.findViewById(R.id.carTypeCard);
            carCategoryLayout = (LinearLayout)itemView.findViewById(R.id.carCategoryLayout);


        }
    }

}
