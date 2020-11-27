package com.example.plent.myActivities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plent.R;
import com.example.plent.models.ActivityType;
import com.example.plent.models.Event;
import com.example.plent.utils.SearchRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.plent.models.ActivityType.FIFTH_ROW;

public class SeeAllActivity extends AppCompatActivity {

    private List<Event> eventList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchRecyclerAdapter seeAllAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_all_activity);

        Event e1 = new Event("Athletics Intro Session", new int[]{02, 10, 2020}, new int[]{12,20}, new int[]{10, 20}, "SUTD Track", "hello", "", FIFTH_ROW,"");
        Event e2 = new Event("ClImBInG INtrO seSSiOn", new int[]{02, 10, 2020}, new int[]{12,20}, new int[]{10, 20}, "SUTD Track", "hello", "", FIFTH_ROW,"");

        eventList.add(e1);
        eventList.add(e2);
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
        eventList.add(e1);


        recyclerView = findViewById(R.id.see_all_recycler_view);
        seeAllAdapter = new SearchRecyclerAdapter(eventList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(seeAllAdapter);

    }


}
