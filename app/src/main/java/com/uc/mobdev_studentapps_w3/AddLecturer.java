package com.uc.mobdev_studentapps_w3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.mobdev_studentapps_w3.model.Lecturer;

import java.util.HashMap;
import java.util.Map;

public class AddLecturer extends AppCompatActivity {

    private TextView title;
    private Toolbar bar;
    private ImageButton btn_back;
    private EditText lecturerName, lecturerExpertise;
    private RadioGroup rg_lecturerGender;
    private RadioButton radio_gender;
    private String name="", gender="male", expert="", action="";
    private Button btn_addLecturer;
    Dialog dialog;

    private Lecturer lecturer;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecturer);

        //database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //

        bar = findViewById(R.id.tb_addLect);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("");

        bar = findViewById(R.id.tb_addLect);
        btn_back = findViewById(R.id.imgbtn_addLecturer_back);
        title = findViewById(R.id.tb_title_lectadd);
        btn_addLecturer = findViewById(R.id.btn_addLecturer_add);

        lecturerName = findViewById(R.id.editText_addLecturer_name);
        lecturerExpertise = findViewById(R.id.editText_addLecturer_expert);

        rg_lecturerGender = findViewById(R.id.radioGroup_addLecturer_gender);
        rg_lecturerGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio_gender = findViewById(checkedId);
                gender = radio_gender.getText().toString();
            }
        });

        dialog = Glovar.loadingDialog(AddLecturer.this);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setSupportActionBar(bar);

        Intent intent = getIntent();
        action = intent.getStringExtra("action");

        if (action.equals("add")) {
            title.setText("Add Lecturer");
            btn_addLecturer.setText("Add Lecturer");
            btn_addLecturer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        name = lecturerName.getText().toString();
                        expert = lecturerExpertise.getText().toString();
                        if (name != "" && expert != "") {
                            dialog.show();
                            addLecturer(name, gender, expert);
                            Intent intent;
                            intent = new Intent(AddLecturer.this, LecturerData.class);
                            startActivity(intent);
                            finish();
                        }
                }
            });
        } else {
            title.setText("Edit Lecturer");
            lecturer = intent.getParcelableExtra("edit_data_lect");
            lecturerName.setText(lecturer.getName());
            lecturerExpertise.setText(lecturer.getExpert());
            if (lecturer.getGender().equals("Male")) {
                rg_lecturerGender.check(R.id.radio_addLecturer_gender_male);
            } else {
                rg_lecturerGender.check(R.id.radio_addLecturer_gender_female);
            }
            btn_addLecturer.setText("Edit Lecturer");
            btn_addLecturer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    name = lecturerName.getText().toString().trim();
                    expert = lecturerExpertise.getText().toString().trim();
                    Map<String, Object> params = new HashMap<>();
                    params.put("name", name);
                    params.put("expert", expert);
                    params.put("gender", gender);
                    mDatabase.child("lecturer").child(lecturer.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.cancel();
                            Intent intent;
                            intent = new Intent(AddLecturer.this, LecturerData.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lecturer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lect_menu_1) {
            Intent intent;
            intent = new Intent(AddLecturer.this, LecturerData.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addLecturer(final String name, String gender, String expert) {
        String id = mDatabase.child("lecturer").push().getKey();
        Lecturer lecturer = new Lecturer(id, name, gender, expert);

        mDatabase.child("lecturer").child(id).setValue(lecturer).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.cancel();
                lecturerName.setText("");
                lecturerExpertise.setText("");
//                rg_lecturerGender.clearCheck();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //ksh dialog
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}