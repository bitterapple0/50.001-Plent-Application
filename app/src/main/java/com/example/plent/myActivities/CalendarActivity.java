package com.example.plent.myActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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

        relativeLayout = findViewById(R.id.day_events);

        CreateCardViewProgrammatically();

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

    public void CreateCardViewProgrammatically(){

        cardview = new CardView(getApplicationContext());

        layoutparams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        cardview.setLayoutParams(layoutparams);

        cardview.setRadius(15);

        cardview.setPadding(25, 25, 25, 25);

        cardview.setCardBackgroundColor(Color.MAGENTA);

        cardview.setMaxCardElevation(30);

        cardview.setMaxCardElevation(6);

        textview = new TextView(getApplicationContext());

        textview.setLayoutParams(layoutparams);

        textview.setText("CardView Programmatically");

        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);

        textview.setTextColor(Color.WHITE);

        textview.setPadding(25,25,25,25);

        textview.setGravity(Gravity.CENTER);

        cardview.addView(textview);

        relativeLayout.addView(cardview);

    }
}
