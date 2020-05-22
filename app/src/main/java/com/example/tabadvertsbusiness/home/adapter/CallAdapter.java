package com.example.tabadvertsbusiness.home.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.home.models.CallPhone;
import com.example.tabadvertsbusiness.home.models.Contact;

import java.util.ArrayList;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.ViewHolder> {

    private Context context;
    ArrayList<CallPhone> carsArraylist;

    public CallAdapter(Context context, ArrayList<CallPhone> articleArrayList) {
        this.context = context;
        this.carsArraylist = articleArrayList;
    }

    @NonNull
    @Override
    public CallAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.calls_layout, viewGroup, false);
        return new CallAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallAdapter.ViewHolder viewHolder, int i) {
        CallPhone phones = carsArraylist.get(i);
        viewHolder.phone.setText(phones.getPhone());
        viewHolder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions((AppCompatActivity)context, new String[]{Manifest.permission.CALL_PHONE,}, 1);
                }else {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel: "+phones.getPhone()));
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return carsArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView phone;
        private ImageButton icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            phone = itemView.findViewById(R.id.phoneNo);
            icon = itemView.findViewById(R.id.callButton);

        }
    }

}
