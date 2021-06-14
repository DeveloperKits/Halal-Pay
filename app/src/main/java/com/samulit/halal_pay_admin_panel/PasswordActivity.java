package com.samulit.halal_pay_admin_panel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PasswordActivity extends AppCompatActivity {

    private TextInputEditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        text = findViewById(R.id.text);

    }

    public void AnotherActivity(View view) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Password");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pass = snapshot.child("password").getValue().toString();

                if (pass.equals(text.getText().toString())){
                    startActivity(new Intent(PasswordActivity.this, HomeActivity.class));
                }else {
                    Toast.makeText(PasswordActivity.this, "Password wrong! Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PasswordActivity.this, "Please check your internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}