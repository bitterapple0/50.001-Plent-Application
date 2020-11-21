package com.example.plent.myActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.plent.R;
import com.example.plent.models.ApiModel;
import com.example.plent.models.Event;
import com.example.plent.models.User;
import com.example.plent.utils.Api;
import com.example.plent.utils.Constants;

import java.util.ArrayList;

public class FindEventsActivity extends AppCompatActivity {

    ArrayList<Integer> images = new ArrayList<>();
    ArrayList<Object> imagesId = new ArrayList<>();
    int permission = 1; // We need to replace this with the user's permission field
    ApiModel api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_events_activity);
        api = Api.getInstance().apiModel;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView fr_image_1 = findViewById(R.id.fr_image_1);
        ImageView fr_image_2 = findViewById(R.id.fr_image_2);
        ImageView fr_image_3 = findViewById(R.id.fr_image_3);
        ImageView fr_image_4 = findViewById(R.id.fr_image_4);
        ImageView fr_image_5 = findViewById(R.id.fr_image_5);
        ImageView fr_image_6 = findViewById(R.id.fr_image_6);

        images.add(R.drawable.athleticsposter);
        images.add(R.drawable.bands_poster);
        images.add(R.drawable.judo_poster);
        images.add(R.drawable.football_poster);
        images.add(R.drawable.robotics_poster);
        images.add(R.drawable.tennis_poster);

        fr_image_1.setImageResource(images.get(0));
        fr_image_2.setImageResource(images.get(1));
        fr_image_3.setImageResource(images.get(2));
        fr_image_4.setImageResource(images.get(3));
        fr_image_5.setImageResource(images.get(4));
        fr_image_6.setImageResource(images.get(5));

        CardView fifth_row_events_card_view = findViewById(R.id.fifth_row_events_card_view);
        TextView header = fifth_row_events_card_view.findViewById(R.id.cluster_header);
        header.setText("Fifth Row Activities");

        CardView industry_talks_card_view = findViewById(R.id.industry_talks_card_view);
        TextView header1 = industry_talks_card_view.findViewById(R.id.cluster_header);
        header1.setText("Industry Talks");

        CardView upcoming_events_card_view = findViewById(R.id.upcoming_events_card_view);
        TextView header2 = upcoming_events_card_view.findViewById(R.id.cluster_header);
        header2.setText("Upcoming Events");

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
            return true;
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

        return super.onOptionsItemSelected(item);
    }

    public void redirectToEventsPage(View view){
        Intent intent = new Intent(FindEventsActivity.this, EventActivity.class);
        intent.putExtra(Constants.SELECTED_EVENT_KEY, Constants.SKIP_BACKEND ? "5fb96424fe88a67bb74b4289" : "5fb937bce230d0e3a9e2f912"); // 5fb937bce230d0e3a9e2f912 - actual id in db  // 5fb96424fe88a67bb74b4289 - dummy id
        startActivity(intent);
    }

}
