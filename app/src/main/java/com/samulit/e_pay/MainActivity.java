package com.samulit.e_pay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent goToMain = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(goToMain);
                finish();
                //progressBar.setVisibility(View.INVISIBLE);
            }
        },2000);
    }
}