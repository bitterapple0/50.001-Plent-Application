package com.example.plent.myActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.plent.R;
import com.example.plent.models.ActivityType;
import com.example.plent.models.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.core.content.ContextCompat;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class CalendarActivity extends AppCompatActivity {

    CardView cardview;
    TextView textview;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;
    String eventType;
    ArrayList<Event> userEvents= new ArrayList<Event>();
    Event e1 = new Event("My first Event", "20102020" , "1000", "1200", "STUD","YAY number 1", "@nil", ActivityType.INDUSTRY_TALK, "" );
    Event e2 = new Event("My second Event", "21102020" , "1100", "1300", "NUS","Yay number 2", "@nil",ActivityType.INDUSTRY_TALK, "" );
    Event e3 = new Event("My third Event", "22102020" , "0900", "1200", "NTU","Yay number 3","@nil", ActivityType.FIFTH_ROW, "" );
    Event e4 = new Event("My third Event", "22102020" , "0900", "1200", "NTU","Yay number 3","@nil",ActivityType.FIFTH_ROW, "" );
    Event e5 = new Event("My third Event", "22102020" , "0900", "1200", "NTU","Yay number 3","@nil",ActivityType.FIFTH_ROW, "" );
    Event e6 = new Event("My third Event", "22102020" , "0900", "1200", "NTU","Yay number 3","@nil",ActivityType.STUDENT_LIFE, "");
    Event e7 = new Event("My third Event", "22102020" , "0900", "1200", "NTU","Yay number 3","@nil",ActivityType.STUDENT_LIFE, "" );

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

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        final Calendar defaultSelectedDate = Calendar.getInstance();

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView).range(startDate, endDate)
                .datesNumberOnScreen(5)
                .defaultSelectedDate(defaultSelectedDate)
                .addEvents(new CalendarEventsPredicate() {
                    @Override
                    public List<CalendarEvent> events(Calendar date) {
                        List<CalendarEvent> events = new ArrayList<>();
                        Log.i("Date", toDateString(date.get(Calendar.DATE), date.get(Calendar.MONTH), date.get(Calendar.YEAR)));
                        Log.i("DefaultDate", toDateString(defaultSelectedDate.get(Calendar.DATE), defaultSelectedDate.get(Calendar.MONTH), defaultSelectedDate.get(Calendar.YEAR)));

                        // for loop to run through dates of events
                        for(Event e: userEvents){
                            if(e.getDate().equals(toDateString(date.get(Calendar.DATE), date.get(Calendar.MONTH), date.get(Calendar.YEAR)))){
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
                Log.i("Message", toDateString(date.get(Calendar.DATE), date.get(Calendar.MONTH), date.get(Calendar.YEAR)));
                linearLayout.removeAllViews();
                filterCalenderEvents(userEvents, date);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, FindEventsActivity.class);
                startActivity(intent);
            }
        });

        linearLayout = findViewById(R.id.calendar_events);

        filterCalenderEvents(userEvents, defaultSelectedDate);

    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.i("ALC", "start");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("ALC", "stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("ALC", "DESTROY");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("ALC", "RESTART");
    }

    public void addCalendarEvent(Event e) {
        View calendar_card =  View.inflate(this, R.layout.calendar_card, null);

        if(e.getType() == ActivityType.FIFTH_ROW) {
            Log.i("CALENDAR", e.getTitle() + " is a fifth row");
            calendar_card.findViewById(R.id.indicator).setBackgroundColor(ContextCompat.getColor(this, R.color.calendar_fr_yellow_dark));
            calendar_card.findViewById(R.id.calendar_card).setBackgroundColor(ContextCompat.getColor(this, R.color.calendar_fr_yellow_bg));
            TextView header = calendar_card.findViewById(R.id.calendar_title);
            header.setText(e.getTitle());
            TextView body = calendar_card.findViewById(R.id.calendar_time);
            body.setText(e.getDescription());

        } else if (e.getType() == ActivityType.STUDENT_LIFE) {
            Log.i("CALENDAR", e.getTitle() + " is a student life event");
            calendar_card.findViewById(R.id.indicator).setBackgroundColor(ContextCompat.getColor(this, R.color.calendar_it_green_dark));
            calendar_card.findViewById(R.id.calendar_card).setBackgroundColor(ContextCompat.getColor(this, R.color.calendar_it_green_bg));
            TextView header = calendar_card.findViewById(R.id.calendar_title);
            header.setText(e.getTitle());
            TextView body = calendar_card.findViewById(R.id.calendar_time);
            body.setText(e.getDescription());

        } else if (e.getType() == ActivityType.INDUSTRY_TALK) {
            Log.i("CALENDAR", e.getTitle() + " is an industry talk");
            calendar_card.findViewById(R.id.indicator).setBackgroundColor(ContextCompat.getColor(this, R.color.calendar_sl_blue_dark));
            calendar_card.findViewById(R.id.calendar_card).setBackgroundColor(ContextCompat.getColor(this, R.color.calendar_sl_blue_bg));
            TextView header = calendar_card.findViewById(R.id.calendar_title);
            header.setText(e.getTitle());
            TextView body = calendar_card.findViewById(R.id.calendar_time);
            body.setText(e.getDescription());
        } else {
            calendar_card.findViewById(R.id.indicator).setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        Log.i("Message", String.valueOf(linearLayout.getChildCount()));

        linearLayout.addView(calendar_card, linearLayout.getChildCount());

    }


    public void filterCalenderEvents(ArrayList<Event> events, Calendar date){
        for (Event e : events) {
            if (e.compareDate(date.get(Calendar.DATE), date.get(Calendar.MONTH), date.get(Calendar.YEAR))){
                addCalendarEvent(e);
            }
        }
    }

    public String toDateString(int date, int month, int year){
        return Integer.toString(date) + Integer.toString(month) +Integer.toString((year));
    }
}