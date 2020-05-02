package com.example.tabadvertsbusiness.auth.view.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertViewsViewModel;
import java.util.List;


public class MyAdvertsFragment extends Fragment {

    private AdvertViewsViewModel viewsViewModel;

    private ProgressBar progressBar;
    private LinearLayout noAdvertsFound;
    private TableLayout tableLayout;
    private HorizontalScrollView recyclerView;

    public MyAdvertsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_adverts, container, false);

        viewsViewModel = ViewModelProviders.of(getActivity()).get(AdvertViewsViewModel.class);

        recyclerView = view.findViewById(R.id.advertsRecyclerView);

        progressBar = view.findViewById(R.id.myAdvertPbr);
        noAdvertsFound = view.findViewById(R.id.noAdvertsFound);
        tableLayout = view.findViewById(R.id.advertTable);

        viewsViewModel.index().observe(getActivity(),advertViewsRooms -> {
            progressBar.setVisibility(View.GONE);
            if (advertViewsRooms.size()<=0){
                noAdvertsFound.setVisibility(View.VISIBLE);
            }else {
                recyclerView.setVisibility(View.VISIBLE);
                showData(advertViewsRooms);
            }
        });
        return view;
    }


    public void showData(List<AdvertViewsRoom> adverts){

        int leftRowMargin=0;
        int topRowMargin=0;
        int rightRowMargin=0;
        int bottomRowMargin = 0;
        int textSize = 0, smallTextSize =0, mediumTextSize = 0;

        TextView spacer = null;
        for (int i= -1;i<adverts.size();i++){
            AdvertViewsRoom row = null;
            if (i> -1){
                row = adverts.get(i);
            }else {
                spacer = new TextView(getContext());
                spacer.setText("");
            }

            // data columns
            final LinearLayout firstColumn = new LinearLayout(getContext());
            firstColumn.setOrientation(LinearLayout.HORIZONTAL);
            firstColumn.setPadding(0,0,5,0);
            final TextView tv = new TextView(getContext());
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.LEFT);
            if (i == -1) {
                tv.setText("Id no");
                tv.setBackgroundColor(Color.parseColor("#1976d2"));
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
                firstColumn.addView(tv);
            } else {
                tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv.setText(String.valueOf(row.getId()));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
                firstColumn.addView(tv);

            }


            final  LinearLayout secondColumn = new LinearLayout(getContext());
            secondColumn.setPadding(0,0,5,0);
            secondColumn.setOrientation(LinearLayout.HORIZONTAL);
            final TextView tv2 = new TextView(getContext());
            if (i == -1) {
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
                secondColumn.addView(tv2);
            } else {
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
                secondColumn.addView(tv2);
            }
            tv2.setGravity(Gravity.LEFT);
            if (i == -1) {
                tv2.setText("Date");
                tv2.setBackgroundColor(Color.parseColor("#1976d2"));
                tv2.setTextColor(Color.WHITE);
            }else {
                tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                tv2.setTextColor(Color.parseColor("#000000"));
                tv2.setText(row.getAdvertTime());
            }

            final  LinearLayout thirdColumn = new LinearLayout(getContext());
            thirdColumn.setPadding(0,0,5,0);
            thirdColumn.setOrientation(LinearLayout.HORIZONTAL);
            thirdColumn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            final TextView tv3 = new TextView(getContext());
            if (i == -1) {
                tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
                thirdColumn.addView(tv3);
            } else {
                tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
                tv3.setGravity(Gravity.RIGHT);
                thirdColumn.addView(tv3);
            }
            tv3.setGravity(Gravity.RIGHT);
            if (i == -1) {
                tv3.setText("Number of viewers");
                tv3.setBackgroundColor(Color.parseColor("#1976d2"));
                tv3.setTextColor(Color.WHITE);
            }else {
                tv3.setBackgroundColor(Color.parseColor("#ffffff"));
                tv3.setTextColor(Color.parseColor("#000000"));
                tv3.setGravity(Gravity.RIGHT);
                tv3.setText("       3");
            }
            final  LinearLayout fourthColumn = new LinearLayout(getContext());
            fourthColumn.setPadding(0,0,5,0);
            fourthColumn.setOrientation(LinearLayout.HORIZONTAL);
            final TextView tv4 = new TextView(getContext());
            if (i == -1) {
                tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
                fourthColumn.addView(tv4);
            } else {
                tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
                fourthColumn.addView(tv4);
            }
            tv4.setGravity(Gravity.LEFT);
            if (i == -1) {
                tv4.setText("Send to server");
                tv4.setBackgroundColor(Color.parseColor("#1976d2"));
                tv4.setTextColor(Color.WHITE);
            }else {
                tv4.setBackgroundColor(Color.parseColor("#ffffff"));
                tv4.setTextColor(Color.parseColor("#000000"));
                if (row.isSend()){
                    tv4.setText("Send");
                    tv4.setTextColor(Color.GREEN);
                }else {
                    tv4.setText("Not send");
                    tv4.setTextColor(Color.RED);
                }
            }



            // add table row
            final TableRow tr = new TableRow(getContext());
            tr.setId(i + 1);
            TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
            tr.setPadding(0,10,0,10);
            tr.setLayoutParams(trParams);
            tr.addView(firstColumn);
            tr.addView(secondColumn);
            tr.addView(thirdColumn);
            tr.addView(fourthColumn);
            tableLayout.addView(tr,trParams);

            if (i > -1) {
                // add separator row
                final TableRow trSep = new TableRow(getContext());
                TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                trSep.setLayoutParams(trParamsSep);
                TextView tvSep = new TextView(getContext());
                TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                tvSepLay.span = 4;
                tvSep.setLayoutParams(tvSepLay);
                tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
                tvSep.setHeight(1);
                trSep.addView(tvSep);
                tableLayout.addView(trSep, trParamsSep);
            }
        }
    }

}
