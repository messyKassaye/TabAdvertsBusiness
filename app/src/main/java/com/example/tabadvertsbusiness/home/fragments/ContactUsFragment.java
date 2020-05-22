package com.example.tabadvertsbusiness.home.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.helpers.GridSpacingItemDecoration;
import com.example.tabadvertsbusiness.constants.Constants;
import com.example.tabadvertsbusiness.home.adapter.CallAdapter;
import com.example.tabadvertsbusiness.home.adapter.ContactsAdapter;
import com.example.tabadvertsbusiness.home.models.CallPhone;
import com.example.tabadvertsbusiness.home.models.Contact;

import java.util.ArrayList;


public class ContactUsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactsAdapter adapter;

    private ArrayList<CallPhone> callPhoneArrayList = new ArrayList<>();
    private RecyclerView callRecylerView;
    private CallAdapter callAdapter;

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        adapter = new ContactsAdapter(getContext(),contactArrayList);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        contactArrayList.addAll(Constants.getContacts());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        callAdapter = new CallAdapter(getActivity(),callPhoneArrayList);
        callRecylerView = view.findViewById(R.id.callRecyclerView);
        callRecylerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        callRecylerView.setItemAnimator(new DefaultItemAnimator());
        callPhoneArrayList.addAll(Constants.getPhones());
        callRecylerView.setAdapter(callAdapter);
        callAdapter.notifyDataSetChanged();

        return view;
    }

}
