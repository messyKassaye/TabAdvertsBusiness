package com.example.tabadvertsbusiness.auth.view;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.example.tabadvertsbusiness.auth.response.TabletResponse;
import com.example.tabadvertsbusiness.auth.roomDB.TabletAdsRoomDatabase;
import com.example.tabadvertsbusiness.auth.services.CommonServices;
import com.example.tabadvertsbusiness.auth.view.fragments.AbouThisTabletFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.AddressFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.AdvertsFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.AllAdvertsFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.CarFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.DriverDownloadFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.FileFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.FinanceFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.HomeFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.RegisterNewCar;
import com.example.tabadvertsbusiness.auth.view.fragments.SettingFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.TodaysAdvertFragment;
import com.example.tabadvertsbusiness.auth.view.fragments.YesterdaysAdvertFragment;
import com.example.tabadvertsbusiness.auth.view_model.TabletViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.view_model.MeViewModel;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener,FinanceFragment.OnFragmentInteractionListener,
        FileFragment.OnFragmentInteractionListener,SettingFragment.OnFragmentInteractionListener,
        AdvertsFragment.OnFragmentInteractionListener, CarFragment.OnFragmentInteractionListener,
        AbouThisTabletFragment.OnFragmentInteractionListener, DriverDownloadFragment.OnFragmentInteractionListener,
        AddressFragment.OnFragmentInteractionListener,RegisterNewCar.OnFragmentInteractionListener,
        TodaysAdvertFragment.OnFragmentInteractionListener, YesterdaysAdvertFragment.OnFragmentInteractionListener,
        AllAdvertsFragment.OnFragmentInteractionListener{
    private static final String TAG = DriverDashboard.class.getSimpleName();
    private MeViewModel viewModel;
    private TextView fullName,email;
    private NavigationView navigationView;
    private String serial_number;
    private TabletViewModel tabletViewModel;
    private ProgressBar progressBar;
    private Response<TabletResponse> tabletResponseResponse;

    private CommonServices commonServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Driver dashboard");
        setSupportActionBar(toolbar);

        TabletAdsRoomDatabase.getDatabase(this);

        File file = getFilesDir();
        System.out.println("file: "+file.getName());

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


        //initialize view model
        this.initialize();


        View headerView = navigationView.getHeaderView(0);
        fullName = (TextView) headerView.findViewById(R.id.full_name);
        email = (TextView)headerView.findViewById(R.id.email);
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
        Fragment newFragment = new HomeFragment();
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
        } else if(id==R.id.nav_adverts){
            fragment = new AdvertsFragment();
        } else if (id == R.id.nav_about_tablet) {
            fragment = new AbouThisTabletFragment();
        }else if(id==R.id.nav_file){
            fragment = new FileFragment();
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

        tabletViewModel = ViewModelProviders.of(this).get(TabletViewModel.class);
    }

    public void setView(){
        viewModel.me().observe(this,meResponse->{
            if(meResponse!=null){
                fullName.setText(meResponse.getData().getAttribute().getFirst_name()+" "
                +meResponse.getData().getAttribute().getLast_name());
                email.setText(meResponse.getData().getAttribute().getEmail());


                if(meResponse.getData().getRelations().getCars().size()<=0){
                    addNewCar();
                }else {
                    tabletViewModel.show(serial_number).enqueue(new Callback<TabletResponse>() {
                        @Override
                        public void onResponse(Call<TabletResponse> call, Response<TabletResponse> response) {
                            progressBar.setVisibility(View.GONE);
                            if(response.body().getData().size()<=0){
                                showCars();
                            }else {
                                showHome();
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


    public void showAboutTablet(){
        Fragment newFragment = new AbouThisTabletFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void showHome(){
        Fragment newFragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
    public void addNewCar(){
        Fragment newFragment = new RegisterNewCar(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void showCars(){
        Fragment newFragment = new CarFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
