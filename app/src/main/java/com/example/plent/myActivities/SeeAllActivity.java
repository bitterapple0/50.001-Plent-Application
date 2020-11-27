package com.example.plent.myActivities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plent.R;
import com.example.plent.models.Event;
import com.example.plent.utils.SearchRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SeeAllActivity extends AppCompatActivity {

    private List<Event> eventList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchRecyclerAdapter seeAllAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_all_activity);

        recyclerView = findViewById(R.id.see_all_recycler_view);
        seeAllAdapter = new SearchRecyclerAdapter(eventList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        //recyclerView.setAdapter(seeAllAdapter);

    }


}
