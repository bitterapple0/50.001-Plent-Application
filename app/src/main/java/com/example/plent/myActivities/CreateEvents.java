package com.example.plent.myActivities;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
    TextView date_picker;
    DatePickerDialog picker;

//    Integer month1;
//    Integer day1;
//    Integer year1;
    LocalDate eventDate;
    Event event;
    Bitmap posterBit;
    String imageFilename;
    String imageUrl;
    ActivityType type;

    // exit page
   public void ClosePage(View view) {
       finish();
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.CalendarTheme);
        setContentView(R.layout.create_event);
        api =  Api.getInstance().apiModel;

        // finding and setting views
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
        date_picker = findViewById(R.id.date_picker);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        types = findViewById(R.id.types);
        title_input = findViewById(R.id.title_input);
        location_input = findViewById(R.id.location_input);
        description_input = findViewById(R.id.description_input);
        telegram_input = findViewById(R.id.telegram_input);

        // setting spinner values
        String[] items = new String[]{"Fifth Row", "Industry Talk", "Student Life"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text_item, items);
        types.setAdapter(adapter);

        // setting date picker input type to null because EditText field for date picker is just for display purpose
        // clicking on the field will open a calendar component to select date
        date_picker.setInputType(InputType.TYPE_NULL);

        // set time picker to be 24 hours
        start_time.setIs24HourView(true);
        end_time.setIs24HourView(true);

        // button: next (submission of event information)
        // on click handler for submit button
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isCompleted()) {
                        // if all required fields are filled in (ie. everything except telegram and image)
                        // toast to let user know event creation is in progress
                        Toast.makeText(CreateEvents.this, "Creating event...", Toast.LENGTH_LONG).show();
                        // check if an image was uploaded
                        // upload image to Cloudinary if an image is present
                        if (imageFilename != null && !imageFilename.isEmpty()) {
                            MediaManager.get().upload(imageFilename).unsigned("iybnngkh").callback(new UploadCallback() {
                                @Override
                                public void onStart(String requestId) {}
                                @Override
                                public void onProgress(String requestId, long bytes, long totalBytes) {}
                                @Override
                                public void onSuccess(String requestId, Map resultData) {
                                    // create event only upon successful image upload
                                    imageUrl = resultData.get("secure_url").toString();
                                    createEvent();
                                    Log.i(TAG, "create event return url: " + imageUrl);
                                }

                                @Override
                                public void onError(String requestId, ErrorInfo error) {
                                    Log.i(TAG,"CLOUDINARY UPLOAD ERROR: " + error.getDescription());
                                    Toast.makeText(CreateEvents.this, "Oops, something went wrong. Please try again!", Toast.LENGTH_LONG).show();
                                }
                                @Override
                                public void onReschedule(String requestId, ErrorInfo error) {}
                            }).dispatch();
                        } else {
                            // if no image was uploaded, create event immediately
                            createEvent();
                        }
                    } else {
                        // feedback/error message for user
                        Toast.makeText(CreateEvents.this, "Oops, the form has not been completed yet!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // feedback/error message for user
                    Toast.makeText(CreateEvents.this, "Oops, something went wrong. Please try again!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // on click handler for uploading image
        upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Browse", "CLICKED");
                try {
                    // check if user has given permission to use external storage (gallery in this case)
                    if (ActivityCompat.checkSelfPermission(CreateEvents.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // ask for permission if no permission was given
                        Log.i("Browse", "AskingForPermission");
                        ActivityCompat.requestPermissions(CreateEvents.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_GET);
                    } else {
                        // redirect to gallery to select image
                        Log.i("Browse", "RedirectinigToGallery");
                        Intent intent = new Intent(); //Intent.ACTION_PICK
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            Log.i("Browse", "Success");
                            startActivityForResult(intent, REQUEST_IMAGE_GET);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Browse", e.toString());
                }
            }
        });

        // on click handler for date picker EditText field
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(CreateEvents.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        eventDate = LocalDate.of(year, monthOfYear+1, dayOfMonth);
                        if (eventDate.isBefore(LocalDate.now(ZoneId.of("Asia/Singapore")))) {
                            Toast.makeText(CreateEvents.this, "Oops, this date has already passed!", Toast.LENGTH_LONG).show();
                            eventDate = null;
                        } else {
                            date_picker.setText(String.format("%d / %d / %d", dayOfMonth, monthOfYear + 1, year));
                            date_picker.setTextAppearance(R.style.Login_Body);
                        }
                        }
                    }, year, month, day);
                picker.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            try {
                // display uploaded image in image view
                posterBit = MediaStore.Images.Media.getBitmap(CreateEvents.this.getContentResolver(), data.getData());
                uploaded_image.setImageBitmap(posterBit);
                // save file name of image for uploading onto Cloudinary upon form submission
                imageFilename = ImageUtils.getImageFilePath(this, data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createEvent() {
        // creating event object
        LocalTime startTime = LocalTime.of(start_time.getHour(), start_time.getMinute());
        LocalTime endTime = LocalTime.of(end_time.getHour(), end_time.getMinute());
        if (!startTime.isBefore(endTime)) {
            // check whether start time < end time
            Toast.makeText(CreateEvents.this, "Oops, it seems like your start time is before your end time!", Toast.LENGTH_LONG).show();
        } else if (LocalDate.now(ZoneId.of("Asia/Singapore")).isEqual(eventDate) && startTime.isBefore(LocalTime.now(ZoneId.of("Asia/Singapore")))) {
            // check whether start time has already passed
            Toast.makeText(CreateEvents.this, "Oops, it looks like your start time has already passed!", Toast.LENGTH_LONG).show();
        } else {
            type = ActivityType.valueOf(((types.getSelectedItem().toString()).toUpperCase()).replace(" ","_"));
            event = new Event(title_input.getText().toString().trim(), eventDate.toString(), startTime.toString(), endTime.toString(),
                    location_input.getText().toString().trim(), description_input.getText().toString().trim(),
                    telegram_input.getText().toString().trim(), type, imageUrl);

            // making API call
            Call<Event> call = api.createEvent(event);
            call.enqueue(new Callback<Event>() {
                @Override
                public void onResponse(Call<Event> call, Response<Event> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(CreateEvents.this, "An error1 occurred, please try again!", Toast.LENGTH_LONG).show();
                    } else {
                        // redirect back to Manage Events page upon successful creation
                        Toast.makeText(CreateEvents.this, "Event created!", Toast.LENGTH_LONG).show();
                        finish();
                        Log.i(TAG, "retrieved event id: " + response.body().getId());
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

    public boolean editTextIsEmpty(EditText editTextField) {
       return editTextField.getText().toString().trim().isEmpty();
    }

    public boolean isCompleted() {
        // telegram and image are optional
        // type, startTime and endTime always has default values
       return !editTextIsEmpty(title_input) && !editTextIsEmpty(location_input)
               && !editTextIsEmpty(description_input)
               && eventDate != null;
    }
}
