package com.uc.mobdev_studentapps_w3.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uc.mobdev_studentapps_w3.AddCourse;
import com.uc.mobdev_studentapps_w3.CourseData;
import com.uc.mobdev_studentapps_w3.R;
import com.uc.mobdev_studentapps_w3.adapter.CourseAdapter;
import com.uc.mobdev_studentapps_w3.adapter.CourseStudentAreaAdapter;
import com.uc.mobdev_studentapps_w3.model.Course;
import com.uc.mobdev_studentapps_w3.model.CourseStudent;

import java.util.ArrayList;

public class CoursesFragment extends Fragment {

    DatabaseReference db_course;
    DatabaseReference db_relation;
    private ArrayList<CourseStudent> list_relation = new ArrayList<>();
    private ArrayList<Course> list_course = new ArrayList<>();
    RecyclerView rv_course_data;

    public CoursesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_course_data = view.findViewById(R.id.rv_available_course);

        db_course = FirebaseDatabase.getInstance().getReference("course");
        db_relation = FirebaseDatabase.getInstance().getReference("course-student-relation");

        fetchCourseData();
    }

    public void fetchCourseData() {
        final SharedPreferences preferences = this.getActivity().getSharedPreferences("UserPreference", Context.MODE_PRIVATE);
        final String _name = preferences.getString("id", "");

        db_course.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_course.clear();
                rv_course_data.setAdapter(null);
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Course course = childSnapshot.getValue(Course.class);
                    list_course.add(course);
                }
                showCourserData(list_course);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        db_relation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_relation.clear();
                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    final CourseStudent data = childSnapshot.getValue(CourseStudent.class);
                    list_relation.add(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showCourserData(final ArrayList<Course> list) {
        rv_course_data.setLayoutManager(new LinearLayoutManager(getActivity()));
        CourseStudentAreaAdapter courseStudentAreaAdapter = new CourseStudentAreaAdapter(getActivity());
        courseStudentAreaAdapter.setListCourse(list, false, list_relation);
        rv_course_data.setAdapter(courseStudentAreaAdapter);

    }

}