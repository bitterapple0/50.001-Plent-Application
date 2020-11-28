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

import java.util.ArrayList;

public class FindEventsActivity extends MenuActivity {

    final static String TAG = "FIND EVENTS";
    final static String placeholderImageUrl = "https://res.cloudinary.com/dyaxu5mb4/image/upload/v1606499824/plent/poster_placeholder1_jgh6vd.png";

    ArrayList<Event> events = new ArrayList<>();
    int permission = 1; // We need to replace this with the user's permission field
    ApiModel api;
    CardView fifth_row_events_card_view;
    CardView industry_talks_card_view;
    CardView student_life_card_view;
    LinearLayout fr_cluster_linear_layout;
    LinearLayout it_cluster_linear_layout;
    LinearLayout sl_cluster_linear_layout;
    DisplayMetrics displayMetrics;

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
        fr_cluster_linear_layout = fifth_row_events_card_view.findViewById(R.id.event_poster_linear_layout);

        industry_talks_card_view = findViewById(R.id.industry_talks_card_view);
        TextView header1 = industry_talks_card_view.findViewById(R.id.cluster_header);
        header1.setText(R.string.industry_talks);
        it_cluster_linear_layout = industry_talks_card_view.findViewById(R.id.event_poster_linear_layout);

        student_life_card_view = findViewById(R.id.student_life_card_view);
        TextView header2 = student_life_card_view.findViewById(R.id.cluster_header);
        header2.setText(R.string.student_life);
        sl_cluster_linear_layout = student_life_card_view.findViewById(R.id.event_poster_linear_layout);

        for (int i=0; i<4; i++) {
            setLoadingCards(ActivityType.FIFTH_ROW, R.drawable.poster_placeholder1);
            setLoadingCards(ActivityType.INDUSTRY_TALK, R.drawable.poster_placeholder1);
            setLoadingCards(ActivityType.STUDENT_LIFE, R.drawable.poster_placeholder1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // retrieve all events from API
        retrieveEvents();
    }

    public void redirectToEventsPage(View view){
        Intent intent = new Intent(FindEventsActivity.this, EventActivity.class);
        intent.putExtra(Constants.SELECTED_EVENT_KEY, Constants.SKIP_BACKEND ? "5fb96424fe88a67bb74b4289" : "5fb937bce230d0e3a9e2f912"); // 5fb937bce230d0e3a9e2f912 - actual id in db  // 5fb96424fe88a67bb74b4289 - dummy id
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    
    public void redirectToSeeAll(View view) {
        Intent intent = new Intent(FindEventsActivity.this, SeeAllActivity.class);
        startActivity(intent);
    }

    // add a poster to each cluster
    public void createClusterCards(ActivityType eventType, String imageUrl){
        View find_events_poster = View.inflate(this, R.layout.find_events_poster, null);
        ImageView poster = find_events_poster.findViewById(R.id.find_events_poster);
        int imageHeight = ImageUtils.dpToPx(110, displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        int imageWidth = ImageUtils.dpToPx(80, displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        new NetworkImage(poster, imageHeight, imageWidth).execute(imageUrl);
        addToCluster(eventType, poster);
    }

    public void setLoadingCards(ActivityType eventType, int image){
        View find_events_poster = View.inflate(this, R.layout.find_events_poster, null);
        ImageView poster = find_events_poster.findViewById(R.id.find_events_poster);
        poster.setImageResource(image);
        addToCluster(eventType, poster);
    }

    private void addToCluster(ActivityType eventType, ImageView poster) {
        if (eventType == ActivityType.FIFTH_ROW){
            fr_cluster_linear_layout.addView(poster, fr_cluster_linear_layout.getChildCount());
        } else if (eventType == ActivityType.INDUSTRY_TALK){
            it_cluster_linear_layout.addView(poster, it_cluster_linear_layout.getChildCount());
        } else if (eventType == ActivityType.STUDENT_LIFE){
            sl_cluster_linear_layout.addView(poster, sl_cluster_linear_layout.getChildCount());
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
                    ArrayList<String> eventIds = new ArrayList<>();
                    for (Event e: events) {
                        eventIds.add(e.getId());
                    }
                    for (Event e: response.body()) {
                        if (!eventIds.contains(e.getId())) {
                            createClusterCards(e.getType(), e.getImageUrl());
                        }
                    }
                    if (events.size() == 0) {
                        for (int i=3; i>=0; i--) {
                            fr_cluster_linear_layout.removeViewAt(i);
                            it_cluster_linear_layout.removeViewAt(i);
                            sl_cluster_linear_layout.removeViewAt(i);
                        }
                    }
                    events = response.body();
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
