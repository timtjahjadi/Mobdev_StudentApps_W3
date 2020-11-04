package com.uc.mobdev_studentapps_w3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.mobdev_studentapps_w3.adapter.CourseAdapter;
import com.uc.mobdev_studentapps_w3.adapter.LecturerAdapter;
import com.uc.mobdev_studentapps_w3.model.Course;
import com.uc.mobdev_studentapps_w3.model.Lecturer;

import java.util.ArrayList;

public class CourseData extends AppCompatActivity {

    Toolbar toolbar;
    DatabaseReference db_course;
    private ArrayList<Course> list_course = new ArrayList<>();
    RecyclerView rv_course_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_data);

        toolbar = findViewById(R.id.tb_course);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rv_course_data = findViewById(R.id.rv_course);

        db_course = FirebaseDatabase.getInstance().getReference("course");

        fetchCourseData();
    }

    public void fetchCourseData() {
        db_course.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_course.clear();
                rv_course_data.setAdapter(null);
                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Course course = childSnapshot.getValue(Course.class);
                    list_course.add(course);
                }
                showCourserData(list_course);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showCourserData(final ArrayList<Course> list) {
        rv_course_data.setLayoutManager(new LinearLayoutManager(CourseData.this));
        CourseAdapter courseAdapter = new CourseAdapter(CourseData.this);
        courseAdapter.setListCourse(list);
        rv_course_data.setAdapter(courseAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent;
            intent = new Intent(CourseData.this, AddCourse.class);
            intent.putExtra("action", "add");
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(CourseData.this, AddCourse.class);
        intent.putExtra("action", "add");
        startActivity(intent);
        finish();
    }
}