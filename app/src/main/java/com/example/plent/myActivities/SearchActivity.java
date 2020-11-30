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
import com.example.plent.models.Event;
import com.example.plent.adapters.SearchRecyclerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

        Bundle bundle = getIntent().getExtras();
        String jsonString = bundle.getString("EventList");

        Gson gson = new Gson();
        Type listOfEventType = new TypeToken<List<Event>>() {}.getType();
        eventList = gson.fromJson(jsonString, listOfEventType);

        searchRecyclerAdapter = new SearchRecyclerAdapter(eventList, this);
        recyclerView.setAdapter(searchRecyclerAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);






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
    }


}