package com.samulit.halal_pay;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class HomeActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private TextView PageName;
    private Button Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        PageName = findViewById(R.id.pageName);
        Back = findViewById(R.id.back);

        progressDialog = new ProgressDialog(HomeActivity.this);



        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onBackPressed() {
        AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this)
                .setMessage("Are you sure! You want to exit this app?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("no", null)
                .create();
        dialog.show();
    }



}