package com.uc.mobdev_studentapps_w3.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.mobdev_studentapps_w3.AddLecturer;
import com.uc.mobdev_studentapps_w3.ItemClickSupport;
import com.uc.mobdev_studentapps_w3.LecturerData;
import com.uc.mobdev_studentapps_w3.LecturerDetail;
import com.uc.mobdev_studentapps_w3.MainActivity;
import com.uc.mobdev_studentapps_w3.R;
import com.uc.mobdev_studentapps_w3.StudentData;
import com.uc.mobdev_studentapps_w3.StudentRegister;
import com.uc.mobdev_studentapps_w3.model.Student;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.CardViewViewHolder> {

    private Context context;
    private ArrayList<Student> listStudent;

    DatabaseReference db_student;

    private ArrayList<Student> getListStudent() {
        return listStudent;
    }

    public void setListStudent(ArrayList<Student> listStudent) {
        this.listStudent = listStudent;
    }

    public StudentAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public StudentAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_adapter, parent, false);
        return new StudentAdapter.CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentAdapter.CardViewViewHolder holder, int position) {
        final Student student = getListStudent().get(position);

        db_student = FirebaseDatabase.getInstance().getReference("student");

        holder.name.setText(student.getName());
        holder.nim.setText(student.getNim());
        holder.email.setText(student.getEmail());
        holder.gender.setText(student.getGender());
        holder.age.setText(student.getAge());
        holder.address.setText(student.getAddress());

        holder.uid = student.getId();

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Konfirmasi")
                        .setMessage("Are you sure to delete the data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
//                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
//                                        dialog.cancel();
                                        db_student.child(holder.uid).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                deleteUser(student.getEmail(), student.getPassword(), v);
//                                                Toast.makeText(v.getContext(), "Delete Success!", Toast.LENGTH_SHORT);
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

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), StudentRegister.class);
                in.putExtra("action", "edit");
                in.putExtra("edit_data_stud", holder.uid);
                context.startActivity(in);
            }
        });
    }

    public void deleteUser(String email, String pass, final View v) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(v.getContext(), "Delete Success!", Toast.LENGTH_SHORT);
                FirebaseUser user = mAuth.getCurrentUser();
                user.delete();
            }
        });
    }
    @Override
    public int getItemCount() {
        return getListStudent().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder {
        String uid;
        TextView name, nim, email, gender, age, address;
        ImageButton delete, edit;

        CardViewViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_stud_name);
            nim = itemView.findViewById(R.id.txt_stud_nim);
            email = itemView.findViewById(R.id.txt_stud_email);
            gender = itemView.findViewById(R.id.txt_stud_gender);
            age = itemView.findViewById(R.id.txt_stud_age);
            address = itemView.findViewById(R.id.txt_stud_address);

            delete = itemView.findViewById(R.id.btn_stud_delete);
            edit = itemView.findViewById(R.id.btn_stud_edit);

        }
    }
}