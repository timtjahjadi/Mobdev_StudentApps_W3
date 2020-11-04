package com.uc.mobdev_studentapps_w3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private CardView add_student, add_course, add_lect, login;
    private ImageButton btn_addStudent, btn_addLecturer, btn_addCourse, btn_loginAsStudent;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private Boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_student = findViewById(R.id.btn_card_add_student);
        add_lect = findViewById(R.id.btn_card_add_lecturer);
        add_course = findViewById(R.id.btn_card_add_course);
        login = findViewById(R.id.btn_card_login);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        SharedPreferences preferences = getSharedPreferences("UserPreference", Context.MODE_PRIVATE);
        isLogin = preferences.getBoolean("isLogin", false);

        if (isLogin) {
            startActivity(new Intent(MainActivity.this, StudentArea.class));
        }

        add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StudentRegister.class);
                intent.putExtra("action", "add");
                startActivity(intent);
            }
        });

        add_lect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddLecturer.class);
                intent.putExtra("action", "add");
                startActivity(intent);
            }
        });

        add_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCourse.class);
                intent.putExtra("action", "add");
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginPage.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
       //
    }

}