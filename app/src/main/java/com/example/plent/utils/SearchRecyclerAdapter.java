package com.example.plent.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plent.R;
import com.example.plent.models.Event;

import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<Event> eventList;
    private List<Event> eventListAll;

    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_EVENT = 1;
    private static final int VIEW_TYPE_SPECIFIC_EVENT = 0;


    public SearchRecyclerAdapter(List<Event> eventList) {
        this.eventList = eventList;
        this.eventListAll = new ArrayList<>(eventList);
    }

    @Override
    public int getItemCount() {
        if (eventList.size() == 0) {
            return 1;
        } else {
            return eventList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (eventList.size() == 0){
            return VIEW_TYPE_EMPTY;
        }else{
            Event e = eventList.get(0);
            for (int i = 1; i < eventList.size(); i++) {
                if(e.getType()!=eventList.get(i).getType()){
                    return VIEW_TYPE_EVENT;
                }else{
                    e = eventList.get(i);
                }
            }
        }
        return VIEW_TYPE_SPECIFIC_EVENT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case VIEW_TYPE_EVENT:
                View eventView = layoutInflater.inflate(R.layout.search_event_item, parent, false);
                viewHolder = new EventViewHolder(eventView);
                break;
            case VIEW_TYPE_SPECIFIC_EVENT:
                View specificEventView = layoutInflater.inflate(R.layout.search_event_item,parent, false );
                viewHolder = new SpecificEventViewHolder(specificEventView);
                break;

            default:
                View v0 = layoutInflater.inflate(R.layout.search_placeholder, parent, false);
                viewHolder = new EmptyViewHolder(v0);
                break;
        }
        return viewHolder;
    }

    //TRYING SOMETHING HERE

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch(holder.getItemViewType()){
            case VIEW_TYPE_EVENT:
                EventViewHolder eventViewHolder = (EventViewHolder) holder;
                setEventDetails(eventViewHolder, position);
                break;
            case VIEW_TYPE_SPECIFIC_EVENT:
                SpecificEventViewHolder specificEventViewHolder = (SpecificEventViewHolder) holder;
                setSpecificEventDetails(specificEventViewHolder, position);
                break;
            default:
                break;

        }
    }

    private void setEventDetails (EventViewHolder holder, int position) {
        Event current_event = eventList.get(position);
        holder.name.setText(current_event.getTitle());
        holder.location.setText(current_event.getLocation());
        holder.time.setText(current_event.getStartTime().toString()+" to "+current_event.getEndTime().toString());
        // need to set poster image url
    }
    private void setSpecificEventDetails (SpecificEventViewHolder vh, int position) {
        Event current_event= eventList.get(position);
        //TODO 2 set all the attributes from the event object to the instantiated view instances
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
                filteredList.clear();
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

    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            ImageView poster;
            TextView name;
            TextView time;
            TextView location;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.search_event_poster);
            name = itemView.findViewById(R.id.search_event_name);
            time = itemView.findViewById(R.id.search_event_time);
            location = itemView.findViewById(R.id.seach_event_location);
        }
        //TODO 3 fix this onclick...
        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(v.getContext(), EventActivity.class);
//            intent.putExtra(Constants.SELECTED_EVENT_KEY, Constants.SKIP_BACKEND ? "5fb96424fe88a67bb74b4289" : "5fb937bce230d0e3a9e2f912"); // 5fb937bce230d0e3a9e2f912 - actual id in db  // 5fb96424fe88a67bb74b4289 - dummy id
//            v.getContext().startActivity(intent);
            Toast.makeText(v.getContext(),"Hello", Toast.LENGTH_LONG ).show();
        }
    }
    class SpecificEventViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView eventTitle;
        //TODO 1 create all the necessary view instances and instantiate them in constructor
        public SpecificEventViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.see_all_image);
            eventTitle = itemView.findViewById(R.id.see_all_event_title);
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
