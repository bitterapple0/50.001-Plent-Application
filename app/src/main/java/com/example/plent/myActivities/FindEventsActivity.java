package com.example.plent.myActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.plent.R;
import com.example.plent.models.ActivityType;
import com.example.plent.models.ApiModel;
import com.example.plent.models.Event;
import com.example.plent.models.User;
import com.example.plent.utils.Api;
import com.example.plent.utils.Constants;
import com.example.plent.utils.ImageUtils;
import com.example.plent.utils.NetworkImage;
import com.example.plent.utils.NetworkImageCallback;
import com.example.plent.utils.SearchRecyclerAdapter;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class FindEventsActivity extends MenuActivity {

    final static String TAG = "FIND EVENTS";
    final static String placeholderImageUrl = "https://res.cloudinary.com/dyaxu5mb4/image/upload/v1606499824/plent/poster_placeholder1_jgh6vd.png";
    Event testEvent = new Event("Athletics Intro Session", LocalDate.of(2020, 11, 29).toString(), LocalTime.of(9, 0).toString(), LocalTime.of(12, 0).toString(), "SUTD Track", "hello", "", ActivityType.FIFTH_ROW,"https://abhisekmishra.com/project4_3.jpg");
    Event emptyEvent = new Event("Loading...",LocalDate.of(2020, 11, 29).toString(), LocalTime.of(9, 0).toString(), LocalTime.of(12, 0).toString(), "", "", "", null,""); ;
    ArrayList<Event> events = new ArrayList<>();
    ArrayList<Event> fifthRowEvents = new ArrayList<>();
    ArrayList<Event> studentLifeEvents = new ArrayList<>();
    ArrayList<Event> industryTalkEvents = new ArrayList<>();
    int permission = 1; // We need to replace this with the user's permission field
    ApiModel api;
    CardView fifth_row_events_card_view;
    CardView industry_talks_card_view;
    CardView student_life_card_view;
    RecyclerView fr_cluster_recyclerView,it_cluster_recyclerView,sl_cluster_recyclerView;
    SearchRecyclerAdapter fr_adapter, it_adapter, sl_adapter;
    DisplayMetrics displayMetrics;
    TextView fr_button, sl_button, it_button;
    TextView seeAllfr, seeAllsl, seeAllit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_events_activity);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        api = Api.getInstance().apiModel;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // initialise the layouts
        fifth_row_events_card_view = findViewById(R.id.fifth_row_events_card_view);
        TextView header = fifth_row_events_card_view.findViewById(R.id.cluster_header);
        header.setText(R.string.fifth_row_activities);
        fr_cluster_recyclerView = fifth_row_events_card_view.findViewById(R.id.horizontal_recycler_view);
        seeAllfr = fifth_row_events_card_view.findViewById(R.id.see_all_button);

        industry_talks_card_view = findViewById(R.id.industry_talks_card_view);
        TextView header1 = industry_talks_card_view.findViewById(R.id.cluster_header);
        header1.setText(R.string.industry_talks);
        seeAllit = industry_talks_card_view.findViewById(R.id.see_all_button);
        it_cluster_recyclerView = industry_talks_card_view.findViewById(R.id.horizontal_recycler_view);

        student_life_card_view = findViewById(R.id.student_life_card_view);
        TextView header2 = student_life_card_view.findViewById(R.id.cluster_header);
        header2.setText(R.string.student_life);
        seeAllsl = student_life_card_view.findViewById(R.id.see_all_button);
        sl_cluster_recyclerView = student_life_card_view.findViewById(R.id.horizontal_recycler_view);

        for (int i = 0; i < 6; i++) {
            fifthRowEvents.add(emptyEvent);
            industryTalkEvents.add(emptyEvent);
            studentLifeEvents.add(emptyEvent);
        }
        fr_adapter = new SearchRecyclerAdapter(fifthRowEvents, ActivityType.FIFTH_ROW, this);
        fr_cluster_recyclerView.setAdapter(fr_adapter);
        it_adapter = new SearchRecyclerAdapter(industryTalkEvents, ActivityType.INDUSTRY_TALK, this);
        it_cluster_recyclerView.setAdapter(it_adapter);
        sl_adapter = new SearchRecyclerAdapter(studentLifeEvents, ActivityType.STUDENT_LIFE, this);
        sl_cluster_recyclerView.setAdapter(sl_adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("HELOOOO" , "On Resume CAlled");
        // retrieve all events from API
        retrieveEvents();
        Log.i("WHAT IS INSIDE MY EVENTSSSS" , events.toString());

    }

    public void redirectToEventsPage(View view){
        Intent intent = new Intent(FindEventsActivity.this, EventActivity.class);
        intent.putExtra(Constants.SELECTED_EVENT_KEY, Constants.SKIP_BACKEND ? "5fb96424fe88a67bb74b4289" : "5fb937bce230d0e3a9e2f912"); // 5fb937bce230d0e3a9e2f912 - actual id in db  // 5fb96424fe88a67bb74b4289 - dummy id
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    
    public void redirectToSeeAll(View view) {

        fr_button = findViewById(R.id.fifth_row_events_card_view).findViewById(R.id.see_all_button);
        sl_button = findViewById(R.id.student_life_card_view).findViewById(R.id.see_all_button);
        it_button = findViewById(R.id.industry_talks_card_view).findViewById(R.id.see_all_button);
        //get the id of the view clicked. (in this case button)
        if (fr_button.equals(view)) { // if its button1
            Intent intent1 = new Intent(FindEventsActivity.this, SeeAllActivity.class);
            intent1.putExtra("EventType", ActivityType.FIFTH_ROW.toString());
            startActivity(intent1);
        } else if (sl_button.equals(view)) {
            Intent intent2 = new Intent(FindEventsActivity.this, SeeAllActivity.class);
            intent2.putExtra("EventType", ActivityType.STUDENT_LIFE.toString());
            startActivity(intent2);
        } else if (it_button.equals(view)) {
            Intent intent3 = new Intent(FindEventsActivity.this, SeeAllActivity.class);
            intent3.putExtra("EventType", ActivityType.INDUSTRY_TALK.toString());
            startActivity(intent3);
        } else {
            Intent intent = new Intent(FindEventsActivity.this, SeeAllActivity.class);
            startActivity(intent);
        }

    }

    // override to only show search option on this page
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search_icon = menu.findItem(R.id.search_events);
        search_icon.setVisible(true);
        return true;
    }

    public void retrieveEvents() {
        Call<ArrayList<Event>> call = api.getAllEvents();
        call.enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(FindEventsActivity.this, "An error1 occurred, please try again!", Toast.LENGTH_LONG).show();
                } else {
                    boolean refreshCards = false;
                    ArrayList<String> eventIds = new ArrayList<>();
                    for (Event e: events) {
                        eventIds.add(e.getId());
                    }
                    for (Event e: response.body()) {
                        if (!eventIds.contains(e.getId())) {
                            refreshCards = true;
                        }
                    }
                    if (refreshCards) {
                        events = response.body();
                        fr_adapter.refreshEvents(events, ActivityType.FIFTH_ROW, null);
                        it_adapter.refreshEvents(events, ActivityType.INDUSTRY_TALK, null);
                        sl_adapter.refreshEvents(events, ActivityType.STUDENT_LIFE, null);
                    }
                    Log.i(TAG, "find events " +events.toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(FindEventsActivity.this, "An error2 occurred, please try again!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
