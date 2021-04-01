package com.samulit.halal_pay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent goToMain;
                if (user != null){
                    goToMain = new Intent(MainActivity.this, HomeActivity.class);
                }else {
                    goToMain = new Intent(MainActivity.this, LoginActivity.class);
                }
                startActivity(goToMain);
                finish();
            }
        },1200);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
