package com.example.tabadvertsbusiness.auth.view.fragments;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.commons.Helpers;
import com.example.tabadvertsbusiness.auth.dialogs.LoadingDialog;
import com.example.tabadvertsbusiness.auth.model.DownloadedAdverts;
import com.example.tabadvertsbusiness.auth.receiver.DownloadCompletedBroadcastReceiver;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.auth.utils.ApiResponse;
import com.example.tabadvertsbusiness.auth.view.DriverDashboard;
import com.example.tabadvertsbusiness.auth.view_model.DownloadViewModel;
import com.example.tabadvertsbusiness.auth.view_model.DownloadedaAdvertsViewModel;
import com.example.tabadvertsbusiness.constants.Constants;

import org.json.JSONArray;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.DOWNLOAD_SERVICE;


public class DriverDownloadFragment extends Fragment {
    private CardView cardView;
    private TextView downloadInfo;
    private Button driverDownloadButton;

    private DownloadedaAdvertsViewModel downloadedaAdvertsViewModel;
    private DownloadedAdverts downloadedAdverts;

    private DownloadViewModel downloadViewModel;
    private ProgressDialog progressDialog;
    private String filePath;
    private DownloadCompletedBroadcastReceiver receiver;

    private int downloadedAdvertsSize = 0;

    private ProgressBar progressBar;
    private LinearLayout mainLayout;


    public DriverDownloadFragment() {
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
        return inflater.inflate(R.layout.fragment_driver_download, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        downloadViewModel = ViewModelProviders.of(getActivity()).get(DownloadViewModel.class);
        progressDialog = LoadingDialog.loadingDialog(getActivity(), "We are Zipping your file....");
        downloadViewModel.storeResponse().observe(getActivity(), this::consumeResponse);

        mainLayout = getView().findViewById(R.id.driverDownloadLayout);
        progressBar = getView().findViewById(R.id.driverDownloadProgress);

        cardView = getView().findViewById(R.id.driverDownloadCard);
        cardView.setLayoutParams(
                new FrameLayout.LayoutParams(Helpers.deviceWidth((AppCompatActivity) getContext()),
                        FrameLayout.LayoutParams.WRAP_CONTENT)
        );

        downloadInfo = getView().findViewById(R.id.downloadInfo);


        TabletAdsRoomDatabase.getDatabase(getActivity()).getAdvertDAO()
                .index().observe(getActivity(), advertRooms -> {
            System.out.println("Adverts: "+advertRooms.size());
            //if there is no previous download history
            if (advertRooms.size() <= 0) {
                downloadedAdverts = new DownloadedAdverts();
                downloadedAdverts.setDownloadedAdverts("");
                getNewAdverts(downloadedAdverts);
            } else {
                /*if there is previous download history we have to send it
                  each adverts id to server because we shouldn't have to download again.
                 */
                List<AdvertRoom> advertRoomList = advertRooms;
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < advertRoomList.size(); i++) {
                    jsonArray.put(advertRoomList.get(i).getAdvertId());
                }
                downloadedAdverts = new DownloadedAdverts();
                downloadedAdverts.setDownloadedAdverts(jsonArray.toString());
                getNewAdverts(downloadedAdverts);
            }
        });


        driverDownloadButton = getView().findViewById(R.id.driverDownloadNow);
        driverDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if device have small disk space
                File cacheDir = getActivity().getCacheDir();
                if (cacheDir.getUsableSpace() * 100 / cacheDir.getTotalSpace() <= 10) { // Alternatively, use cacheDir.getFreeSpace()
                    Toast.makeText(getActivity(), "You have low space. Please delete some file or use external devices",
                            Toast.LENGTH_LONG).show();
                } else {
                    downloadViewModel.store(downloadedAdverts);
                }

            }
        });


    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public int getDownloadedAdvertsSize() {
        return downloadedAdvertsSize;
    }

    public void setDownloadedAdvertsSize(int downloadedAdvertsSize) {
        this.downloadedAdvertsSize = downloadedAdvertsSize;
    }

    private void consumeResponse(ApiResponse apiResponse) {

        switch (apiResponse.status) {

            case LOADING:
                progressDialog.show();
                break;

            case SUCCESS:
                progressDialog.dismiss();
                renderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Something is not Good. Please try again", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    /*
     * method to handle success response
     * */
    private void renderSuccessResponse(SuccessResponse response) {
        if (response.isStatus()) {
            this.filePath = response.getFile_path();
            beginDownload(Constants.getDownloadPath() + "Zips/" + response.getFile_path(), response.getFile_path());
        } else {
            Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void getNewAdverts(DownloadedAdverts downloadedAdverts){
        System.out.println("Advert: "+downloadedAdverts.getDownloadedAdverts());
        downloadedaAdvertsViewModel = ViewModelProviders.of(getActivity())
                .get(DownloadedaAdvertsViewModel.class);
       downloadedaAdvertsViewModel.store(downloadedAdverts)
               .enqueue(new Callback<SuccessResponse>() {
                   @Override
                   public void onResponse(Call<SuccessResponse> call,
                                          Response<SuccessResponse> responseBody) {
                      SuccessResponse response = responseBody.body();
                      progressBar.setVisibility(View.GONE);
                      mainLayout.setVisibility(View.VISIBLE);
                      if (response==null){
                          DriverDashboard dashboard = (DriverDashboard)getActivity();
                          dashboard.showCars();
                          Toast.makeText(getContext(),"Car work place is not set",Toast.LENGTH_LONG).show();
                      }else {
                          if(response.isStatus()){
                              downloadInfo.setText("Congratulations you have "+
                                      response.getAdverts().size()+" new adverts");
                              downloadInfo.setTextColor(Color.GREEN);
                          }else {
                              driverDownloadButton.setVisibility(View.GONE);
                              downloadInfo.setText(response.getMessage());
                              downloadInfo.setTextColor(Color.RED);
                          }
                      }

                   }

                   @Override
                   public void onFailure(Call<SuccessResponse> call, Throwable t) {

                   }
               });

    }

    private void beginDownload(String file_link, String fileName) {

        File file = new File(getContext().getExternalFilesDir(null) + "/advertData", fileName);

        //now if download complete file not visible now lets show it
        DownloadManager.Request request = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            request = new DownloadManager.Request(Uri.parse(file_link))
                    .setTitle("Tab adverts download")
                    .setDescription("Downloading...")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationUri(Uri.fromFile(file))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);
        } else {
            request = new DownloadManager.Request(Uri.parse(file_link))
                    .setTitle("Tab adverts download")
                    .setDescription("Downloading...")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationUri(Uri.fromFile(file))
                    .setAllowedOverRoaming(true);
        }

        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);
        receiver = new DownloadCompletedBroadcastReceiver(getContext(), fileName, downloadId);
        getContext().registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


    }
}
