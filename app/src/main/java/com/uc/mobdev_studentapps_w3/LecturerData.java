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
import android.view.animation.AlphaAnimation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.mobdev_studentapps_w3.adapter.LecturerAdapter;
import com.uc.mobdev_studentapps_w3.model.Lecturer;

import java.util.ArrayList;
import java.util.List;

public class LecturerData extends AppCompatActivity {

    Toolbar toolbar;
    DatabaseReference db_lecturer;
    private ArrayList<Lecturer> list_lecturer = new ArrayList<>();
    RecyclerView rv_lecturer_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_data);

        toolbar = findViewById(R.id.tb_lect_data);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rv_lecturer_data = findViewById(R.id.rv_lectData);

        db_lecturer = FirebaseDatabase.getInstance().getReference("lecturer");

        fetchLecturerData();
    }

    public void fetchLecturerData() {
        db_lecturer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_lecturer.clear();
                rv_lecturer_data.setAdapter(null);
                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Lecturer lecturer = childSnapshot.getValue(Lecturer.class);
                    list_lecturer.add(lecturer);
                }
                showLecturerData(list_lecturer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showLecturerData(final ArrayList<Lecturer> list) {
        rv_lecturer_data.setLayoutManager(new LinearLayoutManager(LecturerData.this));
        LecturerAdapter lecturerAdapter = new LecturerAdapter(LecturerData.this);
        lecturerAdapter.setListLecturer(list);
        rv_lecturer_data.setAdapter(lecturerAdapter);

        ItemClickSupport.addTo(rv_lecturer_data).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getBaseContext(), LecturerDetail.class);
                Lecturer lecturer = new Lecturer(list.get(position).getId(), list.get(position).getName(), list.get(position).getGender(), list.get(position).getExpert());
                intent.putExtra("data_lecturer", lecturer);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent;
            intent = new Intent(LecturerData.this, AddLecturer.class);
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
        intent = new Intent(LecturerData.this, AddLecturer.class);
        intent.putExtra("action", "add");
        startActivity(intent);
        finish();
    }

}