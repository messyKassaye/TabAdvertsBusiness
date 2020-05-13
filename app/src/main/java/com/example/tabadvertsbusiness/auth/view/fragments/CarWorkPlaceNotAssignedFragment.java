package com.example.tabadvertsbusiness.auth.view.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.adapter.CarsAdapter;
import com.example.tabadvertsbusiness.auth.model.Car;

import java.util.ArrayList;


public class CarWorkPlaceNotAssignedFragment extends Fragment {

    private TextView textView;
    private RecyclerView recyclerView;
    private ArrayList<Car> carArrayList = new ArrayList<>();
    private CarsAdapter adapter;
    public CarWorkPlaceNotAssignedFragment(Car car) {
        // Required empty public constructor
        carArrayList.add(car);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car_work_place_not_assigned, container, false);

        textView = view.findViewById(R.id.carWorkPlaceInfo);
        textView.setTextColor(Color.RED);
        textView.setText("The car which is assigned to this tablet is not set it's works place. please set your car work place");

        adapter = new CarsAdapter(getContext(),carArrayList);

        recyclerView = view.findViewById(R.id.carWorkPlacesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }

}
