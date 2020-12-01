package com.example.plent.myActivities;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.INVISIBLE;

// TODO: ADD CHECK FOR WHETHER USER IS ALR LOGGED IN
// TODO: EDIT AP CALL

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LOGIN";
    private SharedPreferences mPreferences;
    private FirebaseAuth mAuth;

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
        mAuth = FirebaseAuth.getInstance();
        api = Api.getInstance().apiModel;
FirebaseAuth.getInstance().signOut();
        // SKIPS LOGIN IF USER INFO IS ALR SAVED IN SHARED PREF
        Gson gson = new Gson();
        mPreferences = getSharedPreferences(Constants.SHARED_PREF_FILE, MODE_PRIVATE);
        String json = mPreferences.getString(Constants.USER_KEY, null);
        if (json == null) {
            Log.i(TAG, "is null");
        } else {
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
                    progressBar.playAnimation();
                    firebaseLogin();
//                    authenticateAndFetchUser();
                }
            }

            });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.cancelAnimation();
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, FindEventsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        }
    }

    private void firebaseLogin() {
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            authenticateAndFetchUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Incorrect credentials",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.cancelAnimation();
                        }
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
                    progressBar.cancelAnimation();
                }
                else {
                    userCred = response.body();

                    if (userCred == null) {
                        Toast.makeText(LoginActivity.this, "Looks like there is no account related to the entered email. Try using another email address or Create a new account", Toast.LENGTH_LONG).show();
                        progressBar.cancelAnimation();
                    } else {
                            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                            Gson gson = new Gson();
                            preferencesEditor.putString(Constants.USER_KEY, gson.toJson(userCred));
                            preferencesEditor.apply();

                            Intent intent = new Intent(LoginActivity.this, FindEventsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            progressBar.cancelAnimation();
                            startActivity(intent);
                            finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "[2] An error occurred, please try again!", Toast.LENGTH_LONG).show();
                progressBar.cancelAnimation();
            }
        });

    }
}