package com.example.plent.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plent.R;
import com.example.plent.models.User;

import java.util.List;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.MyViewHolder> {

    private List<User> participantsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, studentId, email;

        public MyViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.participant_name);
            studentId = view.findViewById(R.id.participant_id);
            email = view.findViewById(R.id.participant_email);
        }
    }

    public ParticipantsAdapter(List<User> participantsList) {
        this.participantsList = participantsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View participantsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.participants_card, parent, false);

        return new MyViewHolder(participantsView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User participant = participantsList.get(position);
        holder.name.setText(participant.getName());
        holder.studentId.setText(participant.getStudentId());
        holder.email.setText(participant.getEmail());
    }

    @Override
    public int getItemCount() {
        return participantsList.size();
    }
}
