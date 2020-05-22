package com.example.tabadvertsbusiness.auth.view.fragments;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Environment;
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
import com.example.tabadvertsbusiness.auth.model.Car;
import com.example.tabadvertsbusiness.auth.model.DownloadRequests;
import com.example.tabadvertsbusiness.auth.model.DownloadedAdverts;
import com.example.tabadvertsbusiness.auth.receiver.DownloadCompletedBroadcastReceiver;
import com.example.tabadvertsbusiness.auth.response.DownloadRequestResponse;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.response.TabletResponse;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.auth.sharedPreferences.ApplicationPreferenceCreator;
import com.example.tabadvertsbusiness.auth.utils.ApiResponse;
import com.example.tabadvertsbusiness.auth.view.DriverDashboard;
import com.example.tabadvertsbusiness.auth.view_model.DownloadRequestViewModel;
import com.example.tabadvertsbusiness.auth.view_model.DownloadedaAdvertsViewModel;
import com.example.tabadvertsbusiness.auth.view_model.TabletViewModel;
import com.example.tabadvertsbusiness.constants.Constants;

import org.json.JSONArray;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.DOWNLOAD_SERVICE;


public class DriverDownloadRequestFragment extends Fragment {
    private CardView cardView;
    private TextView requestInfo;
    private Button sendDownloadRequestButton;

    private LinearLayout mainLayout;

    private DownloadedaAdvertsViewModel downloadedaAdvertsViewModel;
    private DownloadRequestViewModel downloadRequestViewModel;

    private DownloadedAdverts downloadedAdverts;

    private ProgressDialog progressDialog;
    private DownloadCompletedBroadcastReceiver receiver;

    private ProgressBar progressBar;
    private LinearLayout downloadRequestLayout;

    private DownloadRequests processedRequestedDownloads;
    private Button downloadProcessedFile;
    private TextView downloadingStatus;
    private int car_work_place;
    private String serial_number;
    private TabletViewModel tabletViewModel;
    private Car thisTabletCar;
    private LinearLayout mainDownloadLayout;

