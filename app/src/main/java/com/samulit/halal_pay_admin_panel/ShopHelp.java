package com.samulit.halal_pay_admin_panel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ShopHelp extends AppCompatActivity {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_help);
    }

    private void dialog(String s) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(ShopHelp.this);
        View view = getLayoutInflater().inflate(R.layout.custom_dialog_shop,null);

        final EditText amount = view.findViewById(R.id.email);
        final EditText coin = view.findViewById(R.id.email2);
        Button btn_okay = view.findViewById(R.id.done);
        Button btn_cancel = view.findViewById(R.id.cancel);

        databaseReference = FirebaseDatabase.getInstance().getReference("Coin").child(s);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                amount.setText(snapshot.child("amount").getValue().toString());
                coin.setText(snapshot.child("coin").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        alert.setView(view);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btn_cancel.setOnClickListener(v -> alertDialog.dismiss());

        btn_okay.setOnClickListener(v -> {
            Map mer = new HashMap();

            if (!amount.getText().toString().equals(" ") && !coin.getText().toString().equals(" ")){
                mer.put("amount", amount.getText().toString());
                mer.put("coin", coin.getText().toString());
            }

            databaseReference.updateChildren(mer);
            Toast.makeText(getApplicationContext(), "Successfully Done!", Toast.LENGTH_SHORT).show();

            alertDialog.dismiss();

        });
        alertDialog.show();
    }

    public void Shop1(View view) {
        dialog("1st");
    }

    public void Shop2(View view) {
        dialog("2nd");
    }

    public void Shop3(View view) {
        dialog("3rd");
    }

    public void Shop4(View view) {
        dialog("4rd");
    }

    public void Shop5(View view) {
        dialog("5th");
    }

    public void Shop6(View view) {
        dialog("6th");
    }
}