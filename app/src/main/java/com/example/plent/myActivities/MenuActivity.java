package com.example.plent.myActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.plent.R;
import com.example.plent.adapters.SearchRecyclerAdapter;
import com.example.plent.models.User;
import com.example.plent.utils.Constants;
import com.google.gson.Gson;

public class MenuActivity extends AppCompatActivity {
    SearchRecyclerAdapter searchRecyclerAdapter;
    SharedPreferences mPreferences;
    int permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = getSharedPreferences(Constants.SHARED_PREF_FILE, MODE_PRIVATE);
        permission = getUserFromSharedPref().getPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem create_event = menu.findItem(R.id.manage_events);

        Log.i("Message", "Create Event Clicked");
        if (permission == 0) {
            create_event.setEnabled(false);
            Log.i("Message", "No permission");
        } else {
            Log.i("Message", "success");
            create_event.setEnabled(true);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.my_information) {
            Intent intent = new Intent(MenuActivity.this, MyInformationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

        if (id == R.id.my_calender) {
            Intent intent = new Intent(MenuActivity.this, CalendarActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

        if (id == R.id.find_events_and_activities) {
            Intent intent = new Intent(MenuActivity.this, FindEventsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

        if (id == R.id.manage_events) {
            Intent intent = new Intent (MenuActivity.this, ManageEventsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }

    private User getUserFromSharedPref() {
        Gson gson = new Gson();
        String json = mPreferences.getString(Constants.USER_KEY, null);

        if (json == null) {
            // redirect to login page if no user info stored
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();

            return null;
        }
        return gson.fromJson(json, User.class);
    }

}