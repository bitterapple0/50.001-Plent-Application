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
import com.example.plent.models.ApiModel;
import com.example.plent.models.User;
import com.example.plent.utils.Api;

import java.util.ArrayList;
import java.util.List;

// TODO: ADD CHECK FOR WHETHER USER IS ALR LOGGED IN
// TODO: EDIT AP CALL

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private CharSequence email_CS;
    private CharSequence password_CS;
    Button login;
    Button createAcc;
    ApiModel api;

    ArrayList<User> userList;
    boolean emailInList = false;
    String TAG = "Logcat";

    int FIELDS = 2;
    int[] fieldIds;
    EditText[] inputFields = new EditText[FIELDS];
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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (disabled == true) {
                    Toast.makeText(LoginActivity.this, "Please enter all credentials to proceed",
                            Toast.LENGTH_LONG).show();
                }
                else {
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