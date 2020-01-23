package com.example.tabadvertsbusiness.auth.view.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.adapter.CarsAdapter;
import com.example.tabadvertsbusiness.auth.adapter.ShimmerRecyclerViewAdapter;
import com.example.tabadvertsbusiness.auth.model.Car;
import com.example.tabadvertsbusiness.auth.view_model.MeViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private MeViewModel viewModel;
    private View snackBarLayout;
    private Snackbar snackbar;

    private RecyclerView recyclerView;
    private ArrayList<Car> arrayList = new ArrayList<>();
    private CarsAdapter adapter;

    //shimmer pre-loading
    private RecyclerView skeletonLayout;
    private ShimmerLayout shimmer;
    private ShimmerRecyclerViewAdapter shimmerRecyclerViewAdapter;
    private ArrayList<Car> shimmerList = new ArrayList<>();


    public CarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarFragment newInstance(String param1, String param2) {
        CarFragment fragment = new CarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String serial_number = Build.SERIAL;
        snackBarLayout = getView().findViewById(R.id.car_coordinator);
        snackbar = Snackbar.make(snackBarLayout,"Assign this tablet to one of the following your car.",Snackbar.LENGTH_INDEFINITE);
        (snackbar.getView()).getLayoutParams().width =ViewGroup.LayoutParams.MATCH_PARENT;
        snackbar.show();

        //pre loading for shimmer
        shimmer = getView().findViewById(R.id.shimmerSkeleton);
        skeletonLayout = getView().findViewById(R.id.skeletonLayout);
        skeletonLayout.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        skeletonLayout.setItemAnimator(new DefaultItemAnimator());
        shimmerRecyclerViewAdapter = new ShimmerRecyclerViewAdapter(getActivity(),shimmerList);
        this.showSkeleton(true);




        //cars recycler view
        recyclerView = (RecyclerView)getView().findViewById(R.id.cars_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //recycler view adapter
        adapter = new CarsAdapter(getActivity(),arrayList);
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(getActivity()).get(MeViewModel.class);
        viewModel.me().observe(getActivity(),meResponse -> {
            if(meResponse!=null){
                this.showSkeleton(false);
                List<Car> list = meResponse.getData().getRelations().getCars();
                arrayList.addAll(list);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public int getSkeletonRowCount(Context context) {
        int pxHeight = getDeviceHeight(context);
        int skeletonRowHeight = (int) getResources()
                .getDimension(R.dimen.row_layout_height); //converts to pixel
        return (int) Math.ceil(pxHeight / skeletonRowHeight);
    }
    public int getDeviceHeight(Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return metrics.heightPixels;
    }


    public void showSkeleton(boolean show) {

        if (show) {

            skeletonLayout.setAdapter(shimmerRecyclerViewAdapter);
            shimmerRecyclerViewAdapter.notifyDataSetChanged();
            shimmer.setVisibility(View.VISIBLE);
        } else {
            shimmer.setVisibility(View.GONE);
        }
    }
}
