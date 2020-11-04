package com.uc.mobdev_studentapps_w3.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uc.mobdev_studentapps_w3.AddCourse;
import com.uc.mobdev_studentapps_w3.R;
import com.uc.mobdev_studentapps_w3.model.Course;
import com.uc.mobdev_studentapps_w3.model.CourseStudent;
import com.uc.mobdev_studentapps_w3.model.Lecturer;

import java.util.ArrayList;

public class CourseStudentAreaAdapter extends RecyclerView.Adapter<CourseStudentAreaAdapter.CardViewViewHolder> {
    private Context context;
    private ArrayList<Course> listCourse;
    private ArrayList<CourseStudent> list_relation;
    private Boolean isEnrolled;
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
    private String _name;

    DatabaseReference db_course;
    DatabaseReference db_relation;
    DatabaseReference db_lecturer;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private ArrayList<Course> getListCourse() {
        return listCourse;
    }

    public void setListCourse(ArrayList<Course> listCourse, Boolean isEnrolled, ArrayList<CourseStudent> list_relation) {
        this.listCourse = listCourse;
        this.isEnrolled = isEnrolled;
        this.list_relation = list_relation;
    }

    public CourseStudentAreaAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CourseStudentAreaAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_course_student_area_adapter, parent, false);
        return new CourseStudentAreaAdapter.CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewViewHolder holder, int position) {
        final Course course = getListCourse().get(position);

        db_course = FirebaseDatabase.getInstance().getReference("course");
        db_relation = FirebaseDatabase.getInstance().getReference("course-student-relation");

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

        SharedPreferences preferences = context.getSharedPreferences("UserPreference", Context.MODE_PRIVATE);
        this._name = preferences.getString("id", "");

        if (isEnrolled) {
            holder.add_remove.setText("Remove");
            holder.add_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Konfirmasi")
                            .setMessage("Are you sure to remove this course?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialogInterface, int i) {
//                                dialog.show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
//                                        dialog.cancel();
                                            for (int i = 0; i < list_relation.size(); i++) {
                                                if (list_relation.get(i).getStudentId() == _name) {
                                                    if (list_relation.get(i).getCourseId() == course.getId()){
                                                        db_relation.child(list_relation.get(i).getId()).removeValue(new DatabaseReference.CompletionListener() {
                                                            @Override
                                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                                Toast.makeText(context, "Remove Success!", Toast.LENGTH_SHORT);
                                                                dialogInterface.cancel();
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                            dialogInterface.cancel();
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
        } else {
            holder.add_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Konfirmasi")
                            .setMessage("Are you sure to take the course?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialogInterface, int i) {
//                                dialog.show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
//                                        dialog.cancel();
                                            addCourse(_name, course.getId());
                                            dialogInterface.cancel();
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
        }

        holder.uid = course.getId();
    }

    public void addCourse(String studId, String courseId) {
        String id = mDatabase.child("course-student-relation").push().getKey();
        CourseStudent data = new CourseStudent(id, studId, courseId);

        Boolean isOkay = true;

        //CHECKING
        for (int i = 0; i < list_relation.size(); i++) {
            if (list_relation.get(i).getStudentId() == _name){
                if (list_relation.get(i).getCourseId() == courseId){
                    Toast.makeText(context, "Schedule Overlap!!", Toast.LENGTH_SHORT);
                }
                else {
                    isOkay = true;
                }
            }
        }

        if (isOkay) {
            mDatabase.child("course-student-relation").child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
//                dialog.cancel();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //ksh dialog
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return getListCourse().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder {
        String uid;
        TextView matkul, day, timeStart, timeEnd, dosen;
        Button add_remove;

        CardViewViewHolder(View itemView) {
            super(itemView);
            matkul = itemView.findViewById(R.id.txt_stud_course_matkul);
            day = itemView.findViewById(R.id.txt_stud_course_day);
            timeStart = itemView.findViewById(R.id.txt_stud_course_timeStart);
            timeEnd = itemView.findViewById(R.id.txt_stud_course_timeEnd);
            dosen = itemView.findViewById(R.id.txt_stud_course_dosen);

            add_remove = itemView.findViewById(R.id.btn_enroll);
        }
    }
}