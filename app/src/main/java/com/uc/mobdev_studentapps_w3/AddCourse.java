package com.uc.mobdev_studentapps_w3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uc.mobdev_studentapps_w3.model.Course;
import com.uc.mobdev_studentapps_w3.model.Lecturer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCourse extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TextWatcher {

    private ImageButton btn_back;
    private Toolbar bar;
    private EditText subjectText;
    private Spinner spin_day, spin_timeStart, spin_timeEnd, spin_lecturer;
    private Button btn_addCourse;
    private ArrayAdapter<CharSequence> daysAdapter;
    private ArrayAdapter<String> timesAdapter;
    private ArrayAdapter<String> times2Adapter;
    private ArrayAdapter<String> lectAdapter;
    private List<String> timeList = new ArrayList<>();
    private List<String> timeList2 = new ArrayList<>();
    private List<String> list_lecturer = new ArrayList<>();
    private List<String> list_lecturerId = new ArrayList<>();
    private ArrayList<Course> list_course = new ArrayList<>();
    private DatabaseReference db_lecturer;
    DatabaseReference db_course;
    private DatabaseReference mDatabase;
    private String subject="", day="", lecturerId="", action="";
    String _subject, _day, _timeStart, _timeEnd, _lecturerId;
    private Integer timeStart, timeEnd;
    Dialog dialog;
    Boolean isOkay = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        btn_back = findViewById(R.id.imgbtn_addCourse_back);

        bar = findViewById(R.id.toolbar3);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("");

        subjectText = findViewById(R.id.editText_addCourse_subject);
        spin_day = findViewById(R.id.spinner_addCourse_day);
        spin_timeStart = findViewById(R.id.spinner_addCourse_timeStart);
        spin_timeEnd = findViewById(R.id.spinner_addCourse_timeEnd);
        spin_lecturer = findViewById(R.id.spinner_addCourse_lecturer);
        btn_addCourse = findViewById(R.id.btn_addCourse_add);

        //implements Text Watcher
        subjectText.addTextChangedListener(this);

        dialog = Glovar.loadingDialog(AddCourse.this);

        db_lecturer = FirebaseDatabase.getInstance().getReference("lecturer");
        db_course = FirebaseDatabase.getInstance().getReference("course");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        initTime();
        fetchLecturerData();
        fetchCourseData();

        daysAdapter = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_day.setAdapter(daysAdapter);
        spin_day.setOnItemSelectedListener(this);

        timesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeList);
        timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_timeStart.setAdapter(timesAdapter);
        spin_timeStart.setOnItemSelectedListener(this);

        times2Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeList2);
        times2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_timeEnd.setAdapter(times2Adapter);
        spin_timeEnd.setOnItemSelectedListener(this);

        lectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list_lecturer);
        lectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_lecturer.setAdapter(lectAdapter);
        spin_lecturer.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if (action.equalsIgnoreCase("add")){
//            title.setText("Register Student");
//            btn_register.setText("Register");
            btn_addCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subject = subjectText.getText().toString();
                    day = spin_day.getSelectedItem().toString();
                    timeStart = spin_timeStart.getSelectedItemPosition();
                    timeEnd = spin_timeEnd.getSelectedItemPosition();
                    lecturerId = list_lecturerId.get(spin_lecturer.getSelectedItemPosition());

                    //CHECKING
                    for (int i=0; i < list_course.size(); i++) {
                        //checking dosen
                        if (list_course.get(i).getLecturerId() == lecturerId) {
                            if (list_course.get(i).getDay() == day) {
                                if (timeStart >= list_course.get(i).getTimeStart() && timeEnd <= list_course.get(i).getTimeEnd()) {
                                    isOkay = false;
                                }
                                else if (timeStart < list_course.get(i).getTimeEnd() && timeStart >= list_course.get(i).getTimeStart()) {
                                    isOkay = false;
                                }
                                else if (timeStart <= list_course.get(i).getTimeStart() && timeEnd <= list_course.get(i).getTimeEnd()) {
                                    isOkay = false;
                                }
                                else {
                                    isOkay = true;
                                }
                            }
                        }
                    }
                    //

                    if (isOkay) {
//                    dialog.show();
                        addCourses(subject, day, timeStart, timeEnd, lecturerId);
                        Intent intent;
                        intent = new Intent(AddCourse.this, MainActivity.class);
                        isOkay = true;
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddCourse.this, "Schedule Overlap", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (action.equalsIgnoreCase("edit")){
//            title.setText("Edit Student");
            final String uid = intent.getStringExtra("edit_data_course");
            final Query getData = mDatabase.child("course").child(uid);

            getData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                         _subject = snapshot.child("subject").getValue().toString();
                         _day = snapshot.child("day").getValue().toString();
                         _timeStart = snapshot.child("timeStart").getValue().toString();
                         _timeEnd = snapshot.child("timeEnd").getValue().toString();
                         _lecturerId = snapshot.child("lecturerId").getValue().toString();

                        subjectText.setText(_subject);
                        switch(_day) {
                            case "Monday":
                                spin_day.setSelection(0);
                                break;
                            case "Tuesday":
                                spin_day.setSelection(1);
                                break;
                            case "Wednesday":
                                spin_day.setSelection(2);
                                break;
                            case "Thursday":
                                spin_day.setSelection(3);
                                break;
                            case "Friday":
                                spin_day.setSelection(4);
                                break;
                            default:
                                spin_day.setSelection(0);
                        }

                        for (int i = 0; i < timeList.size(); i++) {
                            if (Integer.parseInt(_timeStart) == i) {
                                spin_timeStart.setSelection(i);
                            }
                            if (Integer.parseInt(_timeEnd) == i) {
                                spin_timeEnd.setSelection(i);
                            }
                        }

                        for (int i = 0; i < list_lecturerId.size(); i++) {
                            if (_lecturerId == list_lecturerId.get(i)) {
                                spin_lecturer.setSelection(1);
                            }
                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            btn_addCourse.setText("Edit Course");
            btn_addCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    dialog.show();
                    subject = subjectText.getText().toString();
                    day = spin_day.getSelectedItem().toString();
                    timeStart = spin_timeStart.getSelectedItemPosition();
                    timeEnd = spin_timeEnd.getSelectedItemPosition();
                    lecturerId = list_lecturerId.get(spin_lecturer.getSelectedItemPosition());

                    //CHECKING
                    for (int i=0; i < list_course.size(); i++) {
                        //checking dosen
                        if (list_course.get(i).getLecturerId() == lecturerId) {
                            if (list_course.get(i).getDay() == day) {
                                if (timeStart >= list_course.get(i).getTimeStart() && timeEnd <= list_course.get(i).getTimeEnd()) {
                                    isOkay = false;
                                }
                                else if (timeStart < list_course.get(i).getTimeEnd() && timeStart >= list_course.get(i).getTimeStart()) {
                                    isOkay = false;
                                }
                                else if (timeStart <= list_course.get(i).getTimeStart() && timeEnd <= list_course.get(i).getTimeEnd()) {
                                    isOkay = false;
                                }
                                else {
                                    isOkay = true;
                                }
                            }
                        }
                    }

                    if (isOkay) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("day", day);
                        params.put("lecturerId", lecturerId);
                        params.put("subject", subject);
                        params.put("timeEnd", timeEnd);
                        params.put("timeStart", timeStart);

                        mDatabase.child("course").child(uid).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                            dialog.cancel();
                                isOkay = true;
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(AddCourse.this, "Schedule Overlap", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void addCourses(final String subject, String day, Integer timeStart, Integer timeEnd, String lecturerId) {
        String id = mDatabase.child("course").push().getKey();
        Course course = new Course(id, subject, day, lecturerId, timeStart, timeEnd);

        mDatabase.child("course").child(id).setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void fetchCourseData() {
        db_course.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_course.clear();
                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Course course = childSnapshot.getValue(Course.class);
                    list_course.add(course);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void initTime() {
        timeList = Arrays.asList(getResources().getStringArray(R.array.time));
        timeList2 = timeList;
    }

    public void fetchLecturerData() {
        db_lecturer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Lecturer lecturer = childSnapshot.getValue(Lecturer.class);
                    list_lecturer.add(lecturer.getName());
                    list_lecturerId.add(lecturer.getId());
                }
                lectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int selectedTime = spin_timeStart.getSelectedItemPosition();
        int selectedEndTime = spin_timeEnd.getSelectedItemPosition();

        if (selectedTime == timeList.size()-1) {
            spin_timeStart.setSelection(selectedTime-1);
            spin_timeEnd.setSelection(selectedTime-2);
            Toast.makeText(parent.getContext(), "Last Class starts on 16:00:00", Toast.LENGTH_SHORT).show();
        } else {
            if (selectedEndTime <= selectedTime) {
                spin_timeEnd.setSelection(selectedTime+1);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.course_menu1) {
            Intent intent;
            intent = new Intent(AddCourse.this, CourseData.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}