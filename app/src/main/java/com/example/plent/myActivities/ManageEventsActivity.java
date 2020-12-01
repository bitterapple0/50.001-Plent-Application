package com.example.plent.myActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plent.R;
import com.example.plent.models.Event;
import com.example.plent.models.User;
import com.example.plent.utils.Api;
import com.example.plent.utils.Constants;
import com.example.plent.adapters.SearchRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.plent.models.ApiModel;
import com.google.gson.Gson;

public class ManageEventsActivity extends MenuActivity {

    private static final String TAG = "MANAGE EVENTS";
    FloatingActionButton fab_add;
    private ApiModel api;
    private SharedPreferences mPreferences;
    private User user;

    final List<Event> organisedEvents = new ArrayList<>();
    RecyclerView recyclerView;
    SearchRecyclerAdapter manageEventRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_event_activity);
        api = Api.getInstance().apiModel;
        mPreferences = getSharedPreferences(Constants.SHARED_PREF_FILE, MODE_PRIVATE);
        user = getUserFromSharedPref();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        fab_add = findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageEventsActivity.this, CreateEvents.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.manageEventRecyclerView);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(pLayoutManager);
        manageEventRecyclerAdapter = new SearchRecyclerAdapter(organisedEvents, "1004610", ManageEventsActivity.this);
        recyclerView.setAdapter(manageEventRecyclerAdapter);

        Log.i("Event", "manage events " + String.valueOf(organisedEvents));

    }

    @Override
    protected void onResume() {
        super.onResume();
        Call<ArrayList<Event>> call = api.getOrganisedEvents(user.getId());
        call.enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ManageEventsActivity.this, "An error1 occurred, please try again!", Toast.LENGTH_LONG).show();
                } else {
                    if (response.body() == null) {
                        // user is not an organiser
                        redirectToFindEvents();
                        Toast.makeText(ManageEventsActivity.this, "Hm, it seems that you're not an organiser", Toast.LENGTH_LONG).show();
                    } else {
                        boolean refreshCards = false;
                        ArrayList<String> eventIds = new ArrayList<>();
                        for (Event e: organisedEvents) {
                            eventIds.add(e.getId());
                        }
                        for (Event e: response.body()) {
                            if (!eventIds.contains(e.getId())) {
                                refreshCards = true;
                                organisedEvents.add(e);
                            }
                        }
                        if (!organisedEvents.isEmpty()) {
                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ManageEventsActivity.this, DividerItemDecoration.VERTICAL);
                            recyclerView.addItemDecoration(dividerItemDecoration);
                        }
                        if (refreshCards) {
                            manageEventRecyclerAdapter.refreshManageEvents(organisedEvents);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ManageEventsActivity.this, "An error2 occurred, please try again!", Toast.LENGTH_LONG).show();
            }
        });
    }



    // Made a few changes to the ManageEventsActivity as it was throwing errors
    // TODO Check changes
    /*public void addEvent() {
        View calendar_card = View.inflate(this, R.layout.manage_event_card, null);
        Log.i("Message", String.valueOf(linearLayout.getChildCount()));
        linearLayout.addView(calendar_card, linearLayout.getChildCount());
    }*/

    public void redirectToParticipantsActivity(View view) {
        Intent intentToParticipantsActivity = new Intent(ManageEventsActivity.this, ParticipantsActivity.class);
        intentToParticipantsActivity.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intentToParticipantsActivity.putExtra(Constants.SELECTED_EVENT_KEY, "5fb937bce230d0e3a9e2f912");
        startActivity(intentToParticipantsActivity);
    }

    public void redirectToFindEvents() {
        Intent intentToParticipantsActivity = new Intent(ManageEventsActivity.this, FindEventsActivity.class);
        intentToParticipantsActivity.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentToParticipantsActivity);
        finish();
    }

    private User getUserFromSharedPref() {
        Gson gson = new Gson();
        String json = mPreferences.getString(Constants.USER_KEY, null);

        if (json == null) {
            Log.i(TAG, "is null");
            // redirect to login page if no user info stored
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();

            return null;
        }
        return gson.fromJson(json, User.class);
    }

}