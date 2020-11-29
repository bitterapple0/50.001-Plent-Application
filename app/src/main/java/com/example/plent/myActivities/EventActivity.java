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

import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static com.example.plent.utils.Constants.PREVIOUS_ACTIVITY;


public class EventActivity extends MenuActivity {

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
        mPreferences = getSharedPreferences(Constants.SHARED_PREF_FILE, MODE_PRIVATE);
        // get instance of api model
        api =  Api.getInstance().apiModel;

        // get event id from intent
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            // if no event id is stored, bring them back to find events activity screen
            backToFindEvents();
        } else {
            eventId = extras.getString(PREVIOUS_ACTIVITY);
            if (eventId == null) {
                // if no event id is stored, bring them back to find events activity screen
                backToFindEvents();
            }
        }

        // finding views
        location = findViewById(R.id.location);
        timeDate = findViewById(R.id.time_date);
        eventHeader = findViewById(R.id.event_header);
        eventPoster = findViewById(R.id.athletics_poster);
        description = findViewById(R.id.post_body);
        clashText = findViewById(R.id.warning1);
        event_activity_linear_layout = findViewById(R.id.event_activity_linear_layout);
        joinTelegramGroupButton = findViewById(R.id.join_telegram_group_button);
        signUpButton = findViewById(R.id.sign_up_button);

        user = getUserFromSharedPref();
        // set appearance of sign up button depending on whether user has already signed up for the event
        if (user != null && user.getEvents().contains(eventId)) {
            setButtonAppearanceCancel();
        }
        // fetch event info from db and check for clashes with user's events
        fetchEventOnCreate();

        // on click handler for "Sign Up Now"/"Cancel Sign Up" button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creating hash map object to be sent to API
                HashMap<String, Object> userAndEvent = new HashMap<>();
                userAndEvent.put("user_id", user.getId());
                userAndEvent.put("event_id", eventId);

                final boolean alreadyGoing = user.getEvents().contains(eventId);
                userAndEvent.put("sign_up", !alreadyGoing);

                signUpOnClick(userAndEvent, alreadyGoing);
            }
        });

        // on click handler for "Join Telegram" button
        joinTelegramGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implicit intent to redirect to Telegram
                String url = event.getTelegram();
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

    private User getUserFromSharedPref() {
        Gson gson = new Gson();
        String json = mPreferences.getString(Constants.USER_KEY, null);

        if (json == null) {
            Log.i(TAG, "is null");
            // redirect to login page if no user info stored
            Intent intent = new Intent(EventActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();

            return null;
        }
        return gson.fromJson(json, User.class);
    }

    private void fetchEventOnCreate() {
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
                        if (event.getTelegram().isEmpty()) {
                            // if there is no telegram link provided, no Join Telegram Group Button will be shown
                            joinTelegramGroupButton.setVisibility(View.INVISIBLE);
                        }
                        // format date
                        DateTimeFormatter dateFormatObj = DateTimeFormatter.ofPattern("EEEE, MMM d");
                        DateTimeFormatter timeFormatObj = DateTimeFormatter.ofPattern("hh.mma");
                        String suffix = event.getDate().getDayOfMonth() == 1 ? "st" : event.getDate().getDayOfMonth() == 2 ? "nd" : event.getDate().getDayOfMonth() == 3 ? "rd" : "th";
                        String dateString = event.getDate().format(dateFormatObj) + suffix + ", " + event.getStartTime().format(timeFormatObj) + " - " + event.getEndTime().format(timeFormatObj);

                        // setting text information
                        eventHeader.setText(event.getTitle());
                        location.setText(event.getLocation());
                        timeDate.setText(dateString);
                        description.setText(event.getDescription());
                        clashText.setText(event.getClashString());

                        // setting image
                        try {
                            new NetworkImage(eventPoster).execute(event.getImageUrl());
                        } catch (Exception e) {
                            Log.e(TAG, "Could not get poster image");
                        }
                    }

                    Log.i(TAG, "Retrieved event id: " + event.getId());
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(EventActivity.this, "An error2 occurred, please try again!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signUpOnClick(HashMap<String, Object> userAndEvent, final boolean alreadyGoing) {
        // API call
        Call<HashMap<String, Object>> call = api.signUp(userAndEvent);

        // handling response/error from call
        call.enqueue(new Callback<HashMap<String,Object>>() {
            @Override
            public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(EventActivity.this, "An error1 occurred, please try again!", Toast.LENGTH_LONG).show();
                } else {
                    // check to make sure write to database was successful
                    if (response.body() != null) {
                        if (alreadyGoing) {
                            // if user is attempting to cancel sign up, add an alert prompt to seek confirmation
                            AlertDialog.Builder confirmCancel = new AlertDialog.Builder(EventActivity.this, R.style.AlertDialogCustom);
                            confirmCancel.setTitle("Cancel sign up for " + eventHeader.getText() + " ?");
                            confirmCancel.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast toast_success = Toast.makeText(EventActivity.this, R.string.remove_attendance_success_toast, Toast.LENGTH_LONG);
                                    toast_success.setGravity(Gravity.CENTER_VERTICAL, 0,0 );
                                    toast_success.show();

                                    user.cancelAttendance(eventId);
                                    event.removeAttendee(user.getId());
                                    // button shows "Sign Up Now" upon cancelling attendance
                                    setButtonAppearanceSignUp();
                                    updateUserSharedPref();
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
                        } else {
                            // if user is attempting to sign up, sign up immediately
                            Toast toast_success = Toast.makeText(EventActivity.this, R.string.sign_up_success_toast, Toast.LENGTH_LONG);
                            toast_success.setGravity(Gravity.CENTER_VERTICAL, 0,0 );
                            toast_success.show();

                            user.signUp(eventId);
                            event.addAttendee(user.getId());
                            // buttons shows "Cancel Sign Up" upon signing up
                            setButtonAppearanceCancel();
                            updateUserSharedPref();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(EventActivity.this, "An error2 occurred, please try again!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setButtonAppearanceCancel() {
        signUpButton.setTextAppearance(R.style.Cancel_Button);
        signUpButton.setBackgroundResource(R.drawable.cancel_button_stroke);
        signUpButton.setText("Cancel Sign Up");
    }

    private void setButtonAppearanceSignUp() {
        signUpButton.setText("Sign Up Now");
        signUpButton.setTextAppearance(R.style.Primary_Button);
        signUpButton.setBackgroundResource(R.drawable.primary_button);
    }

    private void updateUserSharedPref() {
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        Gson gson = new Gson();
        preferencesEditor.putString(Constants.USER_KEY, gson.toJson(user));
        preferencesEditor.apply();
    }

}
