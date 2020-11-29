package com.example.plent.myActivities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.plent.R;
import com.example.plent.models.ApiModel;
import com.example.plent.models.User;
import com.example.plent.utils.Api;
import com.example.plent.utils.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.INVISIBLE;

// TODO: ADD CHECK FOR WHETHER USER IS ALR LOGGED IN
// TODO: EDIT AP CALL

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LOGIN";
    private SharedPreferences mPreferences;

    private EditText email;
    private EditText password;
    private CharSequence email_CS;
    private CharSequence password_CS;
    Button login;
    Button createAcc;
    ApiModel api;
    LottieAnimationView progressBar;

    User userCred;
    boolean emailInList = false;

    boolean completed = false;
    boolean disabled = true;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkCompleted() {
        completed = (password_CS != null && !password_CS.toString().isEmpty()
                && email_CS != null && !email_CS.toString().isEmpty());
        if (completed) {
            disabled = false;
            login.setTextAppearance(R.style.Primary_Button);
            login.setBackgroundResource(R.drawable.primary_button);
        } else {
        // else if (!completed){
            disabled = true;
            login.setTextAppearance(R.style.Disabled_Button);
            login.setBackgroundResource(R.drawable.disabled_button);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        api = Api.getInstance().apiModel;

        // SKIPS LOGIN IF USER INFO IS ALR SAVED IN SHARED PREF
        Gson gson = new Gson();
        mPreferences = getSharedPreferences(Constants.SHARED_PREF_FILE, MODE_PRIVATE);
        String json = mPreferences.getString(Constants.USER_KEY, null);
        if (json == null) {
            Log.i(TAG, "is null");
        } else {
            // TODO: UNCOMMENT THIS IF YOU DO NOT WANT TO SKIP LOGIN PAGE
//            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
//            preferencesEditor.remove(Constants.USER_KEY);
//            preferencesEditor.apply();
            Log.i(TAG, json);
            userCred = gson.fromJson(json, User.class);
            Intent intent = new Intent(LoginActivity.this, FindEventsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        }

        email = findViewById(R.id.emailInput);
        password =  findViewById(R.id.passwordInput);
        login = findViewById(R.id.login);
        createAcc = findViewById(R.id.createAcc);
        progressBar = findViewById(R.id.progressBar);

        email.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email_CS = s;
                checkCompleted();
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password_CS = s;
                checkCompleted();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (disabled == true) {
                    Toast.makeText(LoginActivity.this, "Please enter all credentials to proceed",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    if (Constants.SKIP_BACKEND) {
                        userCred = new User("TestUser", email.getText().toString(), "1001234", password.getText().toString());
                        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                        Gson gson = new Gson();
                        preferencesEditor.putString(Constants.USER_KEY, gson.toJson(userCred));
                        preferencesEditor.apply();

                        Intent intent = new Intent(LoginActivity.this, FindEventsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        finish();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        authenticateAndFetchUser();
                    }
                }
            }

            });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(INVISIBLE);
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    public void authenticateAndFetchUser(){
        Call<User> call = api.getUserCred(email.getText().toString());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "[1] An error occurred, please try again!", Toast.LENGTH_LONG).show();
                }
                else {
                    userCred = response.body();

                    if (userCred == null) {
                        Toast.makeText(LoginActivity.this, "Looks like there is no account related to the entered email. Try using another email address or Create a new account", Toast.LENGTH_LONG).show();
                    } else {
                        if (userCred.getPassword().equals(password.getText().toString())) {
                            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                            Gson gson = new Gson();
                            preferencesEditor.putString(Constants.USER_KEY, gson.toJson(userCred));
                            preferencesEditor.apply();

                            Intent intent = new Intent(LoginActivity.this, MyInformationActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            progressBar.setVisibility(INVISIBLE);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "[2] An error occurred, please try again!", Toast.LENGTH_LONG).show();
            }
        });

    }
}