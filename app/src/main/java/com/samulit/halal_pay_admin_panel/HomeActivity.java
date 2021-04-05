package com.samulit.halal_pay_admin_panel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private TextView PageName;
    private Button Back;
    private DatabaseReference databaseReference, databaseReference2;

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
        Intent intent = new Intent(HomeActivity.this, DepositTypeActivity.class);
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

        final EditText fiveYear = mView.findViewById(R.id.email);
        final EditText oneYear = mView.findViewById(R.id.name);
        final EditText oneMonth = mView.findViewById(R.id.age);
        final EditText fifteenDays = mView.findViewById(R.id.fifteenDays);
        final EditText oneWeek = mView.findViewById(R.id.week);
        final TextView enter1 = mView.findViewById(R.id.enter1);
        final TextView enter2 = mView.findViewById(R.id.enter2);
        final TextView enter3 = mView.findViewById(R.id.enter3);
        final LinearLayout ll1 = mView.findViewById(R.id.ll1);
        final LinearLayout ll2 = mView.findViewById(R.id.ll2);
        Button btn_okay = (Button)mView.findViewById(R.id.done);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel);

        ll1.setVisibility(View.VISIBLE);
        ll2.setVisibility(View.VISIBLE);
        enter1.setText("5 Years:    ");
        enter2.setText("1 Year :     ");
        enter3.setText("1 Month:   ");
        fiveYear.setHint("Enter Interest...");
        oneYear.setHint("Enter Interest...");
        oneMonth.setHint("Enter Interest...");

        databaseReference = FirebaseDatabase.getInstance().getReference("InterestMoney");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    fiveYear.setText(snapshot.child("FiveYears").getValue().toString());
                    oneYear.setText(snapshot.child("OneYear").getValue().toString());
                    oneMonth.setText(snapshot.child("OneMonth").getValue().toString());
                    fifteenDays.setText(snapshot.child("FifteenDays").getValue().toString());
                    oneWeek.setText(snapshot.child("OneWeek").getValue().toString());
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

            if (!fiveYear.getText().toString().equals(" ")){
                mer.put("FiveYears", fiveYear.getText().toString());
            }
            if (!oneYear.getText().toString().equals(" ")){
                mer.put("OneYear", oneYear.getText().toString());
            }
            if (!oneMonth.getText().toString().equals(" ")){
                mer.put("OneMonth", oneMonth.getText().toString());
            }
            if (!fifteenDays.getText().toString().equals(" ")){
                mer.put("FifteenDays", fifteenDays.getText().toString());
            }
            if (!oneWeek.getText().toString().equals(" ")){
                mer.put("OneWeek", oneWeek.getText().toString());
            }

            databaseReference.updateChildren(mer);
            Toast.makeText(getApplicationContext(), "Successfully Done!", Toast.LENGTH_SHORT).show();

            alertDialog.dismiss();

        }); // OnClickListener replace with lambda
        alertDialog.show();
    }

    public void Set_Help_Number(View view) {
        final EditText input = new EditText(this);
        input.setHint("Enter Mobile Number...");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        databaseReference = FirebaseDatabase.getInstance().getReference("HelpNumber");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Set Help Number");
        alert.setView(input);
        alert.setCancelable(false);

        alert.setPositiveButton("Done", (dialogInterface, i) -> {
            String number = input.getText().toString();

            databaseReference.child("number").setValue(number);

            Toast.makeText(this, "Successfully done!", Toast.LENGTH_LONG).show();

        });

        alert.setNegativeButton("Cancel", (dialogInterface, i) -> {
            // Nothing
        });

        alert.show();
    }

    public void Set_About_Us(View view) {
        final EditText input = new EditText(this);
        input.setHint("Enter About Us  Text...");

        databaseReference = FirebaseDatabase.getInstance().getReference("AboutUsText");
        databaseReference.child("Text").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    input.setText(Objects.requireNonNull(snapshot.getValue()).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Set About us");
        alert.setView(input);
        alert.setCancelable(false);

        alert.setPositiveButton("Done", (dialogInterface, i) -> {
            String text = input.getText().toString();

            databaseReference.child("Text").setValue(text);

            Toast.makeText(this, "Successfully done!", Toast.LENGTH_LONG).show();

        });

        alert.setNegativeButton("Cancel", (dialogInterface, i) -> {
            // Nothing
        });

        alert.show();
    }
}