package com.example.plent.myActivities;

import java.io.File;
import java.io.IOException;
import java.util.*;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import android.os.*;
import android.util.*;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.utils.ObjectUtils;
import com.example.plent.R;
import com.example.plent.models.ActivityType;
import com.example.plent.models.ApiModel;
import com.example.plent.models.User;
import com.example.plent.models.Event;
import com.example.plent.utils.Api;
import com.example.plent.utils.Constants;
import com.example.plent.utils.ImageUtils;
import com.google.gson.Gson;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEvents extends AppCompatActivity {

    final static int REQUEST_IMAGE_GET = 1;
    final static String TAG = "CREATE_EVENT";

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
    ImageView uploaded_image;
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
    String imageFilename;
    String imageUrl;


    // exit page
   public void ClosePage(View view) {finish();}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Message", "Creating");
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
        uploaded_image = findViewById(R.id.uploaded_image);

        // create close function (set intent)


        // button: next (submission of event information)
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MediaManager.get().upload(imageFilename).unsigned("iybnngkh").callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {}
                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {}
                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            imageUrl = resultData.get("secure_url").toString();
                            Log.i(TAG, "create event return url: " + imageUrl);
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            Log.i(TAG,"CLOUDINARY UPLOAD ERROR: " + error.getDescription());
                        }
                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {}
                    }).dispatch();
                    createEvent();
                } catch (Exception e) {
                    Toast.makeText(CreateEvents.this, "Oops, something went wrong. Please try again!", Toast.LENGTH_LONG).show();
                }


            }
        });

        // uploading image button
        upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(CreateEvents.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CreateEvents.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_GET);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, REQUEST_IMAGE_GET);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // setting spinner values
        types = findViewById(R.id.types);
        String[] items = new String[]{"Fifth Row Activity", "Industry Talk", "Student Life"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text_item, items);
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
        type1 = null; //(ActivityType) types.getSelectedItem(); //ActivityType.valueOf(types.getSelectedItem().toString()); // not sure how to change string to activityType (asking xinyi)
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
        // posterBit ^ as retrieved from above

        String date1 = String.valueOf(day1) + String.valueOf(month1) + String.valueOf(year1);
        String start_time1 = String.valueOf(start_hour1) + ":" + String.valueOf(start_minute1);
        String end_time1 = String.valueOf(end_hour1) + ":" + String.valueOf(end_minute1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "on activity result called");
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            try {
                posterBit = MediaStore.Images.Media.getBitmap(CreateEvents.this.getContentResolver(), data.getData());
                uploaded_image.setImageBitmap(posterBit);
                imageFilename = ImageUtils.getImageFilePath(this, data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createEvent() {
        event = new Event(title1, date1, start_time1, end_time1, location1, description1, telegram1,
                type1, imageUrl);
        Call<Event> call = api.createEvent(event);
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(CreateEvents.this, "An error1 occurred, please try again!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CreateEvents.this, "Event created!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CreateEvents.this, ManageEventsActivity.class);
                    startActivity(intent);
                    finish();
                    Log.i("CREATE EVENT", "retrieved event id: " + response.body().getId());
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {

                t.printStackTrace();

                Toast.makeText(CreateEvents.this, "An error2 occurred, please try again!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
