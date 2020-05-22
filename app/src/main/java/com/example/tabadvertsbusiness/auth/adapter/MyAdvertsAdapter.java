package com.example.tabadvertsbusiness.auth.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertViewsViewModel;

import java.util.ArrayList;

public class MyAdvertsAdapter extends RecyclerView.Adapter<MyAdvertsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AdvertRoom> carsArraylist;
    private AdvertViewsViewModel viewsViewModel;

    public MyAdvertsAdapter(Context context, ArrayList<AdvertRoom> articleArrayList) {
        this.context = context;
        this.carsArraylist = articleArrayList;


    }

    @NonNull
    @Override
    public MyAdvertsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_adverts_card_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdvertsAdapter.ViewHolder viewHolder, int i) {
        AdvertRoom viewsRoom = carsArraylist.get(i);

        viewsViewModel = ViewModelProviders.of((AppCompatActivity)context).get(AdvertViewsViewModel.class);

        viewHolder.companyName.setText(viewsRoom.getCompany_name());
        viewHolder.productName.setText(viewsRoom.getProduct_name());
        viewHolder.icon.setText(String.valueOf(viewsRoom.getCompany_name().charAt(0)));
        String fileName = viewsRoom.getFileName();
        String mediaName = viewsRoom.getFileName()
                .substring(fileName.lastIndexOf(".")+1,fileName.length());
        viewHolder.mediaType.setText(Html.fromHtml("Media type: <span style='color:#1976d2'>"
                +getMediaName(mediaName)+"</span>"));
        viewsViewModel.showCompany(viewsRoom.getId()).observe((AppCompatActivity)context,advertViewsRooms -> {
            viewHolder.totalAdvert.setText(Html.fromHtml("Total adverts: <span style='color:#1976d2'>"+advertViewsRooms.size()+" play</span>"));

        });
    }

    @Override
    public int getItemCount() {
        return carsArraylist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView icon,productName,companyName,mediaType,totalAdvert;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            companyName = itemView.findViewById(R.id.company_name);
            productName = itemView.findViewById(R.id.product_name);
            icon = itemView.findViewById(R.id.company_image);
            mediaType = itemView.findViewById(R.id.mediaType);
            totalAdvert = itemView.findViewById(R.id.totalAdverts);
        }
    }


    public String getMediaName(String extension){
        if (extension.equalsIgnoreCase("mp4")||extension.equalsIgnoreCase("mkv")||extension.equalsIgnoreCase("")){
            return "Video";
        }else if (extension.equalsIgnoreCase("mp3")){
            return "Audio";
        }else {
            return "Image";
        }
    }

}
