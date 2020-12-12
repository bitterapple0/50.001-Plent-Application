package com.example.plent.myActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.plent.R;
import com.example.plent.models.ApiModel;
import com.example.plent.models.User;
import com.example.plent.utils.Api;
import com.example.plent.utils.Constants;
import com.example.plent.adapters.ParticipantsAdapter;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsActivity extends MenuActivity {

    private static final String TAG = "PARTICIPANTS";
    private ApiModel api;
    private String eventId;
    private List<User> participantsList= new ArrayList<>();
    private RecyclerView recyclerView;
    private ParticipantsAdapter participantsAdapter;
    ImageView placeholder_participants;
    TextView placeholder_participants_text;
    TextView numberOfParticipants;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participants_activity);
        api = Api.getInstance().apiModel;

        // get event id from intent
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            // if no event id is stored, bring them back to manage events activity screen
            backToManageEvents();
        } else {
            eventId = extras.getString(Constants.SELECTED_EVENT_KEY);
            if (eventId == null) {
                // if no event id is stored, bring them back to manage events activity screen
                backToManageEvents();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        placeholder_participants = findViewById(R.id.placeholder_participants);
        placeholder_participants_text = findViewById(R.id.placeholder_participants_text);
        numberOfParticipants = findViewById(R.id.number_of_participants);
        recyclerView = findViewById(R.id.participantsRecyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Call<ArrayList<User>> call = api.getParticipants(eventId);
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ParticipantsActivity.this, "Oops the data could not be fetched, please try again!", Toast.LENGTH_LONG).show();
                } else {
                    participantsList = response.body();

                    participantsAdapter = new ParticipantsAdapter(participantsList);
                    RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(pLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(participantsAdapter);

                    if (!participantsList.isEmpty()) {
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ParticipantsActivity.this, DividerItemDecoration.VERTICAL);
                        recyclerView.addItemDecoration(dividerItemDecoration);
                    }

                    numberOfParticipants.setText("(" + response.body().size() + ")");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ParticipantsActivity.this, "Error: please check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void backToManageEvents() {
        Toast.makeText(this, "Oops, this event could not be fetched!", Toast.LENGTH_LONG).show();
        finish();
    }
}
