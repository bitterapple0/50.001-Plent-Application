package com.example.plent.myActivities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plent.R;
import com.example.plent.models.ActivityType;
import com.example.plent.models.ApiModel;
import com.example.plent.models.Event;
import com.example.plent.utils.Api;
import com.example.plent.utils.Constants;
import com.example.plent.utils.SearchRecyclerAdapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.plent.models.ActivityType.FIFTH_ROW;
import static com.example.plent.models.ActivityType.INDUSTRY_TALK;
import static com.example.plent.models.ActivityType.STUDENT_LIFE;

public class SeeAllActivity extends AppCompatActivity {

    private List<Event> eventList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchRecyclerAdapter seeAllAdapter;
    private CardView seeAllCard;
    ApiModel api;
    private CardView parent;
    SearchRecyclerAdapter fr_adapter, it_adapter, sl_adapter;
    private ArrayList<Event> fifthRowEvents, studentLifeEvents, industryTalkEvents;
    SharedPreferences seeAllPreferences;


    final static String placeholderImageUrl = "https://res.cloudinary.com/dyaxu5mb4/image/upload/v1606499824/plent/poster_placeholder1_jgh6vd.png";
    Event e1 = new Event("Athletics Intro Session", LocalDate.of(2020, 11, 29).toString(), LocalTime.of(9, 0).toString(), LocalTime.of(12, 0).toString(), "SUTD Track", "hello", "", FIFTH_ROW,"https://abhisekmishra.com/project4_3.jpg");
    Event e2 = new Event("ClImBInG INtrO seSSiOn", LocalDate.of(2020, 11, 29).toString(), LocalTime.of(9, 0).toString(), LocalTime.of(12, 0).toString(), "SUTD Track", "hello", "", FIFTH_ROW,"");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_all_activity);

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

        api = Api.getInstance().apiModel;

        recyclerView = findViewById(R.id.see_all_recycler_view);

        seeAllPreferences = getSharedPreferences(Constants.SHARED_PREF_FILE, MODE_PRIVATE);
        String eventType = seeAllPreferences.getString("EventType", null);

        if (eventType == FIFTH_ROW.toString()){
            fr_adapter = new SearchRecyclerAdapter(fifthRowEvents, ActivityType.FIFTH_ROW, this);
            recyclerView.setAdapter(fr_adapter);
        } else if (eventType == INDUSTRY_TALK.toString()){
            it_adapter = new SearchRecyclerAdapter(industryTalkEvents, ActivityType.INDUSTRY_TALK, this);
            recyclerView.setAdapter(it_adapter);
        } else if (eventType == STUDENT_LIFE.toString()) {
            sl_adapter = new SearchRecyclerAdapter(studentLifeEvents, ActivityType.STUDENT_LIFE, this);
            recyclerView.setAdapter(sl_adapter);
        } else {
            seeAllAdapter = new SearchRecyclerAdapter(eventList, FIFTH_ROW, this);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.setAdapter(seeAllAdapter);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void retrieveEvents() {
        Call<ArrayList<Event>> call = api.getAllEvents();
        call.enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SeeAllActivity.this, "An error1 occurred, please try again!", Toast.LENGTH_LONG).show();
                } else {
                    eventList = response.body();
                    fr_adapter.refreshEvents(eventList, FIFTH_ROW, null);
                    sl_adapter.refreshEvents(eventList, STUDENT_LIFE, null);
                    it_adapter.refreshEvents(eventList, INDUSTRY_TALK, null);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {

            }
        });

    }
}
