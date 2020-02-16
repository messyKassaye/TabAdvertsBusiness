package com.example.tabadvertsbusiness;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabadvertsbusiness.auth.view.DownloaderDashboard;
import com.example.tabadvertsbusiness.auth.view.DriverDashboard;
import com.example.tabadvertsbusiness.constants.Constants;
import com.example.tabadvertsbusiness.http.MainHttpAdapter;
import com.example.tabadvertsbusiness.http.interfaces.LoginService;
import com.example.tabadvertsbusiness.models.LoginResponse;


import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginButton;
    private TextView errorShower;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar =(Toolbar)findViewById(R.id.my_toolbar);
        toolbar.setTitle("Tab adverts business");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = (EditText)findViewById(R.id.input_email);
        password = (EditText)findViewById(R.id.input_password);
        loginButton = (Button)findViewById(R.id.btn_login);
        errorShower = (TextView)findViewById(R.id.errorShower);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();
                if(emailValue==""){
                    errorShower.setText("Please enter your email or phone number");
                }else if(passwordValue==""){
                    errorShower.setText("Please enter your password");
                }else {

                    login(emailValue,passwordValue);
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    public void login(String email, String password){
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        Retrofit retrofit= MainHttpAdapter.getAuthApi();
        LoginService loginService = retrofit.create(LoginService.class);

       Call<LoginResponse> call = loginService.login(email,password);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.code()==403){
                        progressDialog.dismiss();
                        errorShower.setText("Incorrect email or password is used.");
                    }else if(response.code()==200){
                        if(response.body().getRole().getId()!=2&&response.body().getRole().getId()!=4){
                            progressDialog.dismiss();
                            errorShower.setText("This application is created for car owners only. please use our web app for your purpose");
                        }else {
                            if(response.body().getRole().getId()==2){
                                progressDialog.dismiss();
                                setToken(response.body().getToken());
                                Intent intent = new Intent(getApplicationContext(), DriverDashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else if (response.body().getRole().getId()==4){
                                progressDialog.dismiss();
                                setToken(response.body().getToken());
                                Intent intent = new Intent(getApplicationContext(), DownloaderDashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }

                    }else {
                        progressDialog.dismiss();
                        errorShower.setText("Something is not Good. This is not your mistake please get support from http://tabadvet.com/support");
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    if(t instanceof SocketTimeoutException){
                        progressDialog.dismiss();
                        errorShower.setText("It takes much time. Please check your connection");
                    }
                }
            });



    }

    public void setToken(String token){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(Constants.getTokenPrefence(),0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token",token);
        editor.commit();
    }

}
