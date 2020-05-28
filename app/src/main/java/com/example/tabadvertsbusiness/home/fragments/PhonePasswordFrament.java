package com.example.tabadvertsbusiness.home.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.constants.Constants;
import com.example.tabadvertsbusiness.home.HomeActivity;
import com.example.tabadvertsbusiness.home.models.SignUp;
import com.example.tabadvertsbusiness.http.MainHttpAdapter;
import com.example.tabadvertsbusiness.http.interfaces.LoginService;
import com.example.tabadvertsbusiness.models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhonePasswordFrament extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private String email,firstName,lastName;

    private TextView welcome,signInSubInfo,errorShower;
    private EditText phone,password;
    private Button continueButton;
    private ProgressDialog progressDialog;
    public PhonePasswordFrament(String email,String firstName,String lastName) {
        // Required empty public constructor
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_phone_password_frament, container, false);

        progressDialog = new ProgressDialog(getContext(),
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering...");

        welcome = view.findViewById(R.id.signInWelcome);
        welcome.setText("Hello, "+firstName+" "+lastName);

        signInSubInfo = view.findViewById(R.id.signInSubInfo);
        signInSubInfo.setText("Thank for using our platform. Now it's time to add your phone number and your password.");

        errorShower = view.findViewById(R.id.errorShower);
        errorShower.setTextColor(Color.RED);

        phone = view.findViewById(R.id.phoneNumber);
        password = view.findViewById(R.id.inputPassword);
        continueButton = view.findViewById(R.id.continueBtn);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneValue = phone.getText().toString();
                String passwordValue = password.getText().toString();
                if (phoneValue.equals("")){
                    errorShower.setText("Please enter your phone");
                }else if (passwordValue.equals("")){
                    errorShower.setText("Please enter your password");
                }else {
                    progressDialog.show();
                    SignUp signUp = new SignUp(
                            firstName,
                            lastName,email,
                            phoneValue,
                            passwordValue,
                            2);
                    Retrofit retrofit= MainHttpAdapter.getAuthApi();
                    LoginService loginService = retrofit.create(LoginService.class);
                    loginService.signUP(signUp)
                            .enqueue(new Callback<LoginResponse>() {
                                @Override
                                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                    if (response.isSuccessful()){
                                        setToken(response.body().getToken());
                                        progressDialog.dismiss();
                                        HomeActivity homeActivity = (HomeActivity)getContext();
                                        homeActivity.showCheckTablet();

                                    }else {

                                        if (response.code()==409){
                                            progressDialog.dismiss();
                                            errorShower.setText("Some is register by your email or phone number. please change your email or phone number");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<LoginResponse> call, Throwable t) {

                                }
                            });

                }
            }
        });



        return view;
    }

    public void setToken(String token){
        SharedPreferences preferences = getContext().getSharedPreferences(Constants.getTokenPrefence(),0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token",token);
        editor.commit();
    }

}
