package com.example.plent.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plent.R;
import com.example.plent.models.ActivityType;
import com.example.plent.models.Event;
import com.example.plent.myActivities.CalendarActivity;
import com.example.plent.myActivities.EventActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.example.plent.utils.Constants.CALENDAR_CARD_CONTEXT;
import static com.example.plent.utils.Constants.CALENDAR_CARD_CONTEXT;
import static com.example.plent.utils.Constants.PREVIOUS_ACTIVITY;
import static com.example.plent.utils.Constants.SELECTED_EVENT_KEY;

public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Event> calendarEvents;
    List<Event> calendarEventsAll;
    private Activity CalendarActivity;
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_CALENDAR_EVENT = 1;

    // TODO Comment below for the best practice method for onCLick
//    private OnCalendarListener mOnCalendarListener;
    // TODO Comment below for the best practice method for onCLick
//    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTitle, time, location;
        public ImageView indicator;
        public CardView calendarCard;
        public Button calToEventButton;

        // TODO Comment below for the best practice method for onCLick
//         public OnCalendarListener onCalendarListener;

        // TODO Comment below for the best practice method for onCLick
//        public MyViewHolder(View view, OnCalendarListener onCalendarListener){

        public MyViewHolder(View view) {
            super(view);
            CALENDAR_CARD_CONTEXT = view.getContext();
            eventTitle = view.findViewById(R.id.calendar_title);
            time = view.findViewById(R.id.calendar_time);
            location = view.findViewById(R.id.calendar_location);
            indicator = view.findViewById(R.id.indicator);
            calendarCard = view.findViewById(R.id.calendar_card);
            calToEventButton = view.findViewById(R.id.calToEventButton);
            // TODO Comment below for the best parctice method for onCLick
//            this.onCalendarListener = onCalendarListener;

            // TODO Comment below for the best parctice method for onCLick
//            view.setOnClickListener(this);
        }

        // TODO Comment below for the best parctice method for onCLick
//        @Override
//        public void onClick(View v) {
//            onCalendarListener.onCalendarClick(getAdapterPosition());
//        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        TextView placeholderText;
        ImageView placeholderImage;
        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            placeholderText = itemView.findViewById(R.id.placeholder_text);
            placeholderImage = itemView.findViewById(R.id.placeholder_image);
        }
    }

    // TODO Comment below for the best parctice method for onCLick
//    public CalendarAdapter(List<Event> calendarEvents, OnCalendarListener onCalendarListener) {
    public CalendarAdapter(List<Event> calendarEvents) {
        this.calendarEvents = calendarEvents;
        this.calendarEventsAll = new ArrayList<>(calendarEvents);
        // TODO Comment below for the best parctice method for onCLick
//        this.mOnCalendarListener = onCalendarListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch(viewType){
            case VIEW_TYPE_CALENDAR_EVENT:
                View calendarView = layoutInflater.from(parent.getContext()).inflate(R.layout.calendar_card, parent, false);
                viewHolder = new MyViewHolder(calendarView);
                break;
            default:
                View emptyView = layoutInflater.inflate(R.layout.search_placeholder, parent, false);
                viewHolder = new EmptyViewHolder(emptyView);
                break;
        }
        // TODO Comment below for the best parctice method for onCLick
//        return new CalendarAdapter.MyViewHolder(calendarView, mOnCalendarListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch(holder.getItemViewType()){
            case VIEW_TYPE_CALENDAR_EVENT:
                MyViewHolder calendarViewHolder = (MyViewHolder) holder;
                setCalendarDetails(calendarViewHolder, position);
                break;
            default:
                EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
                setEmptyEventDetails(emptyViewHolder, position);
                break;
        }




//        holder.calendarCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View V){
//                Intent intent = new Intent(CALENDAR_CARD_CONTEXT, EventActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                intent.putExtra(PREVIOUS_ACTIVITY, CAL_EVENT_CLICK);
//                CALENDAR_CARD_CONTEXT.startActivity(intent);
//            }
//        });
    }
    private void setCalendarDetails(CalendarAdapter.MyViewHolder vh, int position){
        final Event calendarEvent = calendarEvents.get(position);
        vh.eventTitle.setText(calendarEvent.getTitle());
        vh.time.setText(DateTimeUtils.formatTime24H(calendarEvent.getStartTime()) + " - " + DateTimeUtils.formatTime24H(calendarEvent.getEndTime()));
        vh.location.setText(calendarEvent.getLocation());


//            public void onClick(View V){
//                Intent intent = new Intent(CALENDAR_CARD_CONTEXT, EventActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                intent.putExtra(PREVIOUS_ACTIVITY, CAL_EVENT_CLICK);
//                CALENDAR_CARD_CONTEXT.startActivity(intent);
//            }
//        });

        if (calendarEvent.getType() == ActivityType.FIFTH_ROW) {
            vh.indicator.setBackgroundColor(Color.parseColor("#EAD620"));
            vh.calendarCard.setBackgroundColor(Color.parseColor("#FFFCE3"));
        } else if (calendarEvent.getType() == ActivityType.STUDENT_LIFE) {
            vh.indicator.setBackgroundColor(Color.parseColor("#81D2AC"));
            vh.calendarCard.setBackgroundColor(Color.parseColor("#EDFFF7"));
        } else if (calendarEvent.getType() == ActivityType.INDUSTRY_TALK) {
            vh.indicator.setBackgroundColor(Color.parseColor("#81C3D2"));
            vh.calendarCard.setBackgroundColor(Color.parseColor("#EAFBFF"));
        }

//        TODO: Not the most efficient way - can be removed if the other way works
        final String CAL_EVENT_CLICK = calendarEvents.get(position).getId();

        vh.calToEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Calendar Adapter", "Button Pressed");
                Intent intent = new Intent(CALENDAR_CARD_CONTEXT, EventActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Log.d("Calendar Adapter", "onClick " + CAL_EVENT_CLICK);
                intent.putExtra(SELECTED_EVENT_KEY, CAL_EVENT_CLICK);
                CALENDAR_CARD_CONTEXT.startActivity(intent);
            }
        });
    }

    private void setEmptyEventDetails (CalendarAdapter.EmptyViewHolder vh, int position){
        vh.placeholderText.setText("You have nothing scheduled, find something to do!");
        vh.placeholderImage.setImageResource(R.drawable.placeholder_calendar);
    }

    @Override
    public int getItemCount() {
        if(calendarEvents.size() == 0){
            return 1;
        }
        return calendarEvents.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(calendarEvents.size() == 0){
            return VIEW_TYPE_EMPTY;
        }
        return VIEW_TYPE_CALENDAR_EVENT;
    }

    // it edits the calendarEvent array which is what will be displayed in the recycleviewer.
    // calendarEventsAll what we use to remember all the user events.
    public void filterEvents(Calendar date) {
        calendarEvents.clear();
        LocalDate calendarDate = LocalDate.of(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DATE));

        for (Event e : calendarEventsAll) {
            if (e.getDate().isEqual(calendarDate)) {
                calendarEvents.add(e);
            }
        }
        notifyDataSetChanged();
    }


    // TODO Comment below for the best parctice method for onCLick
//    public interface OnCalendarListener {
//        void onCalendarClick(int position);
//    }

}
