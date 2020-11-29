package com.example.plent.myActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plent.R;
import com.example.plent.models.ActivityType;
import com.example.plent.models.Event;
import com.example.plent.utils.Constants;
import com.example.plent.utils.ParticipantsAdapter;
import com.example.plent.utils.SearchRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import com.example.plent.models.ApiModel;

public class ManageEventsActivity extends MenuActivity {

    FloatingActionButton fab_add;
    private ApiModel api;
    final List<Event> organisedEvents = new ArrayList<>();
    RecyclerView recyclerView;
    SearchRecyclerAdapter manageEventRecyclerAdapter;

    Event e1 = new Event("Project Meeting", "30/11/2020", "8.30am", "8.30pm", "Digital Studio Lab", "Computational Structure Meeting", "no URL", ActivityType.STUDENT_LIFE, "no URL");
    Event e2 = new Event("Athletics Week 12 Training", "30/11/2020", "6.30pm", "8.30pm", "SUTD Track", "Weekly Training", "no URL", ActivityType.FIFTH_ROW, "no Image");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_event_activity);

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

        organisedEvents.add(e1);
        organisedEvents.add(e2);

        Log.i("Event", String.valueOf(organisedEvents));

        recyclerView = findViewById(R.id.manageEventRecyclerView);
        manageEventRecyclerAdapter = new SearchRecyclerAdapter(organisedEvents, "1004610", ManageEventsActivity.this);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(pLayoutManager);
        recyclerView.setAdapter(manageEventRecyclerAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ManageEventsActivity.this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    @Override
    protected void onResume() {
        super.onResume();
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

}
