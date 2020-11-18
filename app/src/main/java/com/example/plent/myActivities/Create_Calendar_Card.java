package com.example.plent.myActivities;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.plent.R;

public class Create_Calendar_Card extends CardView {

    ImageView indicator = findViewById(R.id.indicator);

    public Create_Calendar_Card(@NonNull Context context, String eventType) {
        // context: CalendarActivity.this
        super(context);

        if (eventType == "Fifth Row Activities"){
            setCardBackgroundColor(getResources().getColor(R.color.calendar_fr_yellow_bg));
            indicator.setBackgroundColor(getResources().getColor(R.color.calendar_fr_yellow_dark));
        } else if (eventType == "Industry Talks"){
            setCardBackgroundColor(getResources().getColor(R.color.calendar_it_green_bg));
            indicator.setBackgroundColor(getResources().getColor(R.color.calendar_it_green_dark));
        } else if (eventType == "Student Life"){
            setCardBackgroundColor(getResources().getColor(R.color.calendar_sl_blue_bg));
            indicator.setBackgroundColor(getResources().getColor(R.color.calendar_sl_blue_dark));
        }

    }


}
