package com.samulit.halal_pay_admin_panel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DepositTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_type);
    }

    public void DepositList(View view) {
        Intent intent = new Intent(DepositTypeActivity.this, DepositList.class);
        intent.putExtra("Type", view.getTag().toString());
        startActivity(intent);
    }
}