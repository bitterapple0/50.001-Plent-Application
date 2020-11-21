package com.example.plent.myActivities;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.plent.R;
import com.example.plent.models.ActivityType;

public class Create_Calendar_Card extends CardView {

    ImageView indicator = findViewById(R.id.indicator);

    public Create_Calendar_Card(@NonNull Context context, ActivityType eventType) {
        // context: CalendarActivity.this
        super(context);

        if (eventType == ActivityType.FIFTH_ROW){
            setCardBackgroundColor(getResources().getColor(R.color.calendar_fr_yellow_bg));
            indicator.setBackgroundColor(getResources().getColor(R.color.calendar_fr_yellow_dark));
        } else if (eventType == ActivityType.INDUSTRY_TALK){
            setCardBackgroundColor(getResources().getColor(R.color.calendar_it_green_bg));
            indicator.setBackgroundColor(getResources().getColor(R.color.calendar_it_green_dark));
        } else if (eventType == ActivityType.STUDENT_LIFE){
            setCardBackgroundColor(getResources().getColor(R.color.calendar_sl_blue_bg));
            indicator.setBackgroundColor(getResources().getColor(R.color.calendar_sl_blue_dark));
        }

    }


}
