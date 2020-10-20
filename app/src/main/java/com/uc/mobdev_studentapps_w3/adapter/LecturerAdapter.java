package com.uc.mobdev_studentapps_w3.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uc.mobdev_studentapps_w3.model.Lecturer;
import com.uc.mobdev_studentapps_w3.R;

import java.util.ArrayList;

public class LecturerAdapter extends RecyclerView.Adapter<LecturerAdapter.CardViewViewHolder>{

    private Context context;
    private ArrayList<Lecturer> listLecturer;
    private ArrayList<Lecturer> getListLecturer() {
        return listLecturer;
    }

    public void setListLecturer(ArrayList<Lecturer> listLecturer) {
        this.listLecturer = listLecturer;
    }

    public LecturerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public LecturerAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecturer_adapter, parent, false);
        return new LecturerAdapter.CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LecturerAdapter.CardViewViewHolder holder, int position) {
        final Lecturer lecturer = getListLecturer().get(position);
        holder.name.setText(lecturer.getName());
        holder.gender.setText(lecturer.getGender());
        holder.expert.setText(lecturer.getExpert());
    }

    @Override
    public int getItemCount() {
        return getListLecturer().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView name, gender, expert;

        CardViewViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_lect_name);
            gender = itemView.findViewById(R.id.txt_lect_gender);
            expert = itemView.findViewById(R.id.txt_lect_expert);

        }
    }
}