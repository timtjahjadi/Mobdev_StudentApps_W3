package com.uc.mobdev_studentapps_w3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uc.mobdev_studentapps_w3.model.Student;

import java.util.HashMap;
import java.util.Map;

public class StudentRegister extends AppCompatActivity implements TextWatcher {

    private TextView title;
    private ImageButton btn_back;
    private EditText input_email, input_password, input_name, input_nim, input_age, input_address;
    private RadioGroup rg_studentGender;
    private RadioButton radio_gender;
    private Button btn_register;
    private Toolbar toolbar;
    private String uid="", email="", password="", name="", nim="", gender="male", age="", address="", action="";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        btn_back = findViewById(R.id.imgbtn_studentRegister_back);
        input_email = findViewById(R.id.editText_studentRegister_email);
        input_password = findViewById(R.id.editText_studentRegister_pass);
        input_name = findViewById(R.id.editText_studentRegister_name);
        input_nim = findViewById(R.id.editText_studentRegister_nim);
        input_age = findViewById(R.id.editText_studentRegister_age);
        input_address = findViewById(R.id.editText_studentRegister_address);

        title = findViewById(R.id.tb_stud_add);

        //implements Text Watcher
        input_email.addTextChangedListener(this);
        input_password.addTextChangedListener(this);
        input_name.addTextChangedListener(this);
        input_nim.addTextChangedListener(this);
        input_age.addTextChangedListener(this);
        input_address.addTextChangedListener(this);

        rg_studentGender = findViewById(R.id.radio_studentRegister_gender);
        rg_studentGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio_gender = findViewById(checkedId);
                gender = radio_gender.getText().toString();
            }
        });

        dialog = Glovar.loadingDialog(StudentRegister.this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btn_register = findViewById(R.id.btn_studentRegister_register);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar = findViewById(R.id.toolbarStudent);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if (action.equalsIgnoreCase("add")){
            title.setText("Register Student");
            btn_register.setText("Register");
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    dialog.show();
                    email = input_email.getText().toString();
                    password = input_password.getText().toString();
                    name = input_name.getText().toString();
                    nim = input_nim.getText().toString();
                    age = input_age.getText().toString();
                    address = input_address.getText().toString();
                    addStudent(email, password, name, nim, gender, age, address);
                    finish();
                }
            });
        } else if (action.equalsIgnoreCase("edit")){
            title.setText("Edit Student");
            final String uid = intent.getStringExtra("edit_data_stud");
            final Query getData = mDatabase.child("student").child(uid);

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
                        String _pass = snapshot.child("password").getValue().toString();

                        input_email.setText(_email);
                        input_password.setText(_pass);
                        input_name.setText(_name);
                        input_nim.setText(_nim);
                        if (_gender.equalsIgnoreCase("male")) {
                            rg_studentGender.check(R.id.radio_studentRegister_gender_male);
                        } else {
                            rg_studentGender.check(R.id.radio_studentRegister_gender_female);
                        }
                        input_age.setText(_age);
                        input_address.setText(_address);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            input_email.setEnabled(false);
            input_password.setEnabled(false);

            btn_register.setText("Edit Student");
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    dialog.show();
                    email = input_email.getText().toString().trim();
                    password = input_password.getText().toString().trim();
                    name = input_name.getText().toString().trim();
                    nim = input_nim.getText().toString().trim();
                    age = input_age.getText().toString().trim();
                    address = input_address.getText().toString().trim();

                    Map<String, Object> params = new HashMap<>();
                    params.put("name", name);
                    params.put("nim", nim);
                    params.put("email", email);
                    params.put("gender", gender);
                    params.put("age", age);
                    params.put("address", address);
                    params.put("password", password);

                    mDatabase.child("student").child(uid).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                            dialog.cancel();
                            mAuth.signOut();
                            Intent intent;
                            intent = new Intent(StudentRegister.this, StudentData.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }
    }

    public void addStudent(final String email, final String password, final String name, final String nim, final String gender, final String age, final String address) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                    dialog.cancel();
                    uid = mAuth.getCurrentUser().getUid();
                    Student student = new Student(uid, email, password, name, nim, gender, age, address);
                    mDatabase.child("student").child(uid).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //ksh dialog
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //ksh dialog
                        }
                    });
                    mAuth.signOut();
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException error) {
                        Toast.makeText(StudentRegister.this, "Invalid email or Password", Toast.LENGTH_SHORT);
                    } catch (FirebaseAuthUserCollisionException error) {
                        Toast.makeText(StudentRegister.this, "Email Already Registered", Toast.LENGTH_SHORT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.student_list) {
            Intent intent;
            intent = new Intent(StudentRegister.this, StudentData.class);
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
        email = input_email.getText().toString().trim();
        password = input_password.getText().toString().trim();
        name = input_name.getText().toString().trim();
        nim = input_nim.getText().toString().trim();
        age = input_age.getText().toString().trim();
        address = input_address.getText().toString().trim();
        if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !nim.isEmpty() && !age.isEmpty() && !address.isEmpty()) {
            btn_register.setEnabled(true);
        } else {
            btn_register.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}