package com.example.tabadvertsbusiness.player.players;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.roomDB.DAO.AdvertDAO;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.EntertainmentRoom;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertRoomVIewModel;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.AdvertViewsViewModel;
import com.example.tabadvertsbusiness.constants.Constants;
import com.example.tabadvertsbusiness.player.PlayerController;
import com.example.tabadvertsbusiness.player.adverts.AdvertDialog;
import com.example.tabadvertsbusiness.player.helpers.ScalingUtilities;
import com.example.tabadvertsbusiness.player.vision.AppFaceDetector;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AdvertPlayer extends Fragment {
    private ArrayList<AdvertRoom> advertPlaylist = new ArrayList<>();
    private MediaPlayer player;
    private AdvertViewsViewModel vIewModel;
    private AdvertRoomVIewModel advertRoomVIewModel;
    private SurfaceView surfaceView;

    private Executor executor = Executors.newSingleThreadExecutor();
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final String IMAGE_NAME = "temp_image.jpg";
    PreviewView mPreviewView;
    private AdvertTimeElapsing timeElapsing;
    private Button counterButton;
    public AdvertPlayer() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_advert_player,container,false);

        vIewModel = ViewModelProviders.of(getActivity()).get(AdvertViewsViewModel.class);
        advertRoomVIewModel = ViewModelProviders.of(getActivity()).get(AdvertRoomVIewModel.class);

        surfaceView = view.findViewById(R.id.advertPlayerSurfaceView);
        surfaceView.setZOrderOnTop(false);

        mPreviewView = view.findViewById(R.id.previewView);
        counterButton = view.findViewById(R.id.counterButton);

        if(allPermissionsGranted()){
            startCamera(); //start camera if permission has been granted by user
        } else{
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        prepareAdvertData();
        return view;
    }

    public void prepareAdvertData(){

        TabletAdsRoomDatabase db = TabletAdsRoomDatabase.getDatabase(getApplicationContext());

        AdvertDAO advertDAO = db.getAdvertDAO();
        advertDAO.index().observe(this,advertRooms -> {
            preparePlayList(advertRooms);
        });

    }

    public void preparePlayList(List<AdvertRoom> adverts){
        advertPlaylist.addAll(adverts);
        AdvertRoom advertRoom = advertPlaylist.get(getRandomNumber());//findAdvertIdRecursively(getRandomNumber());
        advertRoomVIewModel.show(advertRoom.getId())
                .observe(this,advertRooms -> {
                    vIewModel.todayAdvert(Constants.currentDate(),advertRoom.getId())
                            .observe(this,advertViewsRooms -> {
                                if (advertViewsRooms.size()>=advertRooms.get(0).getMaximumViewPerDay()){
                                    prepareAdvertData();
                                }else {
                                    File file = new File(advertRoom.getFilePath());
                                    if (file.exists()){
                                        playRecursively(advertRoom.getFilePath(),advertRoom.getAdvertId());
                                    }else {
                                        prepareAdvertData();
                                    }

                                }
                            });
                });

    }


    public int getRandomNumber(){
        Random random = new Random();
        return  random.nextInt(advertPlaylist.size());
    }

    public void playRecursively(String path,int advertId){
        try {

            player = new MediaPlayer();
            player.setDisplay(surfaceView.getHolder());
            player.setDataSource(path);
            player.prepare();
            player.start();

            timeElapsing = new AdvertTimeElapsing(player.getDuration(),1000);
            timeElapsing.start();

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    saveAdvertViewData(advertId);

                }
            });

            Constants.advertPlayTimer(getContext(),player.getDuration());

        }catch (Exception e){

        }
    }


    private void startCamera() {

        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {

                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(getActivity()));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();

        ImageCapture.Builder builder = new ImageCapture.Builder();

        //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

        // Query if extension is available (optional).
        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            // Enable the extension if available.
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }

        final ImageCapture imageCapture = builder
                .setTargetRotation(preview.getTargetRotation())
                .build();

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis, imageCapture);

        preview.setSurfaceProvider(mPreviewView.createSurfaceProvider(camera.getCameraInfo()));
        File file = new File(getBatchDirectoryName(), IMAGE_NAME);

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback () {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startAnalysisis(file.getAbsolutePath());

                        AppFaceDetector faceDetector=new AppFaceDetector(getContext());
                        faceDetector.startDetecting();

                        CameraX.unbindAll();
                        //
                    }
                });
            }
            @Override
            public void onError(@NonNull ImageCaptureException error) {
                error.printStackTrace();
            }
        });
    }

    public String getBatchDirectoryName() {

        String app_folder_path = "";
        app_folder_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/advertData";
        File dir = new File(app_folder_path);
        if (!dir.exists() && !dir.mkdirs()) {

        }

        return app_folder_path;
    }

    public void startAnalysisis(String path){
        //startFaceDetection(path);
        String decodedPath = decodeFile(path,640,480);
        String base64 = getFileToByte(decodedPath);
        Constants.setImage(base64,getActivity());

        PlayerController controller = (PlayerController)getActivity();
        //controller.startAdvert();
    }

    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                startCamera();
            } else{
                Toast.makeText(getActivity(), "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    public static String getFileToByte(String filePath){
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try{
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return encodeString;
    }

    private String decodeFile(String path,int DESIREDWIDTH, int DESIREDHEIGHT) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/advertData".toString();
            File mFolder = new File(extr);
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            File f = new File(mFolder.getAbsolutePath(), IMAGE_NAME);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

    }


    @Override
    public void onPause() {
        super.onPause();

        if(player!=null){
            player.release();
            player=null;
        }
    }

    public void saveAdvertViewData(int advertId){
        String date = Constants.currentDate();
        String advertTime = Constants.currentTime();
        AdvertViewsRoom advertViewsRoom = new AdvertViewsRoom();
        advertViewsRoom.setAdvertId(advertId);
        advertViewsRoom.setAdvertDate(date);
        advertViewsRoom.setNumberOfViewers(Constants.getFaces(getContext()));
        advertViewsRoom.setPicture(Constants.getImage(getContext()));
        advertViewsRoom.setAdvertTime(advertTime);
        vIewModel.store(advertViewsRoom);
    }


    public class AdvertTimeElapsing extends CountDownTimer {

        public AdvertTimeElapsing(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long elapsedhour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));

            long elapsedMinute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));

            long elapsedSecond = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
            counterButton.setText(elapsedMinute+" : "+elapsedSecond);
        }

        @Override
        public void onFinish() {

        }
    }
}