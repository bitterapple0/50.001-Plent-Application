package com.example.plent.myActivities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
    TextView seeAllHeading;

    final static String placeholderImageUrl = "https://res.cloudinary.com/dyaxu5mb4/image/upload/v1606499824/plent/poster_placeholder1_jgh6vd.png";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_all_activity);

        recyclerView = findViewById(R.id.see_all_recycler_view);

        Bundle bundle = getIntent().getExtras();
        String jsonString = bundle.getString("EventList");

        Gson gson = new Gson();
        Type listOfEventType = new TypeToken<List<Event>>() {}.getType();
        eventList = gson.fromJson(jsonString, listOfEventType);

        ActivityType eventType = eventList.get(0).getType();


        seeAllAdapter = new SearchRecyclerAdapter(eventList, eventType , this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(seeAllAdapter);

        seeAllHeading = findViewById(R.id.see_all_heading);
        seeAllHeading.setText(ActivityType.convertEnumToString(eventType));


    }

}
