package com.samulit.halal_pay_admin_panel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DetailsActivity extends AppCompatActivity {

    private TextView name, amount, date, time, status, type, number, interest, paymentId, text;
    private Button done, cancel;

    private DatabaseReference databaseReference, databaseReference2, databaseReference3, databaseReference4;

    private String getIntentUID, getIntentType, Name, Amount, Date, Time, Status, Type, Number, Interest,
            PaymentID, UID, Total, TotalAmount, TotalWithdraw, userTotalDepositBalance, WeekMonthYear, Interest_String, InterestMoney_String;

    private int counter;
    double TotalAmount_int, Amount_int, TotalWithdraw_int, TotalDeposit_int;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        status = findViewById(R.id.status);
        type = findViewById(R.id.Type);
        number = findViewById(R.id.number);
        interest = findViewById(R.id.interest);
        paymentId = findViewById(R.id.paymentID);
        text = findViewById(R.id.text);

        done = findViewById(R.id.button8);
        cancel = findViewById(R.id.button9);

        getIntentUID = getIntent().getStringExtra("user_uid");
        getIntentType = getIntent().getStringExtra("Type");

        databaseReference = FirebaseDatabase.getInstance().getReference(getIntentType);
        databaseReference.child(getIntentUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Name = String.valueOf(snapshot.child("userName").getValue());
                    Status = String.valueOf(snapshot.child("status").getValue());
                    Date = String.valueOf(snapshot.child("Date").getValue());
                    Time = String.valueOf(snapshot.child("Time").getValue());
                    UID = String.valueOf(snapshot.child("userUID").getValue());

                    name.setText("Name : \t" + Name);
                    status.setText("Status : \t" + Status);
                    date.setText("Date : \t" + Date);
                    time.setText("Time : \t" + Time);

                    if (Status.equals("Cancel!")){
                        text.setText("Cancel!");
                        text.setVisibility(View.VISIBLE);
                        done.setVisibility(View.GONE);
                        cancel.setVisibility(View.GONE);
                    }else if(Status.equals("Successfully Done")){
                        text.setText("Successfully Add Money!");
                        text.setVisibility(View.VISIBLE);
                        done.setVisibility(View.GONE);
                        cancel.setVisibility(View.GONE);
                    }else if (Status.equals("done...")){
                        text.setVisibility(View.GONE);
                        done.setVisibility(View.GONE);
                        cancel.setVisibility(View.GONE);
                    }else {
                        if (!getIntentType.equals("DepositRequest")){
                            cancel.setVisibility(View.GONE);
                        }
                    }

                    switch (getIntentType) {
                        case "DonationRequest":
                            Amount = String.valueOf(snapshot.child("DonationAmount").getValue());
                            amount.setText("Donation Amount : \t" + Amount);
                            amount.setVisibility(View.VISIBLE);
                            break;

                        case "WithdrawRequest":
                            Amount = String.valueOf(snapshot.child("withdrawMoney").getValue());
                            Type = String.valueOf(snapshot.child("withdrawType").getValue());
                            Number = String.valueOf(snapshot.child("WithdrawPhone").getValue());

                            type.setText("Withdraw Type : \t" + Type);
                            number.setText("Withdraw Number : \t" + Number);
                            amount.setText("Withdraw Amount : \t" + Amount);
                            amount.setVisibility(View.VISIBLE);
                            type.setVisibility(View.VISIBLE);
                            number.setVisibility(View.VISIBLE);
                            break;

                        case "DepositRequest":
                            Amount = String.valueOf(snapshot.child("DepositAmount").getValue());
                            Type = String.valueOf(snapshot.child("depositType").getValue());
                            Number = String.valueOf(snapshot.child("depositNumber").getValue());
                            Interest = String.valueOf(snapshot.child("InterestType").getValue());
                            PaymentID = String.valueOf(snapshot.child("PaymentID").getValue());

                            paymentId.setText("Payment ID : \t" + PaymentID);
                            interest.setText("Interest Type : \t" + Interest);
                            type.setText("Deposit Type : \t" + Type);
                            number.setText("Deposit Number : \t" + Number);
                            amount.setText("Deposit Amount : \t" + Amount);
                            amount.setVisibility(View.VISIBLE);
                            type.setVisibility(View.VISIBLE);
                            number.setVisibility(View.VISIBLE);
                            interest.setVisibility(View.VISIBLE);
                            paymentId.setVisibility(View.VISIBLE);
                            break;

                        default:
                            Amount = String.valueOf(snapshot.child("LoanAmount").getValue());
                            Type = String.valueOf(snapshot.child("LoanType").getValue());
                            Number = String.valueOf(snapshot.child("LoanPhone").getValue());

                            type.setText("Withdraw Type : \t" + Type);
                            number.setText("Withdraw Number : \t" + Number);
                            amount.setText("Withdraw Amount : \t" + Amount);
                            amount.setVisibility(View.VISIBLE);
                            type.setVisibility(View.VISIBLE);
                            number.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        done.setOnClickListener(view -> {
            databaseReference = FirebaseDatabase.getInstance().getReference(getIntentType).child(getIntentUID);
            databaseReference2 = FirebaseDatabase.getInstance().getReference();
            databaseReference3 = FirebaseDatabase.getInstance().getReference("UserData").child(UID);
            databaseReference4 = FirebaseDatabase.getInstance().getReference("InterestMoney");
            counter = 0;

            databaseReference2.child("UserData").child(UID).addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        TotalAmount = String.valueOf(snapshot.child("usesCurrentBalance").getValue());
                        TotalWithdraw = String.valueOf(snapshot.child("TotalWithdraw").getValue());
                        userTotalDepositBalance = String.valueOf(snapshot.child("userTotalDepositBalance").getValue());
                        WeekMonthYear = String.valueOf(snapshot.child("WeekMonthYear").getValue());
                        Interest_String = String.valueOf(snapshot.child("Interest").getValue());
                        InterestMoney_String = String.valueOf(snapshot.child("InterestMoney").getValue());

                        TotalAmount_int = Double.parseDouble(TotalAmount);
                        Amount_int = Double.parseDouble(Amount);
                        TotalWithdraw_int = Double.parseDouble(TotalWithdraw);
                        TotalDeposit_int = Double.parseDouble(userTotalDepositBalance);

                        // Check getIntentType......................................................
                        if (getIntentType.equals("DonationRequest")){
                            if (TotalAmount_int >= Amount_int){
                                Total = String.valueOf(TotalAmount_int - Amount_int);

                                if (counter == 0) {
                                    databaseReference3.child("usesCurrentBalance").setValue(Total);
                                    databaseReference.child("status").setValue("done...");

                                    Toast.makeText(DetailsActivity.this, "Successfully Done!", Toast.LENGTH_SHORT).show();
                                    text.setVisibility(View.GONE);
                                    done.setVisibility(View.GONE);
                                    cancel.setVisibility(View.GONE);
                                    counter++;
                                }

                            }else {
                                text.setText("The user's current balance is less than the donation amount");
                                Toast.makeText(DetailsActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }

                        }else if (getIntentType.equals("WithdrawRequest")){
                            if (TotalAmount_int >= Amount_int){
                                Total = String.valueOf(TotalAmount_int - Amount_int);
                                String sumWithdraw = String.valueOf(TotalWithdraw_int + Amount_int);

                                if (counter == 0) {
                                    databaseReference3.child("usesCurrentBalance").setValue(Total);
                                    databaseReference.child("status").setValue("done...");
                                    databaseReference3.child("TotalWithdraw").setValue(sumWithdraw);

                                    Toast.makeText(DetailsActivity.this, "Successfully Done!", Toast.LENGTH_SHORT).show();
                                    text.setVisibility(View.GONE);
                                    done.setVisibility(View.GONE);
                                    cancel.setVisibility(View.GONE);
                                    counter++;
                                }

                            }else {
                                text.setText("The user's current balance is less than the donation amount");
                                Toast.makeText(DetailsActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }

                        }else if (getIntentType.equals("DepositRequest")){
                            Total = String.valueOf(TotalAmount_int + Amount_int);
                            String sumDeposit = String.valueOf(TotalDeposit_int + Amount_int);

                            if (counter == 0) {
                                databaseReference.child("status").setValue("Successfully Done");
                                databaseReference3.child("userTotalDepositBalance").setValue(sumDeposit);

                                Toast.makeText(DetailsActivity.this, "Successfully Done!", Toast.LENGTH_SHORT).show();
                                text.setVisibility(View.GONE);
                                done.setVisibility(View.GONE);
                                cancel.setVisibility(View.GONE);
                                counter++;
                            }

                        }else {
                            Toast.makeText(DetailsActivity.this, "Successfully Done!", Toast.LENGTH_SHORT).show();
                            databaseReference.child("status").setValue("done...");
                            text.setVisibility(View.GONE);
                            done.setVisibility(View.GONE);
                            cancel.setVisibility(View.GONE);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });


        cancel.setOnClickListener(view -> {
            databaseReference = FirebaseDatabase.getInstance().getReference(getIntentType).child(getIntentUID);
            databaseReference.child("status").setValue("Cancel!");
        });

    }


}