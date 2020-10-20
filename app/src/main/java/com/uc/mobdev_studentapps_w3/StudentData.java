package com.uc.mobdev_studentapps_w3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.mobdev_studentapps_w3.adapter.LecturerAdapter;
import com.uc.mobdev_studentapps_w3.adapter.StudentAdapter;
import com.uc.mobdev_studentapps_w3.model.Lecturer;
import com.uc.mobdev_studentapps_w3.model.Student;

import java.util.ArrayList;

public class StudentData extends AppCompatActivity {

    Toolbar toolbar;
    DatabaseReference db_student;
    private ArrayList<Student> list_student = new ArrayList<>();
    RecyclerView rv_student_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_data);

        toolbar = findViewById(R.id.tb_student_data);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rv_student_data = findViewById(R.id.rv_student_data);

        db_student = FirebaseDatabase.getInstance().getReference("student");

        fetchStudentData();
    }

    public void fetchStudentData() {
        db_student.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_student.clear();
                rv_student_data.setAdapter(null);
                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Student student = childSnapshot.getValue(Student.class);
                    list_student.add(student);
                }
                showStudentData(list_student);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showStudentData(ArrayList<Student> list) {
        rv_student_data.setLayoutManager(new LinearLayoutManager(StudentData.this));
        StudentAdapter studentAdapter = new StudentAdapter(StudentData.this);
        studentAdapter.setListStudent(list);
        rv_student_data.setAdapter(studentAdapter);

        ItemClickSupport.addTo(rv_student_data).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent;
            intent = new Intent(StudentData.this, StudentRegister.class);
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
        intent = new Intent(StudentData.this, StudentRegister.class);
        intent.putExtra("action", "add");
        startActivity(intent);
        finish();
        finish();
    }
}