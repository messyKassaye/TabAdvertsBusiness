package com.example.tabadvertsbusiness.auth.view.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.constants.Constants;
import com.example.tabadvertsbusiness.player.Player;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;

import static com.example.tabadvertsbusiness.auth.helpers.SuperApplication.getContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EntertainmentFilesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EntertainmentFilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntertainmentFilesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LinearLayout entertainmentLayout,buttonLayout;
    private TextView hiThere,entertainmentInfo;

    private Button addMyFile,startAdverting;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EntertainmentFilesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EntertainmentFilesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EntertainmentFilesFragment newInstance(String param1, String param2) {
        EntertainmentFilesFragment fragment = new EntertainmentFilesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_entertainment_files, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonLayout = getView().findViewById(R.id.buttonLayout);

        entertainmentLayout = getView().findViewById(R.id.entertainmentLayout);
        entertainmentLayout.setVisibility(View.VISIBLE);

        entertainmentInfo = getView().findViewById(R.id.entertainmentInfo);
        hiThere = getView().findViewById(R.id.hiThere);
        hiThere.setText("Hi there, how you doing today.");

        entertainmentInfo.setText("This tablet is assigned to play advert medias but, you can add " +
                "your files like music, comedy and audios. add your file and entertain your " +
                "passengers");

        addMyFile = getView().findViewById(R.id.addMyFiles);
        startAdverting = getView().findViewById(R.id.startPlaying);

        Constants.calculateDeviceResolution((AppCompatActivity) getContext());
        if(Constants.getRealWidth()<=480){
            buttonLayout.setOrientation(LinearLayout.VERTICAL);
        }else {
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            addMyFile.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            startAdverting.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ));
        }

        addMyFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChooserDialog(getActivity())
                        .withFilter(false,true,"mp4","mkv","mp3")
                        .enableMultiple(true)
                        .disableTitle(true)
                        .withChosenListener(new ChooserDialog.Result() {
                            @Override
                            public void onChoosePath(String path, File pathFile) {
                                System.out.println("Path: "+path);
                                System.out.println("File: "+pathFile.getName());
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

        startAdverting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Player.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
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
}
