package com.example.tabadvertsbusiness.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.view.fragments.CarFragment;
import com.example.tabadvertsbusiness.home.fragments.AboutUsFragment;
import com.example.tabadvertsbusiness.home.fragments.CheckTabletFragment;
import com.example.tabadvertsbusiness.home.fragments.ContactUsFragment;
import com.example.tabadvertsbusiness.home.fragments.HomeFragment;
import com.example.tabadvertsbusiness.home.fragments.HowItWorksFragment;
import com.example.tabadvertsbusiness.home.fragments.LoginFragment;
import com.example.tabadvertsbusiness.home.fragments.PhonePasswordFrament;
import com.example.tabadvertsbusiness.home.fragments.SignupFragment;
import com.example.tabadvertsbusiness.player.PlayerDashboard;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Smart display");
        toolbar.setTitleTextColor(Color.parseColor("#242424"));
        setSupportActionBar(toolbar);

        Fragment newFragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.home_content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();

        if(allPermissionsGranted()){
            return;
        } else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.languages_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment newFragment = null;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case R.id.home:
                newFragment = new HomeFragment();
                ft.replace(R.id.home_content_frame, newFragment);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            case R.id.how_it_works:
                // LoginResponse chose the "Settings" item, show the app settings UI...
                newFragment = new HowItWorksFragment();
                ft.replace(R.id.home_content_frame, newFragment);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            case R.id.contact_us:
                newFragment = new ContactUsFragment();
                ft.replace(R.id.home_content_frame, newFragment);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void showLogin(){
        Fragment newFragment = new LoginFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }


    public void showPlay(){
        Fragment newFragment = new PlayerDashboard();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void showSignUp(){
        Fragment newFragment = new SignupFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void showPhoneAndPassword(String email,String firstName,String lastName){
        Fragment newFragment = new PhonePasswordFrament(email,firstName,lastName);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void showCheckTablet(){
        Fragment newFragment = new CheckTabletFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_content_frame, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                startApp();
            } else{
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);

            }
        }
    }

    public void startApp(){

    }
}
