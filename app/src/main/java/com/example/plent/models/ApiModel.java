package com.example.plent.models;
import com.example.plent.models.User;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiModel {

    @POST("user")
    Call<User> createUser(@Body User user);

    @GET("user")
    Call<User> getUserCred(@Query("email") String email );

    @GET("event")
    Call<Event> getEvent(@Query("event_id") String eventId, @Query("user_id") String userId);

    @POST("event")
    Call<Event> createEvent(@Body Event event);

    @PUT("event")
    Call<HashMap> signUp(@Body HashMap userAndEvent);
}
