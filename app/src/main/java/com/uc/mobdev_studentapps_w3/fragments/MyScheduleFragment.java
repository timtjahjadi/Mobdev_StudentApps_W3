package com.uc.mobdev_studentapps_w3.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uc.mobdev_studentapps_w3.R;
import com.uc.mobdev_studentapps_w3.adapter.CourseStudentAreaAdapter;
import com.uc.mobdev_studentapps_w3.model.Course;
import com.uc.mobdev_studentapps_w3.model.CourseStudent;

import java.util.ArrayList;

public class MyScheduleFragment extends Fragment {

    DatabaseReference db_course;
    DatabaseReference db_relation;
    private ArrayList<CourseStudent> list_course = new ArrayList<>();
    private ArrayList<Course> list_schedule = new ArrayList<>();
    private ArrayList<Course> list_show = new ArrayList<>();
    RecyclerView rv_schedule;

    public MyScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_schedule = view.findViewById(R.id.rv_schedule);

        db_course = FirebaseDatabase.getInstance().getReference("course");
        db_relation = FirebaseDatabase.getInstance().getReference("course-student-relation");

        fetchCourseData();
    }

    public void fetchCourseData() {
        final SharedPreferences preferences = this.getActivity().getSharedPreferences("UserPreference", Context.MODE_PRIVATE);
        final String _name = preferences.getString("id", "");

        db_relation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_schedule.clear();
                rv_schedule.setAdapter(null);
                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    final CourseStudent data = childSnapshot.getValue(CourseStudent.class);

                    final Query getData = db_course.child(data.getCourseId());

                    getData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String id = snapshot.child("id").getValue().toString();
                                String subject = snapshot.child("subject").getValue().toString();
                                String day = snapshot.child("day").getValue().toString();
                                String lecturerId = snapshot.child("lecturerId").getValue().toString();
                                Integer start = Integer.parseInt(snapshot.child("timeStart").getValue().toString());
                                Integer end = Integer.parseInt(snapshot.child("timeEnd").getValue().toString());
                                Course data1 = new Course(id, subject, day, lecturerId, start, end);
                                list_schedule.add(data1);

                                list_course.add(data);
                                showCourseData(list_schedule);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showCourseData(final ArrayList<Course> list) {
        rv_schedule.setLayoutManager(new LinearLayoutManager(getActivity()));
        CourseStudentAreaAdapter courseStudentAreaAdapter = new CourseStudentAreaAdapter(getActivity());
        courseStudentAreaAdapter.setListCourse(list, true, list_course);
        rv_schedule.setAdapter(courseStudentAreaAdapter);
    }
}