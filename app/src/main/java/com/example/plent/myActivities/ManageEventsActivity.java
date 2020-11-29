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

import com.example.plent.R;
import com.example.plent.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ManageEventsActivity extends MenuActivity {

    FloatingActionButton fab_add;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_event_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        linearLayout = findViewById(R.id.upcoming_events);

        addEvent();
        addEvent();

        fab_add = findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageEventsActivity.this, CreateEvents.class);
                startActivity(intent);
            }
        });

    }



    // Made a few changes to the ManageEventsActivity as it was throwing errors
    // TODO Check changes
    public void addEvent() {
        View calendar_card = View.inflate(this, R.layout.manage_event_card, null);

        Log.i("Message", String.valueOf(linearLayout.getChildCount()));

        linearLayout.addView(calendar_card, linearLayout.getChildCount());
    }

    public void redirectToParticipantsActivity(View view) {
        Intent intentToParticipantsActivity = new Intent(ManageEventsActivity.this, ParticipantsActivity.class);
        intentToParticipantsActivity.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intentToParticipantsActivity.putExtra(Constants.SELECTED_EVENT_KEY, "5fb937bce230d0e3a9e2f912");
        startActivity(intentToParticipantsActivity);
    }

}
