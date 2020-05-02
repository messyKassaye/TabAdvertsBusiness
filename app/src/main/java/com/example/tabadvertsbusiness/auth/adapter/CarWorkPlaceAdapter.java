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
import com.example.tabadvertsbusiness.auth.model.Place;
import com.example.tabadvertsbusiness.auth.services.PlaceService;
import com.example.tabadvertsbusiness.auth.view.fragments.RegisterCarWorkPlace;

import java.util.ArrayList;

public class CarWorkPlaceAdapter extends RecyclerView.Adapter<CarWorkPlaceAdapter.ViewHolder> {
    private ArrayList<Place> places;
    private Context context;
    private RegisterCarWorkPlace fragment;
    public CarWorkPlaceAdapter(Context context, RegisterCarWorkPlace fragmet, ArrayList<Place> placeList){
        this.places = placeList;
        this.context = context;
        this.fragment = fragmet;
    }

    @NonNull
    @Override
    public CarWorkPlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.place_card_layout,viewGroup,false);
        return new CarWorkPlaceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarWorkPlaceAdapter.ViewHolder viewHolder, int i) {
        Place place=places.get(i);
        viewHolder.cardView.setTag(place.getId());
        viewHolder.city.setText(place.getCity());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.selectedLayout.setVisibility(View.VISIBLE);
                viewHolder.cardView.setCardBackgroundColor(Color.GREEN);
                fragment.showRegisterButton(place.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView city,selectedLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            city = (TextView)itemView.findViewById(R.id.city_name);
            cardView = (CardView)itemView.findViewById(R.id.placeCard);
            selectedLayout = (TextView)itemView.findViewById(R.id.placeCardSelectedItem);

        }
    }

}
