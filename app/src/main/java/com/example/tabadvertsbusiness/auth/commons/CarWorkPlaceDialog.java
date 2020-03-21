package com.example.tabadvertsbusiness.auth.commons;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.view.fragments.AddressFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.CarWorkPlaceFragment;

public class CarWorkPlaceDialog extends DialogFragment {
    public static final String TAG = "example_dialog";

    private Toolbar toolbar;
    private String title;
    private int id;
    private View view;

    public CarWorkPlaceDialog newInstance(){
        return this;
    }
    public void display(FragmentManager fragmentManager, String title, int id) {
        this.title = title;
        this.id = id;
        this.show(fragmentManager,TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(id, container, false);
        toolbar = view.findViewById(R.id.dialogToolbar);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        CarWorkPlaceFragment f = (CarWorkPlaceFragment) getFragmentManager()
                .findFragmentById(R.id.carWorkPlaceFragment);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }

    public void closeDialog(){
        this.dismissAllowingStateLoss();
    }
}

