package com.example.tabadvertsbusiness.auth.view.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.commons.Helpers;
import com.example.tabadvertsbusiness.auth.helpers.Unzipper;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;


public class FileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private CardView cardView;
    private Button receiveFile;
    public static final int PICKFILE_RESULT_CODE = 1;
    private Uri fileUri;
    private String filePath;
    private Unzipper unzipper;

    public FileFragment() {
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
        return inflater.inflate(R.layout.fragment_file, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardView = getView().findViewById(R.id.filesCard);
        cardView.setLayoutParams(
                new FrameLayout.LayoutParams(Helpers.deviceWidth((AppCompatActivity)getContext()),
                        FrameLayout.LayoutParams.WRAP_CONTENT)
        );

        receiveFile = getView().findViewById(R.id.receiveFileButton);
        receiveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               new ChooserDialog(getActivity())
                        .withFilter(false,true,"zip")
                        .withChosenListener(new ChooserDialog.Result() {
                            @Override
                            public void onChoosePath(String path, File pathFile) {
                                File file = new File(path);
                                handleReceiveFile(file);
                            }
                        })
                        // to handle the back key pressed or clicked outside the dialog:
                        .withOnCancelListener(new DialogInterface.OnCancelListener() {
                            public void onCancel(DialogInterface dialog) {
                                Toast.makeText(getContext(),"File selection canceled",
                                        Toast.LENGTH_LONG).show();
                                dialog.cancel(); // MUST have
                            }
                        })
                        .build()
                        .show();
            }
        });
    }

    public void handleReceiveFile(File file){
        new Unzipper(getContext(),file)
                .execute();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    fileUri = data.getData();
                    filePath = fileUri.getPath();
                    String filename=fileUri.getLastPathSegment();
                    System.out.println("Path: "+filename);
                }

                break;
        }
    }
}
