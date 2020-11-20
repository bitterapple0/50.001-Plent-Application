package com.example.plent.myActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.plent.R;

import java.util.ArrayList;

public class FindEventsActivity extends AppCompatActivity {

    ArrayList<Integer> images = new ArrayList<>();
    ArrayList<Object> imagesId = new ArrayList<>();
    int permission = 1; // We need to replace this with the user's permission field


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_events_activity);

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
        startActivity(intent);
    }

}
