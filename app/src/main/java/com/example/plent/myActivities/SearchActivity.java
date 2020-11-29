package com.example.plent.myActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plent.R;
import com.example.plent.models.ActivityType;
import com.example.plent.models.Event;
import com.example.plent.utils.SearchRecyclerAdapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView placeholderImageView;
    TextView placeholderTextView;
    SearchRecyclerAdapter searchRecyclerAdapter;
    List<Event> eventList = new ArrayList<Event>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.recyclerView);
        placeholderImageView = findViewById(R.id.search_placeholder_image);
        placeholderTextView = findViewById(R.id.search_placeholder_text);

        Event e1 = new Event("My first Event", LocalDate.of(2020, 11, 29).toString(), LocalTime.of(9, 0).toString(), LocalTime.of(12, 0).toString(), "STUD","YAY number 1", "@nil", ActivityType.INDUSTRY_TALK, "" );
        Event e2 = new Event("My second Event", LocalDate.of(2020, 11, 29).toString(), LocalTime.of(13, 0).toString(), LocalTime.of(15, 0).toString(), "NUS","Yay number 2", "@nil",ActivityType.INDUSTRY_TALK, "" );
        Event e3 = new Event("My third Event", LocalDate.of(2020, 11, 30).toString(), LocalTime.of(9, 0).toString(), LocalTime.of(12, 0).toString(), "NTU","Yay number 3","@nil", ActivityType.FIFTH_ROW, "" );
        Event e4 = new Event("My third Event", LocalDate.of(2020, 11, 30).toString(), LocalTime.of(12, 0).toString(), LocalTime.of(13, 0).toString(), "NTU","Yay number 3","@nil",ActivityType.FIFTH_ROW, "" );
        Event e5 = new Event("My third Event", LocalDate.of(2020, 11, 30).toString(), LocalTime.of(13, 0).toString(), LocalTime.of(14, 0).toString(), "NTU","Yay number 3","@nil",ActivityType.FIFTH_ROW, "" );
        Event e6 = new Event("My third Event", LocalDate.of(2020, 11, 30).toString(), LocalTime.of(16, 0).toString(), LocalTime.of(18, 0).toString(), "NTU","Yay number 3","@nil",ActivityType.STUDENT_LIFE, "");
        Event e7 = new Event("My third Event", LocalDate.of(2020, 11, 29).toString(), LocalTime.of(20, 0).toString(), LocalTime.of(22, 0).toString(), "NTU","Yay number 3","@nil",ActivityType.STUDENT_LIFE, "" );

        eventList.add(e1);
        eventList.add(e2);
        eventList.add(e3);
        eventList.add(e4);
        eventList.add(e5);
        eventList.add(e6);
        eventList.add(e7);

        searchRecyclerAdapter = new SearchRecyclerAdapter(eventList, this);
        recyclerView.setAdapter(searchRecyclerAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        if(eventList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            placeholderTextView.setVisibility(View.VISIBLE);
            placeholderImageView.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            placeholderTextView.setVisibility(View.GONE);
            placeholderImageView.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_bar, menu);
        MenuItem item = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) item.getActionView();
//        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
//        ImageView searchBackButton = searchView.findViewById()
//        searchEditText.setTextColor(getColor(R.color.calendar_fr_yellow_bg));
//        searchEditText.setHintTextColor(getColor(R.color.calendar_fr_yellow_bg));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //searchRecyclerAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchRecyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

        // Add back button on top?
    }


}