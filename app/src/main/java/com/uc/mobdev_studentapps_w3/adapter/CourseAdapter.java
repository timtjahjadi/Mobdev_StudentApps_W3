package com.uc.mobdev_studentapps_w3.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uc.mobdev_studentapps_w3.AddCourse;
import com.uc.mobdev_studentapps_w3.R;
import com.uc.mobdev_studentapps_w3.StudentRegister;
import com.uc.mobdev_studentapps_w3.model.Course;
import com.uc.mobdev_studentapps_w3.model.Lecturer;
import com.uc.mobdev_studentapps_w3.model.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CardViewViewHolder> {

    private Context context;
    private ArrayList<Course> listCourse;
    private ArrayList<String> list_lecturer = new ArrayList<>();
    private ArrayList<String> list_lecturerId = new ArrayList<>();
    private String[] timeArr = {
            "07:30:00",
            "08:00:00",
            "08:30:00",
            "09:00:00",
            "09:30:00",
            "10:00:00",
            "10:30:00",
            "11:00:00",
            "11:30:00",
            "12:00:00",
            "12:30:00",
            "13:00:00",
            "13:30:00",
            "14:00:00",
            "14:30:00",
            "15:00:00",
            "15:30:00",
            "16:00:00",
            "16:30:00"
    };

    DatabaseReference db_course;
    DatabaseReference db_lecturer;

    private ArrayList<Course> getListCourse() {
        return listCourse;
    }

    public void setListCourse(ArrayList<Course> listCourse) {
        this.listCourse = listCourse;
    }

    public CourseAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CourseAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_adapter, parent, false);
        return new CourseAdapter.CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CourseAdapter.CardViewViewHolder holder, int position) {
        final Course course = getListCourse().get(position);

        db_course = FirebaseDatabase.getInstance().getReference("course");

        holder.matkul.setText(course.getSubject());
        holder.day.setText(course.getDay());

        for (int i = 0 ; i < timeArr.length ; i++) {
            if (i == course.getTimeStart()) {
                holder.timeStart.setText(timeArr[i]);
            }
            if (i == course.getTimeEnd()) {
                holder.timeEnd.setText(timeArr[i]);
            }
        }

        db_lecturer = FirebaseDatabase.getInstance().getReference("lecturer");

        final Query getData = db_lecturer.child(course.getLecturerId());

        getData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.dosen.setText(snapshot.child("name").getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.uid = course.getId();

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Konfirmasi")
                        .setMessage("Are you sure to delete the data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
//                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
//                                        dialog.cancel();
                                        db_course.child(holder.uid).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                Toast.makeText(v.getContext(), "Delete Success!", Toast.LENGTH_SHORT);
                                                dialogInterface.cancel();
                                            }
                                        });
                                    }
                                }, 1000);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), AddCourse.class);
                in.putExtra("action", "edit");
                in.putExtra("edit_data_course", holder.uid);
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListCourse().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder {
        String uid;
        TextView matkul, day, timeStart, timeEnd, dosen;
        ImageView delete, edit;

        CardViewViewHolder(View itemView) {
            super(itemView);
            matkul = itemView.findViewById(R.id.txt_matkul);
            day = itemView.findViewById(R.id.txt_day);
            timeStart = itemView.findViewById(R.id.txt_timeStart);
            timeEnd = itemView.findViewById(R.id.txt_timeEnd);
            dosen = itemView.findViewById(R.id.txt_dosenMatkul);

            delete = itemView.findViewById(R.id.btn_delete_course);
            edit = itemView.findViewById(R.id.btn_edit_course);

        }
    }
}
