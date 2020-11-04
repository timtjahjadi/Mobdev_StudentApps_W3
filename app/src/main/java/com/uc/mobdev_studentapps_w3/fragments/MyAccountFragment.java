package com.uc.mobdev_studentapps_w3.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uc.mobdev_studentapps_w3.LecturerData;
import com.uc.mobdev_studentapps_w3.LecturerDetail;
import com.uc.mobdev_studentapps_w3.MainActivity;
import com.uc.mobdev_studentapps_w3.R;
import com.uc.mobdev_studentapps_w3.StudentRegister;
import com.uc.mobdev_studentapps_w3.model.Student;

import java.util.List;

public class MyAccountFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextView name, nim, email, gender, age, address;
    private Button logout;

    public MyAccountFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_account, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();

        if (view != null) {
            name = view.findViewById(R.id.txt_acc_name);
            nim = view.findViewById(R.id.txt_acc_nim);
            email = view.findViewById(R.id.txt_acc_email);
            gender = view.findViewById(R.id.txt_acc_gender);
            age = view.findViewById(R.id.txt_acc_age);
            address = view.findViewById(R.id.txt_acc_address);

            logout = view.findViewById(R.id.btn_logout);

            //--READ data
            final SharedPreferences preferences = this.getActivity().getSharedPreferences("UserPreference", Context.MODE_PRIVATE);
            String _name = preferences.getString("UserName", "");
            String _nim = preferences.getString("UserNim", "");
            String _email = preferences.getString("UserEmail", "");
            String _gender = preferences.getString("UserGender", "");
            String _age = preferences.getString("UserAge", "");
            String _address = preferences.getString("UserAddress", "");

                name.setText(_name);
                nim.setText(_nim);
                email.setText(_email);
                gender.setText(_gender);
                age.setText(_age);
                address.setText(_address);

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Logout?")
                            .setMessage("Are you sure to sign out?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialogInterface, int i) {
//                                dialog.show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
//                                        dialog.cancel();
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("id", "");
                                            editor.putString("UserName", "");
                                            editor.putString("UserNim", "");
                                            editor.putString("UserEmail", "");
                                            editor.putString("UserPass", "");
                                            editor.putString("UserGender", "");
                                            editor.putString("UserAge", "");
                                            editor.putString("UserAddress", "");
                                            editor.putBoolean("isLogin", false);
                                            editor.commit();
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            startActivity(intent);
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
        }
    }
}