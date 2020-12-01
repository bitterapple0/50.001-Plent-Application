package com.example.plent.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.plent.R;
import com.example.plent.models.ActivityType;
import com.example.plent.models.Event;
import com.example.plent.myActivities.EventActivity;
import com.example.plent.myActivities.FindEventsActivity;
import com.example.plent.myActivities.ManageEventsActivity;
import com.example.plent.myActivities.ParticipantsActivity;
import com.example.plent.utils.Constants;
import com.example.plent.utils.DateTimeUtils;
import com.example.plent.utils.NetworkImage;

import java.util.ArrayList;
import java.util.List;

import static com.example.plent.utils.Constants.SEE_ALL_CONTEXT;
import static com.example.plent.utils.Constants.SEARCH_CONTEXT;
import static com.example.plent.utils.Constants.SELECTED_EVENT_KEY;
import static com.example.plent.utils.ImageUtils.*;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Context context;
    private List<Event> eventList;
    private List<Event> eventListAll;
    private RecyclerView recyclerView;
    private ActivityType eventType = null;
    private String eventOrganiser = null;
    private boolean loading = false;
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_SEARCH_EVENT = 1;
    private static final int VIEW_TYPE_SEE_ALL_EVENT = 2;
    private static final int VIEW_TYPE_HORIZONTAL_EVENT = 3;
    private static final int VIEW_TYPE_MANAGE_EVENT = 4;
    private static final int VIEW_TYPE_LOADING = 5;

    public SearchRecyclerAdapter(List<Event> eventList, Context context) {
        this.eventList = eventList;
        this.eventListAll = new ArrayList<Event>(eventList);
        this.context = context;
    }

    public SearchRecyclerAdapter(List<Event> eventList, ActivityType eventType, Context context) {
        this.eventType = eventType;
        this.eventList= eventList;
        this.context = context;
    }
    public SearchRecyclerAdapter(List<Event> eventList, String eventOrganiser, Context context) {
        Log.i("SEARCH RECYCLER ADAPTER", "created something lols");
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
        Log.i("SEARCH RECYCLER ADAPTER", "checking the counttt");
        if (eventList == null || eventList.size() == 0) {
            return 1;
        } else {
            return eventList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (eventList == null) {
            return VIEW_TYPE_LOADING;
        }
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

                RecyclerView.LayoutParams paramsHori = (RecyclerView.LayoutParams) horizontalEventView.getLayoutParams();
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                paramsHori.width = (dpToPx(100, displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)); //280
                Log.i("WIDTH", String.valueOf(parent.getMeasuredWidth()));
                paramsHori.height = (dpToPx(150, displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)); //440
                horizontalEventView.setLayoutParams(paramsHori);
                viewHolder = new SeeAllEventViewHolder(horizontalEventView);
                break;
            case VIEW_TYPE_MANAGE_EVENT:
                View manageEventView = layoutInflater.inflate(R.layout.manage_event_card, parent, false);
                viewHolder = new ManageEventViewHolder(manageEventView);
                break;

            case VIEW_TYPE_LOADING:
                View loadingView = layoutInflater.inflate(R.layout.loading, parent, false);
                viewHolder = new LoadingViewHolder(loadingView);
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
        Log.i("SEARCH RECYCLER ADAPTER", "holder value: "+holder.getItemViewType());
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
                SeeAllEventViewHolder horizontalEventViewHolder;
                horizontalEventViewHolder = (SeeAllEventViewHolder) holder;
                setSeeAllEventDetails(horizontalEventViewHolder, position);
                break;
            case VIEW_TYPE_EMPTY:
                EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
                setEmptyEventDetails(emptyViewHolder, position);
                break;
            case VIEW_TYPE_MANAGE_EVENT:
                ManageEventViewHolder manageEventViewHolder = (ManageEventViewHolder) holder;
                setManageEventDetails(manageEventViewHolder, position);
                break;
            case VIEW_TYPE_LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                setLoading(loadingViewHolder, position);
                break;
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
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int imageHeight = dpToPx(110, displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        int imageWidth = dpToPx(80, displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        new NetworkImage.NetworkImageBuilder().setImageView(vh.poster).setDimensions(imageHeight, imageWidth).build().execute(current_event.getImageUrl());

        final String SEARCH_TO_EVENT = current_event.getId();
        vh.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra(Constants.SELECTED_EVENT_KEY, SEARCH_TO_EVENT);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(intent);
            }
        });
    }
    private void setSeeAllEventDetails (SeeAllEventViewHolder vh, int position) {
        final Event current_event = eventList.get(position);
        vh.eventTitle.setText(current_event.getTitle());
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int imageHeight = dpToPx(110, displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        int imageWidth = dpToPx(80, displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        new NetworkImage.NetworkImageBuilder().setImageView(vh.seeAllPoster).setDimensions(imageHeight, imageWidth).build().execute(current_event.getImageUrl());


        final String SEE_ALL_TO_EVENT = current_event.getId();

        vh.seeAllPoster.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Log.d("Calendar Adapter", "onClick " + SEE_ALL_TO_EVENT);
                intent.putExtra(SELECTED_EVENT_KEY, SEE_ALL_TO_EVENT);
                context.startActivity(intent);
            }
        });
    }

    private void setEmptyEventDetails (EmptyViewHolder vh, int position){
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager){

            vh.placeholderText.setText("No Events to display :(");

        } else if(eventOrganiser != null){
            vh.placeholderText.setText("Looks like you don't have any events to manage :(");
        }
    }

    private void setManageEventDetails (ManageEventViewHolder vh, int position){
        final Event current_event = eventList.get(position);
        vh.title.setText(current_event.getTitle());
        vh.location.setText(current_event.getLocation());
        vh.timing.setText(DateTimeUtils.formatDate(current_event.getDate()) + ", " + DateTimeUtils.formatTime24H(current_event.getStartTime()) + " - " + DateTimeUtils.formatTime24H(current_event.getEndTime()));

        vh.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ParticipantsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(SELECTED_EVENT_KEY, current_event.getId());
                context.startActivity(intent);
            }
        });
    }

    private void setLoading(LoadingViewHolder vh, int position) {
        if (loading) {
            vh.progressBar.setVisibility(View.INVISIBLE);
            Log.i("SEARCH RECYCLER ADAPTER", "setting loading invisible");
        } else {
            vh.progressBar.setVisibility(View.VISIBLE);
            Log.i("SEARCH RECYCLER ADAPTER", "setting loading visible");
        }
    }

    /****** Adapter Methods ******/
    public void refreshEvents(List<Event> new_eventList, ActivityType eventType, String eventOrganiser){
        eventListAll= new ArrayList<Event>(new_eventList);
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

    public void refreshManageEvents(List<Event> updatedEvents) {
        this.eventList = new ArrayList(updatedEvents);
        this.eventListAll = new ArrayList(updatedEvents);
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
            List<Event> filteredList = new ArrayList<Event>();
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

    public void setShouldStopLoading(boolean stopLoading) {
        Log.i("SEARCH RECYCLER ADAPTER", "stopping loading: " + stopLoading);
        loading = stopLoading;
        notifyDataSetChanged();
    }

/****** View Holder Class ******/
    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            ImageView poster;
            TextView name;
            TextView time;
            TextView location;
            Button button;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.search_event_poster);
            name = itemView.findViewById(R.id.search_event_name);
            time = itemView.findViewById(R.id.search_event_time);
            location = itemView.findViewById(R.id.search_event_location);
            button = itemView.findViewById(R.id.search_event_button);

            SEARCH_CONTEXT = itemView.getContext();
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
            SEE_ALL_CONTEXT = itemView.getContext();
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

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        LottieAnimationView progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    class ManageEventViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView timing;
        TextView location;
        RelativeLayout mainLayout;

        public ManageEventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.event_title);
            timing = itemView.findViewById(R.id.event_time);
            location = itemView.findViewById(R.id.event_location);
            mainLayout = itemView.findViewById(R.id.manage_events_card);
        }

    }

}
