package com.example.plent.myActivities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.plent.R;
import com.example.plent.models.Api;
import com.example.plent.models.User;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private CharSequence email_CS;
    private CharSequence password_CS;
    Button login;
    Button createAcc;
    Api api;

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

        email = findViewById(R.id.emailInput);
        password =  findViewById(R.id.passwordInput);
        login = findViewById(R.id.login);
        createAcc = findViewById(R.id.createAcc);

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



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/") // Same as the one in the SignUpActivity
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (disabled == true) {
                    Toast.makeText(LoginActivity.this, "Please enter all credentials to proceed",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    userCred();
//                    for (User u: userList){
//                        if (u.getEmail().toString() == email.getText().toString()) {
//                            emailInList = true;
//                            if (u.getPassword() == password.getText().toString()) {
//                                Intent intent = new Intent(LoginActivity.this, FindEventsActivity.class);
//                                startActivity(intent);
//                            }
//                            else {
//                                Toast.makeText(LoginActivity.this, "One or more of the entered credentials are incorrect. Please try again",
//                                        Toast.LENGTH_SHORT).show();
//                                email.setText(null);
//                                password.setText(null);
//                            }
//                        }
                    if (userCred.getPassword() == password.getText().toString()) {
                        Intent intent = new Intent(LoginActivity.this, FindEventsActivity.class);
                        startActivity(intent);
                            }
                    }
                    if (emailInList == false) {
                        Toast.makeText(LoginActivity.this, "Looks like there is no account related to the entered email. \n" +
                                "Try using another email address or Create a new account", Toast.LENGTH_SHORT).show();
                    }
                }

            });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void userCred(){
        Call<User> call = api.getUserCred(email.getText().toString());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "[1] An error occurred, please try again!", Toast.LENGTH_LONG).show();
                }
                else {
                    userCred = response.body();
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