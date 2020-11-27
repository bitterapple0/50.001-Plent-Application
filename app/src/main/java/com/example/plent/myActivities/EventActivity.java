package com.example.plent.myActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.plent.utils.DateTimeUtils;
import com.example.plent.utils.NetworkImage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.HashMap;


public class EventActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private User user;
    private Event event;
    private String eventId;
    private static final String TAG = "EVENT";
    ApiModel api;

    Button signUpButton;
    Button joinTelegramGroupButton;
    TextView location;
    TextView timeDate;
    TextView eventHeader;
    ImageView eventPoster;
    TextView mainHeader;
    TextView description;
    TextView clashText;
    LinearLayout event_activity_linear_layout;

    int permission = 1; // We need to replace this with the user's permission field

    void backToFindEvents() {
        setTheme(R.style.CalendarTheme);
        Toast.makeText(this, "Oops, this event could not be fetched!", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        // get instance of api model
        api =  Api.getInstance().apiModel;

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            // if no event id is stored, bring them back to find events activity screen
            backToFindEvents();
        } else {
            eventId = extras.getString(Constants.SELECTED_EVENT_KEY);
            if (eventId == null) {
                // if no event id is stored, bring them back to find events activity screen
                backToFindEvents();
            }
        }

        location = findViewById(R.id.location);
        timeDate = findViewById(R.id.time_date);
        eventHeader = findViewById(R.id.event_header);
        eventPoster = findViewById(R.id.athletics_poster);
        description = findViewById(R.id.post_body);
        clashText = findViewById(R.id.warning1);
        event_activity_linear_layout = findViewById(R.id.event_activity_linear_layout);

        joinTelegramGroupButton = findViewById(R.id.join_telegram_group_button);

        joinTelegramGroupButton.setVisibility(View.INVISIBLE);


        Gson gson = new Gson();
        mPreferences = getSharedPreferences(Constants.SHARED_PREF_FILE, MODE_PRIVATE);
        String json = mPreferences.getString(Constants.USER_KEY, null);
        if (json == null) {
            Log.i(TAG, "is null");
            // redirect to login page if no user info stored
            Intent intent = new Intent(EventActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        } else {
            Log.i(TAG, json);
            user = gson.fromJson(json, User.class);

            // fetch event info from db and check for clashes with user's events
            if (Constants.SKIP_BACKEND) {
                int[] date = {12, 10, 2020};
                int[] startTime = {18, 30};
                int[] endTime = {20, 30};
                event = new Event("Athletics Freshmore Intro Session", date, startTime, endTime, "SUTD Stadium", "Always had a passion for running or just want to maintain your fitness goals? Athletics club is here for you! Come down to experience what our training would be like and join us for a run to Simpang afterwards for supper after burning those calories!", "https://t.me/sutdathletics2020", ActivityType.FIFTH_ROW, "https://res.cloudinary.com/dyaxu5mb4/image/upload/v1605984445/athleticsposter_lhtcqv.png");
                if (!event.getTelegram().isEmpty()) {
                    // if there is no telegram link provided, no Join Telegram Group Button will be shown
                    joinTelegramGroupButton.setVisibility(View.VISIBLE);
                }
                eventHeader.setText(event.getTitle());
                location.setText(event.getLocation());
                timeDate.setText(DateTimeUtils.formatEventDateTime(event.getDate(), event.getStartTime(), event.getEndTime()));
                description.setText(event.getDescription());
                clashText.setText(event.getClashString());
                try {
                    new NetworkImage(eventPoster).execute(event.getImageUrl());
                } catch (Exception e) {
                    Log.e(TAG, "Could not get poster image");
                }
            } else {
                Call<Event> call = api.getEvent(eventId, user.getId());
                call.enqueue(new Callback<Event>() {
                    @Override
                    public void onResponse(Call<Event> call, Response<Event> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(EventActivity.this, "An error1 occurred, please try again!", Toast.LENGTH_LONG).show();
                        } else {
                            event = response.body();
                            if (event == null) {
                                backToFindEvents();
                            } else {
                                if (!event.getTelegram().isEmpty()) {
                                    // if there is no telegram link provided, no Join Telegram Group Button will be shown
                                    joinTelegramGroupButton.setVisibility(View.VISIBLE);
                                }
                                eventHeader.setText(event.getTitle());
                                location.setText(event.getLocation());
                                timeDate.setText(DateTimeUtils.formatEventDateTime(event.getDate(), event.getStartTime(), event.getEndTime()));
                                description.setText(event.getDescription());
                                clashText.setText(event.getClashString());
                                Log.i(TAG, "clash string: " + event.getClashString());
                                try {
                                    new NetworkImage(eventPoster).execute(event.getImageUrl());
                                } catch (Exception e) {
                                    Log.e(TAG, "Could not get poster image");
                                }
                            }
                            Log.i(TAG, "Retrieved event id: " + event.getId());

                            if (!event.getTelegram().isEmpty()) {
                                // if there is no telegram link provided, no Join Telegram Group Button will be shown
                                joinTelegramGroupButton.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Event> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(EventActivity.this, "An error2 occurred, please try again!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        signUpButton = findViewById(R.id.sign_up_button);
        if (user.getEvents().contains(eventId)) {
            signUpButton.setTextAppearance(R.style.Cancel_Button);
            signUpButton.setBackgroundResource(R.drawable.cancel_button_stroke);
            signUpButton.setText("Cancel Sign Up");
        }
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.SKIP_BACKEND) {
                    final boolean alreadyGoing = user.getEvents().contains(eventId);
                    Toast toast_success = Toast.makeText(EventActivity.this, alreadyGoing ? R.string.remove_attendance_success_toast : R.string.sign_up_success_toast, Toast.LENGTH_LONG);
                    toast_success.setGravity(Gravity.CENTER_VERTICAL, 0,0 );
                    toast_success.show();

                    if (alreadyGoing) {
                        Log.i("Dialog", "Reached Here");
                        AlertDialog.Builder confirmCancel = new AlertDialog.Builder(EventActivity.this, R.style.AlertDialogCustom);
                        confirmCancel.setTitle("Cancel sign up for " + eventHeader.getText() + " ?");
                        confirmCancel.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                user.cancelAttendance(eventId);
                                event.removeAttendee(user.getId());
                                signUpButton.setText("Sign Up Now");
                                signUpButton.setTextAppearance(R.style.Primary_Button);
                                signUpButton.setBackgroundResource(R.drawable.primary_button);
                            }
                        });
                        confirmCancel.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = confirmCancel.create();
                        alert.show();

                        /*user.cancelAttendance(eventId);
                        event.removeAttendee(user.getId());
                        signUpButton.setText("Sign Up Now");
                        signUpButton.setTextAppearance(R.style.Primary_Button);
                        signUpButton.setBackgroundResource(R.drawable.primary_button); */
                    } else {
                        user.signUp(eventId);
                        event.addAttendee(user.getId());
                        signUpButton.setTextAppearance(R.style.Cancel_Button);
                        signUpButton.setBackgroundResource(R.drawable.cancel_button_stroke);
                        signUpButton.setText("Cancel Sign Up");
                    }

                    SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                    Gson gson = new Gson();
                    preferencesEditor.putString(Constants.USER_KEY, gson.toJson(user));
                    Log.i(TAG, "User: " + user);
                    preferencesEditor.apply();
                } else {
                    HashMap userAndEvent = new HashMap();
                    userAndEvent.put("user_id", user.getId());
                    userAndEvent.put("event_id", eventId);

                    final boolean alreadyGoing = user.getEvents().contains(eventId);
                    if (alreadyGoing) {
                        userAndEvent.put("sign_up", false);
                    } else {
                        userAndEvent.put("sign_up", true);
                    }
                    Call<HashMap> call = api.signUp(userAndEvent);
                    Log.i("Message", "Button Clicked");

                    call.enqueue(new Callback<HashMap>() {
                        @Override
                        public void onResponse(Call<HashMap> call, Response<HashMap> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(EventActivity.this, "An error1 occurred, please try again!", Toast.LENGTH_LONG).show();
                            } else {
                                if (response.body() != null) {
                                    Toast toast_success = Toast.makeText(EventActivity.this, alreadyGoing ? R.string.remove_attendance_success_toast : R.string.sign_up_success_toast, Toast.LENGTH_LONG);
                                    toast_success.setGravity(Gravity.CENTER_VERTICAL, 0,0 );
                                    toast_success.show();

                                    if (alreadyGoing) {
                                        user.cancelAttendance(eventId);
                                        event.removeAttendee(user.getId());
                                        signUpButton.setText("Sign Up Now");
                                        signUpButton.setTextAppearance(R.style.Primary_Button);
                                        signUpButton.setBackgroundResource(R.drawable.primary_button);
                                    } else {
                                        user.signUp(eventId);
                                        event.addAttendee(user.getId());
                                        signUpButton.setTextAppearance(R.style.Cancel_Button);
                                        signUpButton.setBackgroundResource(R.drawable.cancel_button_stroke);
                                        signUpButton.setText("Cancel Sign Up");
                                    }

                                    SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                                    Gson gson = new Gson();
                                    preferencesEditor.putString(Constants.USER_KEY, gson.toJson(user));
                                    Log.i(TAG, "User: " + user);
                                    preferencesEditor.apply();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<HashMap> call, Throwable t) {
                            t.printStackTrace();
                            Toast.makeText(EventActivity.this, "An error2 occurred, please try again!", Toast.LENGTH_LONG).show();
                        }
                    });
                }



            }
        });



        joinTelegramGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Message", "Join Telegram Group Button Clicked");
                String url = event.getTelegram(); //"https://t.me/sutdathletics2020";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

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
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

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
            Intent intent = new Intent(EventActivity.this, MyInformationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

        if (id == R.id.my_calender) {
            Intent intent = new Intent(EventActivity.this, CalendarActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

        if (id == R.id.find_events_and_activities) {
            Intent intent = new Intent(EventActivity.this, FindEventsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

        if (id == R.id.manage_events) {
            Intent intent = new Intent (EventActivity.this, ManageEventsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
