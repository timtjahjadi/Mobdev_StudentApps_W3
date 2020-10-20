package com.uc.mobdev_studentapps_w3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.mobdev_studentapps_w3.model.Lecturer;

import java.util.ArrayList;

public class LecturerDetail extends AppCompatActivity {

    Toolbar toolbar;
    DatabaseReference db_lecturer;
    private ArrayList<Lecturer> list_lecturer = new ArrayList<>();
    int pos = 0;
    TextView txt_name, txt_gender, txt_expert;
    Lecturer lecturer;
    ImageView btn_edit, btn_del;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_detail);

        toolbar = findViewById(R.id.tb_lect_detail);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db_lecturer = FirebaseDatabase.getInstance().getReference("lecturer");
        txt_name = findViewById(R.id.txt_lect_detail_nama);
        txt_gender = findViewById(R.id.txt_lect_detail_gender);
        txt_expert = findViewById(R.id.txt_lect_detail_expertise);
        btn_edit = findViewById(R.id.img_lect_detail_edit);
        btn_del = findViewById(R.id.img_lect_detail_delete);
        dialog = Glovar.loadingDialog(LecturerDetail.this);

        Intent intent = getIntent();
        pos = intent.getIntExtra("position", 0);
        lecturer = intent.getParcelableExtra("data_lecturer");

        txt_name.setText(lecturer.getName());
        txt_gender.setText(lecturer.getGender());
        txt_expert.setText(lecturer.getExpert());

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LecturerDetail.this)
                        .setTitle("Konfirmasi")
                        .setMessage("Are you sure to delete "+lecturer.getName()+" data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();
                                        db_lecturer.child(lecturer.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                              @Override
                                              public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                  Intent in = new Intent(LecturerDetail.this, LecturerData.class);
                                                  in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                  Toast.makeText(LecturerDetail.this, "Delete Success!", Toast.LENGTH_SHORT);
                                                  startActivity(in);
                                                  finish();
                                                  dialogInterface.cancel();
                                              }
                                        });
                                    }
                                }, 1000);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LecturerDetail.this, AddLecturer.class);
                in.putExtra("action", "edit");
                in.putExtra("edit_data_lect", lecturer);
                startActivity(in);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent;
            intent = new Intent(LecturerDetail.this, LecturerData.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(LecturerDetail.this, LecturerData.class);
        startActivity(intent);
        finish();
    }
}