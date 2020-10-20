package com.uc.mobdev_studentapps_w3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class AddCourse extends AppCompatActivity {

    private ImageButton btn_back;
    private EditText subject;
    private Spinner spin_day, spin_timeStart, spin_timeEnd, spin_lecturer;
    private Button btn_addCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        btn_back = findViewById(R.id.imgbtn_addCourse_back);

        spin_day = findViewById(R.id.spinner_addCourse_day);
        spin_timeStart = findViewById(R.id.spinner_addCourse_timeStart);
        spin_timeEnd = findViewById(R.id.spinner_addCourse_timeEnd);
        spin_lecturer = findViewById(R.id.spinner_addCourse_lecturer);
        btn_addCourse = findViewById(R.id.btn_addCourse_add);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}