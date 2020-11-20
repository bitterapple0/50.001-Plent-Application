package com.example.plent.myActivities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.plent.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class CalendarActivity extends AppCompatActivity {

    CardView cardview;
    TextView textview;
    ViewGroup.LayoutParams layoutparams;
    RelativeLayout relativeLayout;
    String eventType;


    CalendarEvent c1 = new CalendarEvent(Color.parseColor("#EAD620"), "event");
    CalendarEvent c2 = new CalendarEvent(Color.parseColor("#81D2AC"), "event");
    CalendarEvent c3 = new CalendarEvent(Color.parseColor("#81C3D2"), "event");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("ALC", "CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

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
                        Log.i("Date", date.toString());
                        Log.i("DefaultDate", defaultSelectedDate.toString());

                        // for loop to run through dates of events

                        if (date.get(Calendar.DAY_OF_YEAR) == defaultSelectedDate.get(Calendar.DAY_OF_YEAR)){
                            events.add(c1);
                            events.add(c2);
                            events.add(c3);
                            events.add(c1);
                            events.add(c2);
                            events.add(c3);
                        }


                        return events;
                    }
                })
                .build();


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                Log.i("Message", "Date Selected");
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

        /*relativeLayout = findViewById(R.id.day_events); now is calendar_events
        CreateCardViewProgrammatically();

        relativeLayout = findViewById(R.id.calendar_events);
        addCalendarCard(); */

        relativeLayout = findViewById(R.id.calendar_events);
        addCalendarEvent("Fifth Row Activities");
        addCalendarEvent("Industry Talks");
        addCalendarEvent("Student Life");

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

    public void addCalendarEvent(String eventType) {
        View calendar_card =  View.inflate(this, R.layout.calendar_card, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        calendar_card.setLayoutParams(lp);

        if (eventType == "Fifth Row Activities") {
            calendar_card.findViewById(R.id.indicator).setBackgroundColor(getResources().getColor(R.color.calendar_fr_yellow_dark));
            calendar_card.findViewById(R.id.calendar_card).setBackgroundColor(getResources().getColor(R.color.calendar_fr_yellow_bg));
            TextView header = calendar_card.findViewById(R.id.calendar_title);
            header.setText("Fifth Row Activities Title");
        } else if (eventType == "Industry Talks") {
            calendar_card.findViewById(R.id.indicator).setBackgroundColor(getResources().getColor(R.color.calendar_it_green_dark));
            calendar_card.findViewById(R.id.calendar_card).setBackgroundColor(getResources().getColor(R.color.calendar_it_green_bg));
            TextView header = calendar_card.findViewById(R.id.calendar_title);
            header.setText("Industry Talks Title");
        } else if (eventType == "Student Life") {
            calendar_card.findViewById(R.id.indicator).setBackgroundColor(getResources().getColor(R.color.calendar_sl_blue_dark));
            calendar_card.findViewById(R.id.calendar_card).setBackgroundColor(getResources().getColor(R.color.calendar_sl_blue_bg));
            TextView header = calendar_card.findViewById(R.id.calendar_title);
            header.setText("House Guardians Games Night");
            TextView body = calendar_card.findViewById(R.id.calendar_time);
            body.setText("9pm - 11pm, BLK 59 Level 10");
        } else {
            calendar_card.findViewById(R.id.indicator).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        relativeLayout.addView(calendar_card);

    }
}
