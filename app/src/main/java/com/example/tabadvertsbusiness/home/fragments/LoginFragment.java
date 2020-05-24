package com.example.tabadvertsbusiness.home.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.model.Tablet;
import com.example.tabadvertsbusiness.auth.response.TabletResponse;
import com.example.tabadvertsbusiness.auth.view.DownloaderDashboard;
import com.example.tabadvertsbusiness.auth.view.DriverDashboard;
import com.example.tabadvertsbusiness.auth.view_model.MeViewModel;
import com.example.tabadvertsbusiness.auth.view_model.TabletViewModel;
import com.example.tabadvertsbusiness.constants.Constants;
import com.example.tabadvertsbusiness.http.MainHttpAdapter;
import com.example.tabadvertsbusiness.http.interfaces.LoginService;
import com.example.tabadvertsbusiness.models.LoginResponse;


import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginFragment extends Fragment {

    private EditText email, password;
    private Button loginButton;
    private TextView errorShower;
    private LinearLayout loginLayout;
    private RelativeLayout checkLayout;
    private ProgressBar progressBar;
    private TextView textViewInfo;
    private Button exit;
    private TabletViewModel tabletViewModel;
    private MeViewModel meViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_login,container,false);

        loginLayout = view.findViewById(R.id.loginLayout);
        checkLayout = view.findViewById(R.id.checkingLayout);

        email = view.findViewById(R.id.input_email);

        password = view.findViewById(R.id.input_password);
        loginButton = view.findViewById(R.id.btn_login);
        errorShower = view.findViewById(R.id.errorShower);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();
                if(emailValue.equals("")){
                    errorShower.setText("Please enter your email or phone number");
                }else if(passwordValue.equals("")){
                    errorShower.setText("Please enter your password");
                }else {

                    login(emailValue,passwordValue);
                }
            }
        });

        return view;

    }

    public void login(String email, String password){
        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
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
                                showCheckLayout(getView());
                            }else if (response.body().getRole().getId()==4){
                                progressDialog.dismiss();
                                setToken(response.body().getToken());
                                Intent intent = new Intent(getContext(), DownloaderDashboard.class);
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
        SharedPreferences preferences = getContext().getSharedPreferences(Constants.getTokenPrefence(),0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token",token);
        editor.commit();
    }

    public void showCheckLayout(View view){
        loginLayout.setVisibility(View.GONE);
        checkLayout.setVisibility(View.VISIBLE);

        progressBar = view.findViewById(R.id.checkPr);
        textViewInfo = view.findViewById(R.id.checkingInfo);
        exit = view.findViewById(R.id.checkingExit);

        tabletViewModel = ViewModelProviders.of(this).get(TabletViewModel.class);
        meViewModel = ViewModelProviders.of(this).get(MeViewModel.class);
        String serail_number = Build.SERIAL;
        tabletViewModel.show(serail_number).enqueue(new Callback<TabletResponse>() {
            @Override
            public void onResponse(Call<TabletResponse> call, Response<TabletResponse> response) {
                if (response.body().getData().size()<=0){
                    progressBar.setVisibility(View.GONE);
                    showDriverDashboard();
                }else {
                    checkOwnerOfThisTablet(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<TabletResponse> call, Throwable t) {

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearToken();
                getActivity().finish();
            }
        });


    }

    public void checkOwnerOfThisTablet(List<Tablet> tablets){
        meViewModel.me().observe(this,meResponse -> {
            progressBar.setVisibility(View.GONE);
            if (meResponse.getData().getAttribute().getId()==tablets.get(0).getUser_id()){
                showDriverDashboard();
            }else {
                textViewInfo.setText("Sorry. This tablet is assigned for some one else not for you.");
                exit.setVisibility(View.VISIBLE);
                clearToken();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearToken();
                getActivity().finish();
            }
        });
    }

    public void showDriverDashboard(){
        Intent intent = new Intent(getContext(), DriverDashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void clearToken(){
        SharedPreferences preferences =getActivity().getSharedPreferences(Constants.getTokenPrefence(), 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
