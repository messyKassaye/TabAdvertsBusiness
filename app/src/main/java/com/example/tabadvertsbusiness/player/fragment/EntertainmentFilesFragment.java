package com.example.tabadvertsbusiness.player.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.roomDB.DAO.EntertainmentDAO;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.EntertainmentRoom;
import com.example.tabadvertsbusiness.constants.Constants;
import com.example.tabadvertsbusiness.player.PlayerController;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;


public class EntertainmentFilesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match


    private LinearLayout entertainmentLayout,buttonLayout;
    private TextView hiThere,entertainmentInfo;

    private Button addMyFile,startAdverting;




    public EntertainmentFilesFragment() {
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
            setMargins(addMyFile, 0, 50, 50, 0);

            startAdverting.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            setMargins(startAdverting, 0, 50, 0, 0);
        }

        addMyFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addFile();
            }
        });

        startAdverting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabletAdsRoomDatabase db = TabletAdsRoomDatabase.getDatabase(getActivity());
                db.getEntertainmentDAO().index().observe(getActivity(),entertainmentRooms -> {
                    if (entertainmentRooms.size()<=0){
                        Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.confirmation_dialog);
                        ((Button)dialog.findViewById(R.id.addFileButton))
                                .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                addFile();
                            }
                        });
                        ((Button)dialog.findViewById(R.id.closeButton))
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();

                                    }
                                });
                        dialog.show();
                    }else {
                        Intent intent = new Intent(getContext(), PlayerController.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        ActivityCompat.finishAffinity(getActivity());
                    }
                });
            }
        });
    }

    public void addFile(){
        new ChooserDialog(getActivity())
                .withFilter(false,true,"mp4","mkv","mp3")
                .enableMultiple(true)
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        File file = new File(path);
                        if(!file.isDirectory()){
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    TabletAdsRoomDatabase db=  TabletAdsRoomDatabase
                                            .getDatabase(getContext());
                                    EntertainmentDAO entDao = db.getEntertainmentDAO();
                                    EntertainmentRoom room = new EntertainmentRoom();
                                    room.setFilePath(path);
                                    room.setChoose(true);
                                    entDao.store(room);
                                }
                            });

                        }
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

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

}
