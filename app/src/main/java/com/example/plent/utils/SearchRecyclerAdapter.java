package com.example.plent.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plent.R;
import com.example.plent.models.Event;

import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<Event> eventList;
    private List<Event> eventListAll;
    private RecyclerView recyclerView;
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_SEARCH_EVENT = 1;
    private static final int VIEW_TYPE_SEE_ALL_EVENT = 2;
    private static final int VIEW_TYPE_HORIZONTAL_EVENT = 3;



    public SearchRecyclerAdapter(List<Event> eventList) {
        this.eventList = eventList;
        this.eventListAll = new ArrayList<>(eventList);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
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
                    return VIEW_TYPE_SEARCH_EVENT;
                }else{
                    e = eventList.get(i);
                }
            }
        }
        if(recyclerView.getLayoutManager() instanceof GridLayoutManager){
            return VIEW_TYPE_SEE_ALL_EVENT;
        }else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            return VIEW_TYPE_HORIZONTAL_EVENT;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case VIEW_TYPE_SEARCH_EVENT:
                View eventView = layoutInflater.inflate(R.layout.search_event_item, parent, false);
                viewHolder = new EventViewHolder(eventView);
                break;
            case VIEW_TYPE_SEE_ALL_EVENT:
                View seeAllEventView = layoutInflater.inflate(R.layout.see_all_card, parent, false );

                GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) seeAllEventView.getLayoutParams();
                int width = parent.getMeasuredWidth() / 3;
                params.width = (width-20);
                params.height = (int) ((int) width*1.5);
                seeAllEventView.setLayoutParams(params);

                viewHolder = new SeeAllEventViewHolder(seeAllEventView);
                break;
            case VIEW_TYPE_HORIZONTAL_EVENT:
                View HorizontalEventView = layoutInflater.inflate(R.layout.see_all_card, parent, false );
                viewHolder = new SeeAllEventViewHolder(HorizontalEventView);
                break;

            default:
                View emptyView = layoutInflater.inflate(R.layout.search_placeholder, parent, false);
                if(recyclerView.getLayoutManager() instanceof GridLayoutManager){
                    GridLayoutManager.LayoutParams params_empty = (GridLayoutManager.LayoutParams) emptyView.getLayoutParams();
                    params_empty.width = parent.getMeasuredWidth();
                    emptyView.setLayoutParams(params_empty);

                }
                viewHolder = new EmptyViewHolder(emptyView);
                break;
        }
        return viewHolder;
    }

    //TRYING SOMETHING HERE

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch(holder.getItemViewType()){
            case VIEW_TYPE_SEARCH_EVENT:
                EventViewHolder eventViewHolder = (EventViewHolder) holder;
                setEventDetails(eventViewHolder, position);
                break;
            case VIEW_TYPE_SEE_ALL_EVENT:
                SeeAllEventViewHolder seeAllEventViewHolder = (SeeAllEventViewHolder) holder;
                setSeeAllEventDetails(seeAllEventViewHolder, position);
                break;
            case VIEW_TYPE_HORIZONTAL_EVENT:
                SeeAllEventViewHolder HorizontalEventViewHolder;
                HorizontalEventViewHolder = (SeeAllEventViewHolder) holder;
                setSeeAllEventDetails(HorizontalEventViewHolder, position);
                break;
            case VIEW_TYPE_EMPTY:
                EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
                setEmptyEventDetails(emptyViewHolder, position);
                break;
            default:
                break;

        }
    }

    private void setEventDetails (EventViewHolder vh, int position) {
        Event current_event = eventList.get(position);
        vh.name.setText(current_event.getTitle());
        vh.location.setText(current_event.getLocation());
        vh.time.setText(current_event.getStartTime().toString()+" to "+current_event.getEndTime().toString());
        //TODO set the image via getImageURL
    }
    private void setSeeAllEventDetails (SeeAllEventViewHolder vh, int position) {
        Event current_event = eventList.get(position);
        vh.eventTitle.setText(current_event.getTitle());
        new NetworkImage.NetworkImageBuilder().setImageView(vh.seeAllPoster).build().execute(current_event.getImageUrl());
        //vh.seeAllPoster.setImageResource(Integer.parseInt(current_event.getImageUrl()));
        //TODO set the image via getImageURL
    }

    private void setEmptyEventDetails (EmptyViewHolder vh, int position){
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager){
            vh.placeholderText.setText("No Events to display :(");
        }
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
            location = itemView.findViewById(R.id.search_event_location);
        }
        //TODO fix this onclick to direct to an intent
        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(v.getContext(), EventActivity.class);
//            intent.putExtra(Constants.SELECTED_EVENT_KEY, Constants.SKIP_BACKEND ? "5fb96424fe88a67bb74b4289" : "5fb937bce230d0e3a9e2f912"); // 5fb937bce230d0e3a9e2f912 - actual id in db  // 5fb96424fe88a67bb74b4289 - dummy id
//            v.getContext().startActivity(intent);
            Toast.makeText(v.getContext(),"Hello", Toast.LENGTH_LONG ).show();
        }
    }

    class SeeAllEventViewHolder extends RecyclerView.ViewHolder {
        ImageView seeAllPoster;
        TextView eventTitle;
        public SeeAllEventViewHolder(@NonNull View itemView) {
            super(itemView);
            seeAllPoster = itemView.findViewById(R.id.see_all_image);
            eventTitle = itemView.findViewById(R.id.see_all_event_title);
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        TextView placeholderText;
        ImageView placeholderImage;
        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            placeholderText = itemView.findViewById(R.id.placeholder_text);
            placeholderImage = itemView.findViewById(R.id.placeholder_image);
        }
    }

}
