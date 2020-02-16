package com.example.tabadvertsbusiness.auth.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.tabadvertsbusiness.R;

public class DownloaderDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader_dashboard);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("Dashboard");
        setSupportActionBar(toolbar);
    }
}
