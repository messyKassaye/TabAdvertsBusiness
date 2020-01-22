package com.example.tabadvertsbusiness.auth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.helpers.ImageHelper;
import com.example.tabadvertsbusiness.auth.model.Car;

import java.util.ArrayList;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.ViewHolder> {

    private Context context;
    ArrayList<Car> articleArrayList;

    public CarsAdapter(Context context, ArrayList<Car> articleArrayList) {
        this.context = context;
        this.articleArrayList = articleArrayList;
    }

    @NonNull
    @Override
    public CarsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cars_card_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarsAdapter.ViewHolder viewHolder, int i) {
        Car car=articleArrayList.get(i);
        viewHolder.carImage.setImageBitmap(ImageHelper.convertBase64ToImage(car.getCar_category().get(0).getImage()));
        viewHolder.plate_number.setText(car.getPlate_number());
        /*viewHolder.tvAuthorAndPublishedAt.setText("-"+article.getAuthor() +" | "+"Piblishetd At: "+article.getPublishedAt());
        viewHolder.tvDescription.setText(article.getDescription());
        Glide.with(context)
                .load(article.getUrlToImage())
                .into(viewHolder.imgViewCover);*/
    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView plate_number;
        private final ImageView carImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            plate_number=(TextView) itemView.findViewById(R.id.plate_number);
            carImage = (ImageView)itemView.findViewById(R.id.taxi_image);

        }
    }
}
