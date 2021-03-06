package com.example.tabadvertsbusiness.auth.commons;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.view.fragments.AddressFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.CarFragment;

public class MainDialog extends DialogFragment {

    public static final String TAG = "example_dialog";

    private Toolbar toolbar;
    private String title;
    private Fragment fragment;
    private View view;

    public MainDialog newInstance(){
        return this;
    }
    public void display(FragmentManager fragmentManager, String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
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
        view = inflater.inflate(R.layout.main_dialog_layout, container, false);
        toolbar = view.findViewById(R.id.dialogToolbar);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.mainDialogContent, fragment);
        ft.addToBackStack(null);
        ft.commit();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle("  "+title);
        toolbar.setTitleTextColor(Color.WHITE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AddressFragment f = (AddressFragment) getFragmentManager()
                .findFragmentById(R.id.address_fragment);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }

    public void closeDialog(){
        super.dismissAllowingStateLoss();
    }
}