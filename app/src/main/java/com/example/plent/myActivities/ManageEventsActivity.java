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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ManageEventsActivity extends AppCompatActivity {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

        // Add back button on top?
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.my_information) {
            if (id == R.id.my_calender) {
                Intent intent = new Intent(ManageEventsActivity.this, MyInformationActivity.class);
                startActivity(intent);
            }

            if (id == R.id.my_calender) {
                Intent intent = new Intent(ManageEventsActivity.this, CalendarActivity.class);
                startActivity(intent);
            }

            if (id == R.id.find_events_and_activities) {
                Intent intent = new Intent(ManageEventsActivity.this, FindEventsActivity.class);
                startActivity(intent);
            }

            if (id == R.id.manage_events) {
                Intent intent = new Intent(ManageEventsActivity.this, ManageEventsActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);

        }

    public void addEvent() {
        View calendar_card = View.inflate(this, R.layout.manage_event_card, null);

        Log.i("Message", String.valueOf(linearLayout.getChildCount()));

        linearLayout.addView(calendar_card, linearLayout.getChildCount());
    }

}
