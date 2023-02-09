package com.developerkits.admin_panel_cashnet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DepositTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_type);
    }

    public void DepositList(View view) {
        Create_helper_dialog(view.getTag().toString());
    }

    private void Create_helper_dialog(String string) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(DepositTypeActivity.this);
        View view = getLayoutInflater().inflate(R.layout.helper_dialog,null);

        Button successful = (Button)view.findViewById(R.id.button);
        Button pending = (Button)view.findViewById(R.id.button2);

        alert.setView(view);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        successful.setOnClickListener(view1 -> {

            create_intent("Successfully Done", string);
            alertDialog.dismiss();
        });

        pending.setOnClickListener(view1 -> {

            create_intent(" ", string);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void create_intent(String string, String i) {
        Intent intent = new Intent(DepositTypeActivity.this, DepositList.class);
        intent.putExtra("Type", i);
        intent.putExtra("string", string);
        startActivity(intent);
    }
}