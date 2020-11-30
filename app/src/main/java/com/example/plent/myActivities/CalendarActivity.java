package com.example.plent.myActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.plent.R;
import com.example.plent.models.ActivityType;
import com.example.plent.models.ApiModel;
import com.example.plent.models.Event;
import com.example.plent.models.User;
import com.example.plent.utils.Api;
import com.example.plent.utils.CalendarAdapter;
import com.example.plent.utils.Constants;
import com.example.plent.utils.DateTimeUtils;
import com.example.plent.utils.ParticipantsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.plent.utils.Constants.PREVIOUS_ACTIVITY;
import static com.example.plent.utils.Constants.SELECTED_EVENT_KEY;


public class CalendarActivity extends MenuActivity implements CalendarAdapter.OnCalendarListener {

    private static final String TAG = "CALENDAR";
    private ApiModel api;
    private SharedPreferences mPreferences;
    private User user;

    final ArrayList<Event> allUserEvents = new ArrayList<>();
    final ArrayList<Event> userEvents = new ArrayList<>();
    RecyclerView recyclerView;
    CalendarAdapter calendarAdapter;

    CalendarEvent c1 = new CalendarEvent(Color.parseColor("#EAD620"), "event");
    CalendarEvent c2 = new CalendarEvent(Color.parseColor("#81D2AC"), "event");
    CalendarEvent c3 = new CalendarEvent(Color.parseColor("#81C3D2"), "event");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("ALC", "CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        api = Api.getInstance().apiModel;
        mPreferences = getSharedPreferences(Constants.SHARED_PREF_FILE, MODE_PRIVATE);
        user = getUserFromSharedPref();

        recyclerView = findViewById(R.id.calendar_events);

        /* starts before 1 month from now */
        final Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        final Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        final Calendar defaultSelectedDate = Calendar.getInstance();
        LocalDate startLocalDate =  LocalDate.of(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH)+1, startDate.get(Calendar.DATE));
        LocalDate endLocalDate =  LocalDate.of(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH)+1, endDate.get(Calendar.DATE));

        Call<ArrayList<Event>> call = api.getCalendarEvents(user.getId(), startLocalDate.toString(), endLocalDate.toString());
        call.enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(CalendarActivity.this, "An error1 occurred, please try again!", Toast.LENGTH_LONG).show();
                } else {
                    for (Event e: response.body()) {
                        allUserEvents.add(e);
                        userEvents.add(e);
                    }
                    Log.i(TAG, "calendar upon retrieving data " + allUserEvents);

                    calendarAdapter = new CalendarAdapter(userEvents, CalendarActivity.this); // this array list is the dynamic one we will vary based on date selected
                    RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(pLayoutManager);
                    recyclerView.setAdapter(calendarAdapter);

                    calendarAdapter.filterEvents(defaultSelectedDate);

                    HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(CalendarActivity.this, R.id.calendarView).range(startDate, endDate)
                            .datesNumberOnScreen(5)
                            .defaultSelectedDate(defaultSelectedDate)
                            .addEvents(new CalendarEventsPredicate() {
                                @Override
                                public List<CalendarEvent> events(Calendar date) {
                                    Log.i(TAG, "calendar allUserEvents " + allUserEvents);
                                    final List<CalendarEvent> events = new ArrayList<>();
                                    final LocalDate selectedDay = LocalDate.of(date.get(Calendar.YEAR), date.get(Calendar.MONTH)+1, date.get(Calendar.DATE));
                                    for(Event e: allUserEvents){
                                        Log.i("EventDate", (e.getDate().toString()));
                                        if(e.getDate().isEqual(selectedDay)){
                                            if (e.getType() == ActivityType.FIFTH_ROW) {
                                                events.add(c1);
                                            } else if (e.getType() == ActivityType.STUDENT_LIFE) {
                                                events.add(c2);
                                            } else if (e.getType() == ActivityType.INDUSTRY_TALK) {
                                                events.add(c3);
                                            }
                                        }
                                    }
                                    return events;
                                }
                            })
                            .build();

                    horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
                        @Override
                        public void onDateSelected(Calendar date, int position) {
                            calendarAdapter.filterEvents(date);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(CalendarActivity.this, "An error2 occurred, please try again!", Toast.LENGTH_LONG).show();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, FindEventsActivity.class);
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
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();

            return null;
        }
        return gson.fromJson(json, User.class);
    }


    @Override
    public void onCalendarClick(int position) {
        Log.d(TAG, "Clicked the Calendar Card");
        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra(SELECTED_EVENT_KEY, userEvents.get(position).getId());
        startActivity(intent);


    }
}