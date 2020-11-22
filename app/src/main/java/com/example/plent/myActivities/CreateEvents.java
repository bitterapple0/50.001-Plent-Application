package com.example.plent.myActivities;

import java.util.*;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.*;
import android.os.*;
import android.util.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.plent.R;
import com.example.plent.models.ApiModel;
import com.example.plent.models.User;
import com.example.plent.models.Event;
import com.example.plent.utils.Api;
import com.example.plent.utils.Constants;
import com.google.gson.Gson;

public class CreateEvents extends AppCompatActivity {
    TextView create_event;
    Button submit;
    TextView event_title;
    EditText title_input;
    TextView event_type;
    Spinner types;
    TextView date;
    DatePicker enter_date;
    TextView time;
    TimePicker start_time;
    TextView to;
    TimePicker end_time;
    TextView location;
    EditText location_input;
    TextView description;
    EditText description_input;
    TextView poster;
    Button upload;
    TextView telegram;
    EditText telegram_input;
    ApiModel api;

    // local variables
    String title1;
    String type1;
    int month1;
    int day1;
    int year1;
    int start_hour1;
    int start_minute1;
    int end_hour1;
    int end_minute1;
    String location1;
    String description1;
    String telegram1;
    Event event;

    // exit page
    public void ClosePage(View view) {
        finish();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        api =  Api.getInstance().apiModel;

        // calling textView objects
        create_event = findViewById(R.id.create_event);
        event_title = findViewById(R.id.event_title);
        event_type = findViewById(R.id.event_type);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        to = findViewById(R.id.to);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        poster = findViewById(R.id.poster);
        telegram = findViewById(R.id.telegram);

        // create close function (set intent)


        // button: next (submission of event information)
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateEvents.this, "Your event has successfully been created!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CreateEvents.this, ManageEventsActivity.class);
                startActivity(intent);
            }
        });

        // uploading image button
        upload = findViewById(R.id.upload);
        final int REQUEST_IMAGE_GET = 1;
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }
            }

            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
                    Uri posterUri = data.getData();
                    Toast.makeText(CreateEvents.this, "Uploaded picture!", Toast.LENGTH_SHORT).show();
                }
            }
            
        });

       // getting all the values
        title_input = findViewById(R.id.title_input);
        types = findViewById(R.id.types);
        enter_date = findViewById(R.id.enter_date);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        location_input = findViewById(R.id.location_input);
        description_input = findViewById(R.id.description_input);
        telegram_input = findViewById(R.id.telegram_input);

        title1 = title_input.getText().toString();
        type1 = types.getSelectedItem().toString();
        month1 = enter_date.getMonth();
        day1 = enter_date.getDayOfMonth();
        year1 = enter_date.getYear();
        start_hour1 = start_time.getHour();
        start_minute1 = start_time.getMinute();
        end_hour1 = end_time.getHour();
        end_minute1 = end_time.getMinute();
        location1 = location_input.getText().toString();
        description1 = description_input.getText().toString();
        telegram1 = telegram_input.getText().toString();
        // posterUri ^ as retrieved from above

        String date = String.valueOf(day1) + String.valueOf(month1) + String.valueOf(year1);
        String start_time = String.valueOf(start_hour1) + ":" + String.valueOf(start_minute1);
        String end_time = String.valueOf(end_hour1) + ":" + String.valueOf(end_minute1);

    }

    public void CreateEvent(View view) {
        event = new Event();
    }

}
