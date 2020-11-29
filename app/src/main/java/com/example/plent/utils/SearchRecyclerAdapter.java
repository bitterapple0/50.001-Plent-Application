package com.example.plent.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plent.R;
import com.example.plent.models.ActivityType;
import com.example.plent.models.Event;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private static final String TAG = "SEARCH RECYCLER ADAPTER";
    private Context context;
    private List<Event> eventList;
    private List<Event> eventListAll;
    private RecyclerView recyclerView;
    private ActivityType eventType = null;
    private String eventOrganiser = null;
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_SEARCH_EVENT = 1;
    private static final int VIEW_TYPE_SEE_ALL_EVENT = 2;
    private static final int VIEW_TYPE_HORIZONTAL_EVENT = 3;
    private static final int VIEW_TYPE_MANAGE_EVENT = 4;



    public SearchRecyclerAdapter(List<Event> eventList, Context context) {
        this.eventList = eventList;
        this.eventListAll = new ArrayList<>(eventList);
        this.context = context;
    }

    public SearchRecyclerAdapter(List<Event> eventList, ActivityType eventType, Context context) {
        this.eventType = eventType;
        this.eventList= eventList;
        this.context = context;
    }
    public SearchRecyclerAdapter(List<Event> eventList, String eventOrganiser, Context context) {
        this.eventOrganiser = eventOrganiser;
        this.context = context;
        this.eventList=eventList;
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
        }else if(eventType != null){
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager){
                return VIEW_TYPE_SEE_ALL_EVENT;
            }else if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
                return VIEW_TYPE_HORIZONTAL_EVENT;
            }
        }else if(eventOrganiser != null){
            return VIEW_TYPE_MANAGE_EVENT;
        }
        return VIEW_TYPE_SEARCH_EVENT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        Log.i(TAG, "search recycler adapter " + viewType);
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
                View horizontalEventView = layoutInflater.inflate(R.layout.see_all_card, parent, false );
                RecyclerView.LayoutParams params1 = (RecyclerView.LayoutParams) horizontalEventView.getLayoutParams();
                // TODO need to find a way to fix the view
//                parent.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int horizontalWidth = parent.getMeasuredWidth() / 5;
                Log.i(TAG, "search recycler adapter, measured width " + horizontalWidth);
                params1.width = 180;
                horizontalEventView.setLayoutParams(params1);
                viewHolder = new SeeAllEventViewHolder(horizontalEventView);
                break;
            case VIEW_TYPE_MANAGE_EVENT:
                View manageEventView = layoutInflater.inflate(R.layout.manage_event_card, parent, false);
                viewHolder = new ManageEventViewHolder(manageEventView);
                break;

            default:
                View emptyView = layoutInflater.inflate(R.layout.search_placeholder, parent, false);
                if(recyclerView.getLayoutManager() instanceof GridLayoutManager){
                    GridLayoutManager.LayoutParams params_empty_see_all = (GridLayoutManager.LayoutParams) emptyView.getLayoutParams();
                    params_empty_see_all.width = parent.getMeasuredWidth();
                    emptyView.setLayoutParams(params_empty_see_all);

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
            case VIEW_TYPE_MANAGE_EVENT:
                ManageEventViewHolder manageEventViewHolder = (ManageEventViewHolder) holder;
                setManageEventDetails(manageEventViewHolder, position);
            default:
                break;

        }
    }
    /***** View Setters ******/
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
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int imageHeight = ImageUtils.dpToPx(110, displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        int imageWidth = ImageUtils.dpToPx(80, displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        new NetworkImage.NetworkImageBuilder().setImageView(vh.seeAllPoster).setDimensions(imageHeight, imageWidth).build().execute(current_event.getImageUrl());
    }

    private void setEmptyEventDetails (EmptyViewHolder vh, int position){
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager){
            vh.placeholderText.setText("No Events to display :(");
        } else if(eventOrganiser != null){
            vh.placeholderText.setText("Looks like you don't have any events to manage :(");
        }
    }

    private void setManageEventDetails (ManageEventViewHolder vh, int position){
        Event current_event = eventList.get(position);
        vh.title.setText(current_event.getTitle());
        vh.location.setText(current_event.getLocation());
        vh.timing.setText(DateTimeUtils.formatDate(current_event.getDate()) + ", " + DateTimeUtils.formatTime24H(current_event.getStartTime()) + " - " + DateTimeUtils.formatTime24H(current_event.getEndTime()));
    }

    /****** Adapter Methods ******/
    public void refreshEvents(List<Event> new_eventList, ActivityType eventType, String eventOrganiser){
        eventListAll= new ArrayList<>(new_eventList);
        eventList.clear();
        if (eventType != null){
            for(Event e : eventListAll){
                if(e.getType() == eventType){
                    this.eventList.add(e);
                }
            }
        }else if (eventOrganiser != null){
            for(Event e : eventListAll){
//            TODO manage event view
//            Need to implement the getter for the organiser
//            if(e.getOrganiser() == eventOrganiser){
//                this.eventList.add(e);
//            }
            }
        }else{
            this.eventList = new_eventList;
        }
        notifyDataSetChanged();
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


/****** View Holder Class ******/
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

    class ManageEventViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView timing;
        TextView location;

        public ManageEventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.event_title);
            timing = itemView.findViewById(R.id.event_time);
            location = itemView.findViewById(R.id.event_location);
        }
    }

}
