package com.example.tabadvertsbusiness.home.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.home.models.Contact;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private Context context;
    ArrayList<Contact> carsArraylist;

    public ContactsAdapter(Context context, ArrayList<Contact> articleArrayList) {
        this.context = context;
        this.carsArraylist = articleArrayList;
    }

    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contacts_layout, viewGroup, false);
        return new ContactsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder viewHolder, int i) {
        Contact contact = carsArraylist.get(i);
        viewHolder.name.setText(contact.getName());
        viewHolder.icon.setImageResource(contact.getImage_id());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(contact.getLink()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return carsArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView icon;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.social_media_name);
            icon = itemView.findViewById(R.id.contact_icon);
            cardView = itemView.findViewById(R.id.contactsCard);

        }
    }

}