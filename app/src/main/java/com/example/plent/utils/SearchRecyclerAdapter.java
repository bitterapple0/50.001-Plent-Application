package com.example.plent.utils;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plent.R;
import com.example.plent.models.Event;
import com.example.plent.myActivities.EventActivity;
import com.example.plent.myActivities.SearchActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.MyViewHolder> implements Filterable {

    private List<Event> eventList;
    private List<Event> eventListAll;
    public SearchRecyclerAdapter(List<Event> eventList) {

        this.eventList = eventList;
        this.eventListAll = new ArrayList<>(eventList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.search_event_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Event current_event = eventList.get(position);
        holder.name.setText(current_event.getTitle());
        holder.location.setText(current_event.getLocation());
        holder.time.setText(current_event.getStart_time()+" to "+current_event.getEnd_time());
        // need to set poster image url

    }
    //Number of rows in your recycler view
    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter(){
        // runs on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Event> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(eventListAll);
            }else{
                for(Event e : eventListAll){
                    if(e.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(e);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }
        // runs on UI thread, takes in filterResults
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            eventList.clear();
            eventList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            ImageView poster;
            TextView name;
            TextView time;
            TextView location;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.search_event_poster);
            name = itemView.findViewById(R.id.search_event_name);
            time = itemView.findViewById(R.id.search_event_time);
            location = itemView.findViewById(R.id.seach_event_location);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), EventActivity.class);
            intent.putExtra(Constants.SELECTED_EVENT_KEY, Constants.SKIP_BACKEND ? "5fb96424fe88a67bb74b4289" : "5fb937bce230d0e3a9e2f912"); // 5fb937bce230d0e3a9e2f912 - actual id in db  // 5fb96424fe88a67bb74b4289 - dummy id
            v.getContext().startActivity(intent);
        }
    }
}
