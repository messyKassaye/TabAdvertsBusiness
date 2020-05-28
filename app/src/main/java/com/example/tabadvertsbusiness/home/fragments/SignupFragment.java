package com.example.tabadvertsbusiness.home.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.model.Tablet;
import com.example.tabadvertsbusiness.auth.response.TabletResponse;
import com.example.tabadvertsbusiness.auth.view.DriverDashboard;
import com.example.tabadvertsbusiness.auth.view_model.MeViewModel;
import com.example.tabadvertsbusiness.auth.view_model.TabletViewModel;
import com.example.tabadvertsbusiness.constants.Constants;
import com.example.tabadvertsbusiness.home.HomeActivity;
import com.example.tabadvertsbusiness.home.models.SignUp;
import com.example.tabadvertsbusiness.http.MainHttpAdapter;
import com.example.tabadvertsbusiness.http.interfaces.LoginService;
import com.example.tabadvertsbusiness.models.LoginResponse;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.tabadvertsbusiness.auth.commons.MainDialog.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;


public class SignupFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    // TODO: Rename parameter arguments, choose names that match
    private EditText firstName,lastName,email,phone,password;
    Button signUpButton;
    LoginButton fbSignUp;
    CallbackManager callbackManager;
    TextView firstNameLabel;
    LinearLayout formLayout,mainSignUpLayout;
    TextView errorShower;
    SignInButton googleSignIn;
    private GoogleApiClient googleSignInClient;
    private RelativeLayout checkLayout;
    private ProgressBar progressBar;
    private TextView textViewInfo;
    private Button exit;
    private TabletViewModel tabletViewModel;
    private MeViewModel meViewModel;

    private ProgressDialog progressDialog;
    private static final int GOOGLE_SIGN_IN = 1;
    private static final int FACEBOOK_SIGN_IN = 2;

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        formLayout = view.findViewById(R.id.formLayout);
        mainSignUpLayout = view.findViewById(R.id.mainSignUpLayout);
        progressDialog = new ProgressDialog(getContext(),
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering...");

        errorShower = view.findViewById(R.id.errorShower);
        firstName = view.findViewById(R.id.first_name);
        lastName = view.findViewById(R.id.last_name);
        email = view.findViewById(R.id.email_address);
        phone = view.findViewById(R.id.phone_number);
        password = view.findViewById(R.id.input_password);
        signUpButton = view.findViewById(R.id.signUpBtn);

        checkLayout = view.findViewById(R.id.checkingLayout);
        googleSignIn = view.findViewById(R.id.googleSignInButton);




        //facebook sign in
        fbSignUp = view.findViewById(R.id.fbSignUp);
        callbackManager = CallbackManager.Factory.create();
        fbSignUp.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (loginResult.getAccessToken().getUserId()!=null){
                    HomeActivity homeActivity =(HomeActivity)getContext();
                    homeActivity.showLogin();
                }else {
                }
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstNameValue = firstName.getText().toString();
                String lastNameValue = lastName.getText().toString();
                String emailValue = email.getText().toString();
                String phoneValue =  phone.getText().toString();
                String passwordValue = password.getText().toString();
                if (firstNameValue.equals("")){
                    errorShower.setText("Please enter your first name");
                }else if (lastNameValue.equals("")){
                    errorShower.setText("Please enter your last name");
                }else if (emailValue.equals("")){
                    errorShower.setText("Please enter your email");
                }else if (phoneValue.equals("")){
                    errorShower.setText("Please enter your phone");
                }else if (passwordValue.equals("")){
                    errorShower.setText("Please enter your password");
                }else {

                    progressDialog.show();
                    SignUp signUp = new SignUp(
                            firstNameValue,
                            lastNameValue,
                            emailValue,
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
                                         showCheckLayout(getView());
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

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //google sign in
                GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                googleSignInClient =new GoogleApiClient.Builder(getContext())
                        .enableAutoManage(getActivity(),SignupFragment.this::onConnectionFailed)
                        .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                        .build();
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleSignInClient);
                startActivityForResult(intent,GOOGLE_SIGN_IN);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(FACEBOOK_SIGN_IN,resultCode,data);

        if (resultCode == Activity.RESULT_OK){
            if(requestCode==GOOGLE_SIGN_IN){
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }else if (requestCode==FACEBOOK_SIGN_IN){
                Toast.makeText(getContext(),"Feco",Toast.LENGTH_LONG).show();
            }
        }


    }

    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            String fullName = result.getSignInAccount().getDisplayName();
            String email = result.getSignInAccount().getEmail();
            String firstName = fullName.substring(0,fullName.lastIndexOf(" "));
            String lastName = fullName.substring(fullName.lastIndexOf(" ")+1,fullName.length());
            phoneAndPassword(email,firstName,lastName);
        } else {
            Toast.makeText(getApplicationContext(), "Sign in cancel", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        GoogleSignInClient googleSignInClient=GoogleSignIn.getClient(getContext(),gso);
        googleSignInClient.signOut();

        LoginManager.getInstance().logOut();
    }

    public void phoneAndPassword(String email,String firstName,String lastName){
        HomeActivity homeActivity = (HomeActivity)getContext();
        homeActivity.showPhoneAndPassword(email,firstName,lastName);
    }

    public void setToken(String token){
        SharedPreferences preferences = getContext().getSharedPreferences(Constants.getTokenPrefence(),0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token",token);
        editor.commit();
    }

    public void showCheckLayout(View view){
        progressDialog.dismiss();
        mainSignUpLayout.setVisibility(View.GONE);
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

    public void checkOwnerOfThisTablet(List<Tablet> tablets){
        meViewModel.me().observe(this,meResponse -> {
            progressBar.setVisibility(View.GONE);
            if (meResponse.getData().getAttribute().getId()==tablets.get(0).getUser_id()){
                showDriverDashboard();
            }else {
                textViewInfo.setTextColor(Color.RED);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
