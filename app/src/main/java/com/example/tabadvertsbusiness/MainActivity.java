package com.example.tabadvertsbusiness;

import android.content.Intent;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tabadvertsbusiness.player.PlayerDashboard;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private Timer timer;
    TextView welcome_and_wish;
    Button startAdvert,startUpdating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar =(Toolbar)findViewById(R.id.my_toolbar);
        toolbar.setTitle("Tab adverts business");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        welcome_and_wish = (TextView)findViewById(R.id.welcome_and_wish);
        final int data[] = {R.string.welcome,R.string.start_working_now,R.string.have_nice_time};
        welcome_and_wish.post(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                welcome_and_wish.setText(data[i]);
                i++;
                if (i ==3)
                    i = 0;
                welcome_and_wish.postDelayed(this, 5000);
            }
        });

        startAdvert = findViewById(R.id.start_advertising);
        startAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlayerDashboard.class);
                startActivity(intent);
            }
        });

        startUpdating = (Button)findViewById(R.id.start_updating);
        startUpdating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.languages_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                // LoginResponse chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    }
