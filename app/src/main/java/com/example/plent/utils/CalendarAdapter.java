package com.example.plent.utils;

import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plent.R;
import com.example.plent.models.ActivityType;
import com.example.plent.models.Event;
import com.example.plent.myActivities.CalendarActivity;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> {

    List<Event> calendarEvents;
    private Activity CalendarActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTitle, time, location;
        public ImageView indicator;



        public MyViewHolder(View view){
            super(view);
            eventTitle = view.findViewById(R.id.calendar_title);
            time = view.findViewById(R.id.calendar_time);
            location = view.findViewById(R.id.calendar_location);
            indicator = view.findViewById(R.id.indicator);
        }
    }

    public CalendarAdapter(List<Event> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }

    @NonNull
    @Override
    public CalendarAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View calendarView = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_card, parent, false);
        return new CalendarAdapter.MyViewHolder(calendarView);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.MyViewHolder holder, int position) {
        Event calendarEvent = calendarEvents.get(position);
        holder.eventTitle.setText(calendarEvent.getTitle());
        holder.time.setText(String.valueOf(calendarEvent.getStartTime() + " - " + String.valueOf(calendarEvent.getEndTime())));
        holder.location.setText(calendarEvent.getLocation());


        if (calendarEvent.getType() == ActivityType.FIFTH_ROW) {
            //holder.indicator.setBackgroundColor(R.color.calendar_fr_yellow_dark);
            //findViewById(R.id.indicator).setBackgroundColor(ContextCompat.getColor(CalendarActivity, R.color.calendar_fr_yellow_dark));
            //calendar_card.findViewById(R.id.calendar_card).setBackgroundColor(ContextCompat.getColor(CalendarActivity, R.color.calendar_fr_yellow_bg));
        } else if (calendarEvent.getType() == ActivityType.STUDENT_LIFE) {
            //calendar_card.findViewById(R.id.indicator).setBackgroundColor(ContextCompat.getColor(CalendarActivity, R.color.calendar_it_green_dark));
            //calendar_card.findViewById(R.id.calendar_card).setBackgroundColor(ContextCompat.getColor(CalendarActivity, R.color.calendar_it_green_bg));
        } else if (calendarEvent.getType() == ActivityType.INDUSTRY_TALK) {
            //calendar_card.findViewById(R.id.indicator).setBackgroundColor(ContextCompat.getColor(CalendarActivity, R.color.calendar_sl_blue_dark));
            //calendar_card.findViewById(R.id.calendar_card).setBackgroundColor(ContextCompat.getColor(CalendarActivity, R.color.calendar_sl_blue_bg));
        }
    }

    @Override
    public int getItemCount() {
        return calendarEvents.size();
    }

}
