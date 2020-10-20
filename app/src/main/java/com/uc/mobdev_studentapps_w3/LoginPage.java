package com.uc.mobdev_studentapps_w3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.mobdev_studentapps_w3.model.Student;

public class LoginPage extends AppCompatActivity {

    private ImageButton btn_back;
    private EditText email, password;
    private Button btn_login;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();

        btn_back = findViewById(R.id.imgbtn_loginPage_back);
        email = findViewById(R.id.editText_loginPage_email);
        password = findViewById(R.id.editText_loginPage_pass);
        btn_login = findViewById(R.id.btn_loginPage_login);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        //showProgressDialog();
        String _email = email.getText().toString();
        String _password = password.getText().toString();

        mAuth.signInWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //hideProgressDialog();

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginPage.this, "Sign In Success", Toast.LENGTH_SHORT).show();
                            onAuthSuccess();
                        } else {
                            Toast.makeText(LoginPage.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess() {
        startActivity(new Intent(LoginPage.this, StudentArea.class));
        finish();
    }
}