    public DriverDownloadRequestFragment() {
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
        View view = inflater.inflate(R.layout.fragment_driver_download_request, container, false);


        cardView = view.findViewById(R.id.driverDownloadCard);
        mainDownloadLayout = view.findViewById(R.id.driverDownloadMainLayout);
        progressBar = view.findViewById(R.id.driverDownloadProgress);
        mainLayout = view.findViewById(R.id.mainLayout);

        downloadRequestLayout = view.findViewById(R.id.downloadRequestLayout);
        downloadingStatus = view.findViewById(R.id.downloadingStatus);

        requestInfo = view.findViewById(R.id.sendRequestInfo);
        sendDownloadRequestButton = view.findViewById(R.id.sendDownloadRequestNow);
        downloadProcessedFile = view.findViewById(R.id.driverDownloadNow);

        /*
          View models and it's progress bar
         */
        downloadRequestViewModel = ViewModelProviders.of(getActivity()).get(DownloadRequestViewModel.class);
        progressDialog = LoadingDialog.loadingDialog(getActivity(), "Sending your request....");
        tabletViewModel = ViewModelProviders.of(getActivity()).get(TabletViewModel.class);


        /*
          first let's check if this tablet is assigned to any car
         */
        serial_number = Build.SERIAL;
        tabletViewModel.show(serial_number).enqueue(new Callback<TabletResponse>() {
            @Override
            public void onResponse(Call<TabletResponse> call, Response<TabletResponse> response) {
                if (response.body().getData().size() > 0) {
                    thisTabletCar = response.body().getData().get(0).getCars();
                    if (response.body().getData().get(0).getCars().getWorking_place().size()<=0){
                        DriverDashboard dashboard = (DriverDashboard)getContext();
                        dashboard.showRegisterCarWorkPlaceFragment(response.body().getData().get(0).getCars());
                    }else {
                        car_work_place = response.body().getData().get(0).getCars().getWorking_place().get(0).getId();
                        mainDownloadLayout.setVisibility(View.VISIBLE);

                        /*
                          check if downloading is on progress
                        */
                        if (ApplicationPreferenceCreator.isDownloading(getActivity()) == null) {
                            showDefaultHomePage();
                        } else {
                            if (ApplicationPreferenceCreator.isDownloading(getActivity()).equalsIgnoreCase("downloading")) {
                                showDownloading();
                            } else {
                                showDefaultHomePage();
                            }
                        }
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    DriverDashboard dashboard = (DriverDashboard) getContext();
                    dashboard.showTabletIsNotAssignedToCar();
                }
            }

            @Override
            public void onFailure(Call<TabletResponse> call, Throwable t) {

            }
        });


        sendDownloadRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if device have small disk space
                File cacheDir = getActivity().getCacheDir();
                if (cacheDir.getUsableSpace() * 100 / cacheDir.getTotalSpace() <= 10) { // Alternatively, use cacheDir.getFreeSpace()
                    Toast.makeText(getActivity(),
                            "You have low space. Please delete some file or use external devices",
                            Toast.LENGTH_LONG).show();
                } else {
                    DownloadRequests downloadRequests = new DownloadRequests();
                    downloadRequests.setDevice_id(serial_number);
                    downloadRequests.setDownloadedAdverts(downloadedAdverts.getDownloadedAdverts());
                    downloadRequestViewModel.storeResponse().observe(getActivity(), DriverDownloadRequestFragment.this::consumeResponse);
                    downloadRequestViewModel.store(downloadRequests);
                }

            }
        });

        downloadProcessedFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                 }else{
                    beginDownload(Constants.getDownloadPath() + "Zips/" + processedRequestedDownloads.getFile_name(),
                            processedRequestedDownloads.getFile_name());
                    showDownloading();
                    ApplicationPreferenceCreator.setDownloadingStatus(getActivity(), "downloading");

                }

            }

        });

        return view;
    }

    public void showDefaultHomePage() {
        /*
         check if there is new download request and if there is processed requests
         */
        downloadRequestViewModel.show(serial_number,"new_request").enqueue(new Callback<DownloadRequestResponse>() {
            @Override
            public void onResponse(Call<DownloadRequestResponse> call, Response<DownloadRequestResponse> response) {
                if (response.body().getData().size() <= 0) {
                    showDownloadRequestProcess();
                } else {
                    progressBar.setVisibility(View.GONE);
                    showDownloadingRequestOnProgress();
                }
            }

            @Override
            public void onFailure(Call<DownloadRequestResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public void showDownloading() {
        progressBar.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);

        downloadRequestLayout.setVisibility(View.VISIBLE);
        downloadingStatus.setVisibility(View.VISIBLE);
        downloadingStatus.setTextColor(Color.WHITE);
        downloadingStatus.setText("Downloading your file...");
        downloadProcessedFile.setVisibility(View.GONE);
    }

    public void showDownloadingRequestOnProgress() {
        mainLayout.setVisibility(View.VISIBLE);
        requestInfo.setTextColor(Color.GREEN);
        requestInfo.setText("We are processing your download request. This will take a small amount of time....");
        sendDownloadRequestButton.setVisibility(View.GONE);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
            DriverDashboard dashboard = (DriverDashboard) getContext();
            dashboard.showDownloadRequestSubmmitedFragment(response.getMessage());
        } else {
            Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void getNewAdverts(DownloadedAdverts downloadedAdverts) {
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

                        if (response.isStatus()) {
                            requestInfo.setText("Congratulations you have " +
                                    response.getAdverts().size() + " new adverts");
                            requestInfo.setTextColor(Color.GREEN);
                            sendDownloadRequestButton.setText("Send your download request");
                        } else {
                            if (response.getStatus_type().equalsIgnoreCase("no advert")) {
                                sendDownloadRequestButton.setVisibility(View.GONE);
                                requestInfo.setText(response.getMessage());
                                requestInfo.setTextColor(Color.RED);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessResponse> call, Throwable t) {

                    }
                });

    }

    private void beginDownload(String file_link, String fileName) {


            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/advertData", fileName);

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
            receiver = new DownloadCompletedBroadcastReceiver(getContext(), fileName, downloadId, processedRequestedDownloads, this);
            getContext().registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    public void downloadUnsucessful() {
        downloadRequestLayout.setVisibility(View.VISIBLE);
        downloadingStatus.setText("Something is not good. please try again");
        downloadingStatus.setTextColor(Color.RED);
        downloadProcessedFile.setVisibility(View.VISIBLE);
    }

    public void downloadSuccessful() {
        downloadRequestLayout.setVisibility(View.VISIBLE);
        downloadingStatus.setText("Download completed. Thank you");
        downloadingStatus.setTextColor(Color.GREEN);
    }


    public void showDownloadRequestProcess() {
        downloadRequestViewModel.show(serial_number,"request_processed")
                .enqueue(new Callback<DownloadRequestResponse>() {
                    @Override
                    public void onResponse(Call<DownloadRequestResponse> call, Response<DownloadRequestResponse> response) {
                        if (response.body().getData().size() <= 0) {
                            TabletAdsRoomDatabase.getDatabase(getActivity()).getAdvertDAO()
                                    .index().observe(getActivity(), advertRooms -> {
                                //if there is no previous download history
                                if (advertRooms.size() <= 0) {
                                    downloadedAdverts = new DownloadedAdverts();
                                    downloadedAdverts.setCar_work_place(car_work_place);
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
                                    downloadedAdverts.setCar_work_place(car_work_place);
                                    downloadedAdverts.setDownloadedAdverts(jsonArray.toString());
                                    getNewAdverts(downloadedAdverts);
                                }
                            });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.GONE);
                            downloadRequestLayout.setVisibility(View.VISIBLE);
                            processedRequestedDownloads = response.body().getData().get(0);
                        }
                    }

                    @Override
                    public void onFailure(Call<DownloadRequestResponse> call, Throwable t) {

                    }
                });
    }


}