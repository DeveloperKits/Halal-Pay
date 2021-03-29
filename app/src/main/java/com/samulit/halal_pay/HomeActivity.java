package com.samulit.halal_pay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private TextView PageName;
    private Button Back;
    private DatabaseReference databaseReference;

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


    public void Deposit_List(View view) {
        Intent intent = new Intent(HomeActivity.this, DepositList.class);
        startActivity(intent);
    }

    public void Donation_List(View view) {
        Intent intent = new Intent(HomeActivity.this, DonationList.class);
        startActivity(intent);
    }

    public void Withdraw_List(View view) {
        Intent intent = new Intent(HomeActivity.this, WithdrawList.class);
        startActivity(intent);
    }

    public void Merchant_Number(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_box_edit_profile,null);

        final EditText Bkash = mView.findViewById(R.id.email);
        final EditText Rocket = mView.findViewById(R.id.name);
        final EditText Nogod = mView.findViewById(R.id.age);
        Button btn_okay = (Button)mView.findViewById(R.id.done);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel);

        databaseReference = FirebaseDatabase.getInstance().getReference("MerchantNumber");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Bkash.setText(snapshot.child("Bkash").getValue().toString());
                    Rocket.setText(snapshot.child("Rocket").getValue().toString());
                    Nogod.setText(snapshot.child("Nogod").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_cancel.setOnClickListener(v -> alertDialog.dismiss()); // OnClickListener replace with lambda
        btn_okay.setOnClickListener(v -> {

            databaseReference = FirebaseDatabase.getInstance().getReference("MerchantNumber");
            Map mer = new HashMap();

            if (!Bkash.getText().toString().equals(" ")){
                mer.put("Bkash", Bkash.getText().toString());
            }
            if (!Rocket.getText().toString().equals(" ")){
                mer.put("Rocket", Rocket.getText().toString());
            }
            if (!Nogod.getText().toString().equals(" ")){
                mer.put("Nogod", Nogod.getText().toString());
            }

            databaseReference.updateChildren(mer);
            Toast.makeText(getApplicationContext(), "Successfully Done!", Toast.LENGTH_SHORT).show();

            alertDialog.dismiss();

        }); // OnClickListener replace with lambda
        alertDialog.show();
    }

    public void Business_Loan_List(View view) {
        Intent intent = new Intent(HomeActivity.this, BusinessLoanList.class);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    public void Set_User_Type_Interest(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_box_edit_profile,null);

        final EditText Year = mView.findViewById(R.id.email);
        final EditText Month = mView.findViewById(R.id.name);
        final EditText Week = mView.findViewById(R.id.age);
        final TextView enter1 = mView.findViewById(R.id.enter1);
        final TextView enter2 = mView.findViewById(R.id.enter2);
        final TextView enter3 = mView.findViewById(R.id.enter3);
        Button btn_okay = (Button)mView.findViewById(R.id.done);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel);

        enter1.setText("Year:    ");
        enter2.setText("Month:");
        enter3.setText("Week: ");
        Year.setHint("Yearly Interest");
        Month.setHint("Monthly Interest");
        Week.setHint("Weekly Interest");

        databaseReference = FirebaseDatabase.getInstance().getReference("InterestMoney");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Year.setText(snapshot.child("Year").getValue().toString());
                    Month.setText(snapshot.child("Month").getValue().toString());
                    Week.setText(snapshot.child("Week").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_cancel.setOnClickListener(v -> alertDialog.dismiss()); // OnClickListener replace with lambda
        btn_okay.setOnClickListener(v -> {

            databaseReference = FirebaseDatabase.getInstance().getReference("InterestMoney");
            Map mer = new HashMap();

            if (!Year.getText().toString().equals(" ")){
                mer.put("Year", Year.getText().toString());
            }
            if (!Month.getText().toString().equals(" ")){
                mer.put("Month", Month.getText().toString());
            }
            if (!Week.getText().toString().equals(" ")){
                mer.put("Week", Week.getText().toString());
            }

            databaseReference.updateChildren(mer);
            Toast.makeText(getApplicationContext(), "Successfully Done!", Toast.LENGTH_SHORT).show();

            alertDialog.dismiss();

        }); // OnClickListener replace with lambda
        alertDialog.show();
    }
}