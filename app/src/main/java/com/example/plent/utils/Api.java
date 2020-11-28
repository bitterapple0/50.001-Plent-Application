package com.example.plent.utils;

import com.example.plent.models.ApiModel;
import com.example.plent.models.User;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    public volatile ApiModel apiModel;
    static volatile Api api;

    private Api() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://secure-scrubland-16082.herokuapp.com/")
                //.baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiModel = retrofit.create(ApiModel.class);
    }

    public static Api getInstance() {
        if (api == null)
        {
            // To make thread safe
            synchronized (Api.class)
            {
                // check again as multiple threads
                // can reach above step
                if (api==null)
                    api = new Api();
            }
        }
        return api;
    }



}
