package com.example.tabadvertsbusiness.auth.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.model.Car;
import com.example.tabadvertsbusiness.auth.model.Place;
import com.example.tabadvertsbusiness.auth.model.Tablet;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.services.PlaceService;
import com.example.tabadvertsbusiness.auth.utils.ApiResponse;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    private ArrayList<Place> places;
    private PlaceService placeService;
    private Context context;
    public PlacesAdapter(Context context, ArrayList<Place> placeList){
        this.places = placeList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlacesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.place_card_layout,viewGroup,false);
        return new PlacesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesAdapter.ViewHolder viewHolder, int i) {
        Place place=places.get(i);
        placeService = new PlaceService(this.context);
        viewHolder.cardView.setTag(place.getId());
        viewHolder.city.setText(place.getCity());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.selectedLayout.setVisibility(View.VISIBLE);
                viewHolder.cardView.setCardBackgroundColor(Color.GREEN);
                placeService.setPlaceId((int)view.getTag());
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
