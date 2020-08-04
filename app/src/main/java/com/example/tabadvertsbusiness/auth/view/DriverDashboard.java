package com.example.tabadvertsbusiness.auth.view;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.tabadvertsbusiness.auth.roomDB.viewModel.TabletAssignViewModel;
import com.example.tabadvertsbusiness.auth.roomDB.viewModel.TabletAssignedCarWorkPlaceViewModel;
import com.example.tabadvertsbusiness.auth.view.fragments.HomeFragment;
import com.example.tabadvertsbusiness.constants.Constants;
import com.example.tabadvertsbusiness.home.HomeActivity;
import com.example.tabadvertsbusiness.auth.model.Car;
import com.example.tabadvertsbusiness.auth.response.TabletResponse;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.services.CommonServices;
import com.example.tabadvertsbusiness.auth.sharedPreferences.ApplicationPreferenceCreator;
import com.example.tabadvertsbusiness.auth.view.fragments.AbouThisTabletFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.CarFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.DownloadRequestSubmittedFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.DriverDownloadRequestFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.FinanceFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.MyAdvertsFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.MyFilesFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.RegisterCarWorkPlace;
import com.example.tabadvertsbusiness.auth.view.fragments.RegisterNewCar;
import com.example.tabadvertsbusiness.auth.view.fragments.SettingFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.TabletNotAssignedToCarFragment;
import com.example.tabadvertsbusiness.auth.view_model.TabletViewModel;

import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.view_model.MeViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,FinanceFragment.OnFragmentInteractionListener,
        SettingFragment.OnFragmentInteractionListener,
        DriverDownloadRequestFragment.OnFragmentInteractionListener,
        RegisterNewCar.OnFragmentInteractionListener{
    private static final String TAG = DriverDashboard.class.getSimpleName();
    private MeViewModel viewModel;
    private TextView fullName,email;
    private ImageView profileImage;
    private NavigationView navigationView;
    private String serial_number;
    private TabletViewModel tabletViewModel;
    private ProgressBar progressBar;
    private Response<TabletResponse> tabletResponseResponse;
    private CommonServices commonServices;
    private Toolbar toolbar;

    private TabletAssignViewModel tabletAssignViewModel;
    private TabletAssignedCarWorkPlaceViewModel tabletAssignedCarWorkPlaceViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);
         toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Driver dashboard");
        setSupportActionBar(toolbar);


        TabletAdsRoomDatabase.getDatabase(this);


        commonServices = new CommonServices(this);
        commonServices.setAdvertlayout("horizontal");


        progressBar = findViewById(R.id.progress_circular);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //identifying the device
        serial_number = android.os.Build.SERIAL;

        //SQLite database creation starts here
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                TabletAdsRoomDatabase.getDatabase(getBaseContext());
                ApplicationPreferenceCreator.setDownloadingStatus(getApplicationContext(),null);

            }
        });
        //initialize view model
        this.initialize();


        View headerView = navigationView.getHeaderView(0);
        fullName = (TextView) headerView.findViewById(R.id.full_name);
        email = (TextView)headerView.findViewById(R.id.email);
        profileImage = headerView.findViewById(R.id.driverProfileImage);
        //set view model value
        this.setView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.driver_dashboard, menu);
        return true;
    }

    public void addRegisterTabletFragment(){
        Fragment newFragment = new CarFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addHomeFragment(){
        Fragment newFragment = new com.example.tabadvertsbusiness.auth.view.fragments.HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addAboutThisTabletFragment(){
        Fragment newFragment = new AbouThisTabletFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void replaceFragment(){
        Fragment fragment = new AbouThisTabletFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_home) {
            // Handle the camera action
            fragment = new HomeFragment();
            toolbar.setTitle("Driver dashboard");
        } else if(id==R.id.nav_adverts){
            fragment = new MyAdvertsFragment();
            toolbar.setTitle("My adverts");
        } else if (id == R.id.nav_about_tablet) {
            fragment = new AbouThisTabletFragment();
            toolbar.setTitle("About this tablet");
        }else if(id==R.id.nav_file){
            fragment = new MyFilesFragment();
            toolbar.setTitle("My files");
        }else if (id==R.id.nav_logout){
            Constants.clearToken(getApplicationContext());
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (id==R.id.nav_my_cars){
            fragment = new CarFragment();
            toolbar.setTitle("My cars");
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        progressBar.setVisibility(View.GONE);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initialize(){
        viewModel = ViewModelProviders.of(this).get(MeViewModel.class);
        tabletAssignViewModel = ViewModelProviders.of(this).get(TabletAssignViewModel.class);
        tabletAssignedCarWorkPlaceViewModel = ViewModelProviders.of(this).get(TabletAssignedCarWorkPlaceViewModel.class);
        tabletViewModel = ViewModelProviders.of(this).get(TabletViewModel.class);
    }

    public void setView(){
        viewModel.me().observe(this,meResponse->{
            if(meResponse!=null){
                Constants.setUserId(meResponse.getData().getAttribute().getId(),getApplicationContext());
                fullName.setText(meResponse.getData().getAttribute().getFirst_name()+" "
                +meResponse.getData().getAttribute().getLast_name());
                email.setText(meResponse.getData().getAttribute().getEmail());
                if (meResponse.getData().getAttribute().getAvator().equals("letter")){
                    profileImage.setImageResource(R.drawable.ic_person_black_24dp);
                }else {

                }


                if(meResponse.getData().getRelations().getCars().size()<=0){
                    addNewCar();
                    progressBar.setVisibility(View.GONE);
                }else {

                    tabletViewModel.show(serial_number).enqueue(new Callback<TabletResponse>() {
                        @Override
                        public void onResponse(Call<TabletResponse> call, Response<TabletResponse> response) {
                            progressBar.setVisibility(View.GONE);
                            if(response.body().getData().size()<=0){
                                showTabletIsNotAssignedToCar();
                            }else {
                                if (response.body().getData().get(0).getCars().getWorking_place().size()<=0){
                                    showAboutThisTablet();
                                }else {
                                    showHome();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<TabletResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }



    public void showHome(){
        Fragment newFragment = new com.example.tabadvertsbusiness.auth.view.fragments.HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void showAboutThisTablet(){
        Fragment newFragment = new AbouThisTabletFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
    public void addNewCar(){
        Fragment newFragment = new RegisterNewCar();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void showDownloadRequestSubmmitedFragment(String message){
        Fragment newFragment = new DownloadRequestSubmittedFragment(message);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void showTabletIsNotAssignedToCar(){
        Fragment newFragment = new TabletNotAssignedToCarFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void showCarRegisterationFragment(){
        Fragment newFragment = new RegisterNewCar();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void showRegisterCarWorkPlaceFragment(Car car){
        Fragment newFragment = new RegisterCarWorkPlace(car);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
