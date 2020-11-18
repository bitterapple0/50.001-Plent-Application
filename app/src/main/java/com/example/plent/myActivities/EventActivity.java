package com.example.plent.myActivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.plent.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class EventActivity extends AppCompatActivity {

    Button signUpButton;
    Button joinTelegramGroupButton;
    TextView location;
    TextView timeDate;
    TextView eventHeader;
    ImageView eventPoster;
    TextView mainHeader;
    int permission = 0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);


        signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Message", "Button Clicked");
                Toast toast_success = Toast.makeText(EventActivity.this, R.string.success_toast, Toast.LENGTH_LONG);
                toast_success.setGravity(Gravity.CENTER_VERTICAL, 0,0 );
                toast_success.show();

            }
        });



        joinTelegramGroupButton = findViewById(R.id.join_telegram_group_button);
        joinTelegramGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Message", "Join Telegram Group Button Clicked");
                String url = "https://t.me/sutdathletics2020";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);


                /*Intent intent = new Intent(MainActivity.this, FindEventsActivity.class);
                startActivity(intent);*/

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this, CalendarActivity.class);
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
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem create_event = menu.findItem(R.id.create_event);

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
            Intent intent = new Intent(EventActivity.this, CalendarActivity.class);
            startActivity(intent);
        }

        if (id == R.id.find_events_and_activities) {
            Intent intent = new Intent(EventActivity.this, FindEventsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
