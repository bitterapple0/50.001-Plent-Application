package com.example.plent.myActivities;

import java.io.IOException;
import java.util.*;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import android.os.*;
import android.util.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.plent.R;
import com.example.plent.models.ActivityType;
import com.example.plent.models.ApiModel;
import com.example.plent.models.User;
import com.example.plent.models.Event;
import com.example.plent.utils.Api;
import com.example.plent.utils.Constants;
import com.google.gson.Gson;

import retrofit2.Call;

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
    ActivityType type1;
    int month1;
    int day1;
    int year1;
    String date1;
    int start_hour1;
    int start_minute1;
    String start_time1;
    int end_hour1;
    int end_minute1;
    String end_time1;
    String location1;
    String description1;
    String telegram1;
    Event event;
    Bitmap posterBit;


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

            public void onActivityResult(int requestCode, int resultCode, Intent data) throws IOException {
                if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
                    Bitmap posterBit = MediaStore.Images.Media.getBitmap(CreateEvents.this.getContentResolver(), data.getData());
                    Toast.makeText(CreateEvents.this, "Uploaded picture!", Toast.LENGTH_SHORT).show();
                }
            }
            
        });
        // setting spinner values
        types = findViewById(R.id.types);
        String[] items = new String[]{"Fifth Row", "Industry Talks", "Student Life"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        types.setAdapter(adapter);

        // getting all the values
        title_input = findViewById(R.id.title_input);
        enter_date = findViewById(R.id.enter_date);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        location_input = findViewById(R.id.location_input);
        description_input = findViewById(R.id.description_input);
        telegram_input = findViewById(R.id.telegram_input);

        title1 = title_input.getText().toString();
        type1 = ActivityType.valueOf(types.getSelectedItem().toString()); // not sure how to change string to activityType (asking xinyi)
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

        String date1 = String.valueOf(day1) + String.valueOf(month1) + String.valueOf(year1);
        String start_time1 = String.valueOf(start_hour1) + ":" + String.valueOf(start_minute1);
        String end_time1 = String.valueOf(end_hour1) + ":" + String.valueOf(end_minute1);

    }

    public void createEvent(View view) {
        event = new Event(title1, date1, start_time1, end_time1, location1, description1, telegram1,
                type1, posterBit);
        Call<Event> call = api.createEvent(event);

        // not sure if have to do more?
    }



}
