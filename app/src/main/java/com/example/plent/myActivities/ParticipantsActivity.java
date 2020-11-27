package com.example.plent.myActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.example.plent.models.User;
import com.example.plent.utils.ParticipantsAdapter;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsActivity extends AppCompatActivity {

    private List<User> participantsList= new ArrayList<>();
    private RecyclerView recyclerView;
    private ParticipantsAdapter participantsAdapter;
    User p1, p2, p3, p4;
    ImageView placeholder_participants;
    TextView placeholder_participants_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participants_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        placeholder_participants = findViewById(R.id.placeholder_participants);
        placeholder_participants_text = findViewById(R.id.placeholder_participants_text);

        recyclerView = findViewById(R.id.participantsRecyclerView);

        /*p1 = new User("Student 1", "student1@mymail.sutd.edu.sg", "1004610", "password");
        p2 = new User("Student 2", "student2@mymail.sutd.edu.sg", "1004320", "password");
        p3 = new User("Student 3", "student3@mymail.sutd.edu.sg", "1004324", "password");
        p4 = new User("Student 4", "student4@mymail.sutd.edu.sg", "1004612", "password");

        participantsList.add(p1);
        participantsList.add(p2);
        participantsList.add(p3);
        participantsList.add(p4);*/

        participantsAdapter = new ParticipantsAdapter(participantsList);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(pLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(participantsAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        if (participantsList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            placeholder_participants.setVisibility(View.VISIBLE);
            placeholder_participants_text.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            placeholder_participants.setVisibility(View.GONE);
            placeholder_participants_text.setVisibility(View.GONE);
        }


    }

    private void prepareParticipantsList() {
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
                Intent intent = new Intent(ParticipantsActivity.this, MyInformationActivity.class);
                startActivity(intent);
            }

            if (id == R.id.my_calender) {
                Intent intent = new Intent(ParticipantsActivity.this, CalendarActivity.class);
                startActivity(intent);
            }

            if (id == R.id.find_events_and_activities) {
                Intent intent = new Intent(ParticipantsActivity.this, FindEventsActivity.class);
                startActivity(intent);
            }

            if (id == R.id.manage_events) {
                Intent intent = new Intent(ParticipantsActivity.this, ManageEventsActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);

    }
}
