package com.example.plent.myActivities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;


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
    Button login;
    Button createAcc;
    Api api;
    ArrayList<User> userList;
    boolean emailInList = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email = findViewById(R.id.emailInput);
        password =  findViewById(R.id.passwordInput);
        login = findViewById(R.id.login);
        createAcc = findViewById(R.id.createAcc);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/") // Same as the one in the SignUpActivity
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                for (User u: userList){
                    if (u.getEmail().toString() == email.getText().toString()) {
                        emailInList = true;
                        if (u.getPassword() == password.getText().toString()) {
                            Intent intent = new Intent(LoginActivity.this, FindEventsActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "One or more of the entered credentials are incorrect. Please try again",
                                    Toast.LENGTH_SHORT).show();
                            email.setText(null);
                            password.setText(null);
                        }
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

    public void userList(){
        Call<ArrayList<User>> call = api.getUserList();
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "[1] An error occurred, please try again!", Toast.LENGTH_LONG).show();
                }
                else {
                    userList = response.body();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "[2] An error occurred, please try again!", Toast.LENGTH_LONG).show();
            }
        });

    }
}