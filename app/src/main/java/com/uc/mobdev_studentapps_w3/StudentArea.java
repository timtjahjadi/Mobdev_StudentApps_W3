package com.uc.mobdev_studentapps_w3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uc.mobdev_studentapps_w3.fragments.CoursesFragment;
import com.uc.mobdev_studentapps_w3.fragments.MyAccountFragment;
import com.uc.mobdev_studentapps_w3.fragments.MyScheduleFragment;

import java.util.HashSet;
import java.util.Set;

public class StudentArea extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_area);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyScheduleFragment()).commit();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("student");

        Query getData = mDatabase.child(mAuth.getCurrentUser().getUid());

        getData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String _name = snapshot.child("name").getValue().toString();
                    String _nim = snapshot.child("nim").getValue().toString();
                    String _email = snapshot.child("email").getValue().toString();
                    String _gender = snapshot.child("gender").getValue().toString();
                    String _age = snapshot.child("age").getValue().toString();
                    String _address = snapshot.child("address").getValue().toString();

                    //--SAVE Data
                    SharedPreferences preferences = getSharedPreferences("UserPreference", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("UserName", _name);
                    editor.putString("UserNim", _nim);
                    editor.putString("UserEmail", _email);
                    editor.putString("UserGender", _gender);
                    editor.putString("UserAge", _age);
                    editor.putString("UserAddress", _address);
                    editor.putBoolean("isLogin", true);
                    editor.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.menu_myschedule:
                    selectedFragment = new MyScheduleFragment();
                    break;
                case R.id.menu_course:
                    selectedFragment = new CoursesFragment();
                    break;
                case R.id.menu_myacc:
                    selectedFragment = new MyAccountFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        }
    };

    @Override
    public void onBackPressed() {
        //
    }
}