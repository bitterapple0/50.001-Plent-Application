package com.example.plent.myActivities;

import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plent.R;
import com.example.plent.models.ActivityType;
import com.example.plent.models.Event;
import com.example.plent.utils.SearchRecyclerAdapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.plent.models.ActivityType.FIFTH_ROW;

public class SeeAllActivity extends AppCompatActivity {

    private List<Event> eventList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchRecyclerAdapter seeAllAdapter;
    private CardView seeAllCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_all_activity);

        Event e1 = new Event("Athletics Intro Session", LocalDate.of(2020, 11, 29).toString(), LocalTime.of(9, 0).toString(), LocalTime.of(12, 0).toString(), "SUTD Track", "hello", "", FIFTH_ROW,"https://abhisekmishra.com/project4_3.jpg");
        Event e2 = new Event("ClImBInG INtrO seSSiOn", LocalDate.of(2020, 11, 29).toString(), LocalTime.of(9, 0).toString(), LocalTime.of(12, 0).toString(), "SUTD Track", "hello", "", FIFTH_ROW,"");

        eventList.add(e1);
        eventList.add(e2);
        eventList.add(e2);
        eventList.add(e2);
        eventList.add(e2);
        eventList.add(e1);
        eventList.add(e1);
        eventList.add(e1);
        eventList.add(e1);
        eventList.add(e1);
        eventList.add(e1);

        recyclerView = findViewById(R.id.see_all_recycler_view);
        seeAllAdapter = new SearchRecyclerAdapter(eventList, FIFTH_ROW, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(seeAllAdapter);

    }


}
