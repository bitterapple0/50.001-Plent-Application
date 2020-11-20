package com.example.plent.myActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.plent.R;

public class ManageEventsActivity extends AppCompatActivity {

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
            return true;
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
            Intent intent = new Intent (ManageEventsActivity.this, ManageEventsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void addEvent() {
        View calendar_card =  View.inflate(this, R.layout.manage_event_card, null);

        Log.i("Message", String.valueOf(linearLayout.getChildCount()));

        linearLayout.addView(calendar_card, linearLayout.getChildCount());
    }
}
