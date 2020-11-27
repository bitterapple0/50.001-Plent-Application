package com.example.plent.myActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.plent.utils.NetworkImage;

import java.util.ArrayList;

public class FindEventsActivity extends AppCompatActivity {

    ArrayList<Integer> images = new ArrayList<>();
    ArrayList<Object> imagesId = new ArrayList<>();
    int permission = 1; // We need to replace this with the user's permission field
    ApiModel api;
    CardView fifth_row_events_card_view;
    CardView industry_talks_card_view;
    CardView student_life_card_view;
    LinearLayout fr_cluster_linear_layout;
    LinearLayout it_cluster_linear_layout;
    LinearLayout sl_cluster_linear_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_events_activity);
        api = Api.getInstance().apiModel;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // to replace with an arrayList of events
        images.add(R.drawable.athleticsposter);
        images.add(R.drawable.bands_poster);
        images.add(R.drawable.judo_poster);
        images.add(R.drawable.football_poster);
        images.add(R.drawable.robotics_poster);
        images.add(R.drawable.tennis_poster);

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

        //NetworkImage getImage = new NetworkImage();
        //getImage.execute();

        for (Integer image:images){
            createClusterCards("Fifth Row", image);
        }

        for (int i=0; i<6; i++){
            createClusterCards("Industry Talk", R.drawable.poster_placeholder1);
            createClusterCards("Student Life", R.drawable.poster_placeholder1);
        }



        // TODO: MOVE TO CREATE EVENT ACTIVITY ONCE THAT CLASS IS ADDED
//        Event event = new Event("Test event", "211120", "1900", "2100", "Zoom", "lorem impsum blah blah", "https://t.me/kiasufoodies");
//        Call call = api.createEvent(event);
//
//        call.enqueue(new Callback<Event>() {
//            @Override
//            public void onResponse(Call<Event> call, Response<Event> response) {
//                if (!response.isSuccessful()) {
//                    Toast.makeText(FindEventsActivity.this, "An error1 occurred, please try again!", Toast.LENGTH_LONG).show();
//                } else {
//                    Log.i("FIND EVENTS", "retrieved event id: " + response.body().getId());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Event> call, Throwable t) {
//
//                t.printStackTrace();
//
//                Toast.makeText(FindEventsActivity.this, "An error2 occurred, please try again!", Toast.LENGTH_LONG).show();
//            }
//        });
    }

    public void addImages() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem create_event = menu.findItem(R.id.manage_events);

        Log.i("Message", "Create Event Clicked");
        if (permission == 0) {
            create_event.setEnabled(false);
            Log.i("Message", "No permission");
        } else {
            Log.i("Message", "success");
            create_event.setEnabled(true);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.my_information) {
            Intent intent = new Intent(FindEventsActivity.this, MyInformationActivity.class);
            startActivity(intent);
        }

        if (id == R.id.my_calender) {
            Intent intent = new Intent(FindEventsActivity.this, CalendarActivity.class);
            startActivity(intent);
        }

        if (id == R.id.find_events_and_activities) {
            Intent intent = new Intent(FindEventsActivity.this, FindEventsActivity.class);
            startActivity(intent);
        }

        if (id == R.id.manage_events) {
            Intent intent = new Intent (FindEventsActivity.this, ManageEventsActivity.class);
            startActivity(intent);
        }

        if (id == R.id.search_events){
            Intent intent = new Intent(FindEventsActivity.this, SearchActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    public void redirectToEventsPage(View view){
        Intent intent = new Intent(FindEventsActivity.this, EventActivity.class);
        intent.putExtra(Constants.SELECTED_EVENT_KEY, Constants.SKIP_BACKEND ? "5fb96424fe88a67bb74b4289" : "5fb937bce230d0e3a9e2f912"); // 5fb937bce230d0e3a9e2f912 - actual id in db  // 5fb96424fe88a67bb74b4289 - dummy id
        startActivity(intent);
    }

    public void createClusterCards(String eventType, int image){
        View find_events_poster = View.inflate(this, R.layout.find_events_poster, null);

        // change to e.getType() == ActivityType.FIFTH_ROW
        if (eventType == "Fifth Row"){
            ImageView poster = find_events_poster.findViewById(R.id.find_events_poster);
            poster.setImageResource(image);
            //change to imageURL
            fr_cluster_linear_layout.addView(poster, fr_cluster_linear_layout.getChildCount());

        } else if (eventType == "Industry Talk"){
            //change to imageURL
            ImageView poster = find_events_poster.findViewById(R.id.find_events_poster);
            poster.setImageResource(image);
            it_cluster_linear_layout.addView(find_events_poster);

        } else if (eventType == "Student Life"){
            ImageView poster = find_events_poster.findViewById(R.id.find_events_poster);
            poster.setImageResource(image);
            sl_cluster_linear_layout.addView(find_events_poster, sl_cluster_linear_layout.getChildCount());
        }

    }


}
