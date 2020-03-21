package com.example.tabadvertsbusiness.auth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabadvertsbusiness.R;

public class AdvertsAdapter  extends RecyclerView.Adapter<AdvertsAdapter.ViewHolder> {

    private Context context;

    public AdvertsAdapter(Context context){
        this.context = context;
    }
    @NonNull
    @Override
    public AdvertsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adverts_recyclerview_layout,
                viewGroup,false);
        return new AdvertsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
