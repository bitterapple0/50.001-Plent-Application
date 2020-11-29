package com.example.plent.myActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.plent.R;
import com.example.plent.models.ActivityType;
import com.example.plent.models.Event;
import com.example.plent.utils.CalendarAdapter;
import com.example.plent.utils.DateTimeUtils;
import com.example.plent.utils.ParticipantsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import static com.example.plent.utils.Constants.PREVIOUS_ACTIVITY;


public class CalendarActivity extends MenuActivity {

    CardView cardview;
    TextView textview;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;
    String eventType;
    ArrayList<Event> userEvents= new ArrayList<Event>();
    ArrayList<Event> allUserEvents;
    RecyclerView recyclerView;
    CalendarAdapter calendarAdapter;

    Event e1 = new Event("My first Event", LocalDate.of(2020, 11, 29).toString(), LocalTime.of(9, 0).toString(), LocalTime.of(12, 0).toString(), "STUD","YAY number 1", "@nil", ActivityType.INDUSTRY_TALK, "" );
    Event e2 = new Event("My second Event", LocalDate.of(2020, 11, 29).toString(), LocalTime.of(13, 0).toString(), LocalTime.of(15, 0).toString(), "NUS","Yay number 2", "@nil",ActivityType.INDUSTRY_TALK, "" );
    Event e3 = new Event("My third Event", LocalDate.of(2020, 11, 30).toString(), LocalTime.of(9, 0).toString(), LocalTime.of(12, 0).toString(), "NTU","Yay number 3","@nil", ActivityType.FIFTH_ROW, "" );

    CalendarEvent c1 = new CalendarEvent(Color.parseColor("#EAD620"), "event");
    CalendarEvent c2 = new CalendarEvent(Color.parseColor("#81D2AC"), "event");
    CalendarEvent c3 = new CalendarEvent(Color.parseColor("#81C3D2"), "event");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("ALC", "CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        userEvents.add(e1);
        userEvents.add(e2);
        userEvents.add(e3);

        allUserEvents = new ArrayList<>(userEvents); // need to track ALL events the user will attend to get all DOTS on calendar

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        final Calendar defaultSelectedDate = Calendar.getInstance();

        recyclerView = findViewById(R.id.calendar_events);

        calendarAdapter = new CalendarAdapter(userEvents); // this array list is the dynamic one we will vary based on date selected
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(pLayoutManager);
        recyclerView.setAdapter(calendarAdapter);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView).range(startDate, endDate)
                .datesNumberOnScreen(5)
                .defaultSelectedDate(defaultSelectedDate)
                .addEvents(new CalendarEventsPredicate() {
                    @Override
                    public List<CalendarEvent> events(Calendar date) {
                        List<CalendarEvent> events = new ArrayList<>();
//                        Log.i("Date", LocalDate.of(date.get(Calendar.YEAR), date.get(Calendar.MONTH)+1 ,date.get(Calendar.DATE)).toString());
//                        Log.i("DefaultDate", LocalDate.of(defaultSelectedDate.get(Calendar.YEAR), defaultSelectedDate.get(Calendar.MONTH)+1,defaultSelectedDate.get(Calendar.DATE)).toString());
                        LocalDate selectedDay = LocalDate.of(date.get(Calendar.YEAR), date.get(Calendar.MONTH)+1, date.get(Calendar.DATE));
                        // for loop to run through dates of events
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
                        Log.i("CALENDAR", ""+events);
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, FindEventsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        calendarAdapter.filterEvents(defaultSelectedDate);
    }


    public String toDateString(int date, int month, int year){
        return Integer.toString(date) + Integer.toString(month) +Integer.toString((year));
    }


}
