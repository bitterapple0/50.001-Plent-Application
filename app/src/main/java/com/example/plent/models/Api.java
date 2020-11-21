package com.example.plent.models;
import com.example.plent.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @POST("user")
    Call<User> createUser(@Body User user);

    @GET("userlist")
    Call<ArrayList<User>> getUserList();
    }
