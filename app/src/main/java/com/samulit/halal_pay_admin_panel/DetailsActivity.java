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

    private TextView name, amount, date, time, status, type, number, interest, paymentId, text, Email;
    private Button done, cancel;

    private DatabaseReference databaseReference, databaseReference2, databaseReference3, databaseReference4;

    private String getIntentUID, getIntentType, Name, Amount, Date, Time, Status, Type, Number, Interest, email,
            PaymentID, UID, Total, TotalAmount, TotalWithdraw, userTotalDepositBalance, Interest_String, InterestMoney_String, string1, string2;

    private int counter;
    double TotalAmount_int, Amount_int, TotalWithdraw_int, TotalDeposit_int, Amount_double, InterestMoney_double;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
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
                    }else if (Status.equals("pending...")){
                        done.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.VISIBLE);
                    }else {
                        if (!getIntentType.equals("DepositRequest")){
                            done.setVisibility(View.GONE);
                        }
                    }

                    switch (getIntentType) {
                        case "DonationRequest":
                            Amount = String.valueOf(snapshot.child("DonationAmount").getValue());
                            amount.setText("Donation Amount : \t" + Amount);
                            amount.setVisibility(View.VISIBLE);
                            break;

                        case "WithdrawRequest":
                            email = String.valueOf(snapshot.child("userEmail").getValue());
                            Amount = String.valueOf(snapshot.child("withdrawMoney").getValue());
                            Type = String.valueOf(snapshot.child("withdrawType").getValue());
                            Number = String.valueOf(snapshot.child("WithdrawPhone").getValue());

                            Email.setText("Email : \t" + email);
                            type.setText("Withdraw Type : \t" + Type);
                            number.setText("Withdraw Number : \t" + Number);
                            amount.setText("Withdraw Amount : \t" + Amount);

                            Email.setVisibility(View.VISIBLE);
                            amount.setVisibility(View.VISIBLE);
                            type.setVisibility(View.VISIBLE);
                            number.setVisibility(View.VISIBLE);
                            break;

                        case "DepositRequest":
                            email = String.valueOf(snapshot.child("userEmail").getValue());
                            Amount = String.valueOf(snapshot.child("DepositAmount").getValue());
                            Type = String.valueOf(snapshot.child("depositType").getValue());
                            Number = String.valueOf(snapshot.child("depositNumber").getValue());
                            Interest = String.valueOf(snapshot.child("InterestType").getValue());
                            PaymentID = String.valueOf(snapshot.child("PaymentID").getValue());

                            Email.setText("Email : \t" + email);
                            paymentId.setText("Payment ID : \t" + PaymentID);
                            interest.setText("Interest Type : \t" + Interest);
                            type.setText("Deposit Type : \t" + Type);
                            number.setText("Deposit Number : \t" + Number);
                            amount.setText("Deposit Amount : \t" + Amount);

                            Email.setVisibility(View.VISIBLE);
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
            counter = 0;

            databaseReference2.child("UserData").child(UID).addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        TotalAmount = String.valueOf(snapshot.child("usesCurrentBalance").getValue());
                        TotalWithdraw = String.valueOf(snapshot.child("TotalWithdraw").getValue());
                        userTotalDepositBalance = String.valueOf(snapshot.child("userTotalDepositBalance").getValue());
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
                            String sumWithdraw = String.valueOf(TotalWithdraw_int + Amount_int);

                            if (counter == 0) {
                                databaseReference.child("status").setValue("done...");
                                databaseReference3.child("TotalWithdraw").setValue(sumWithdraw);

                                Toast.makeText(DetailsActivity.this, "Successfully Done!", Toast.LENGTH_SHORT).show();
                                text.setVisibility(View.GONE);
                                done.setVisibility(View.GONE);
                                cancel.setVisibility(View.GONE);
                                counter++;

                            }else {
                                text.setText("Save failed!");
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

                                if (!Interest.equals("Add Money")){
                                    addInterest(Interest_String, InterestMoney_String);
                                }

                                counter++;
                            }

                        }else {
                            Total = String.valueOf(TotalAmount_int + Amount_int);

                            if (counter == 0) {
                                databaseReference3.child("usesCurrentBalance").setValue(Total);
                                Toast.makeText(DetailsActivity.this, "Successfully Done!", Toast.LENGTH_SHORT).show();
                                databaseReference.child("status").setValue("done...");
                                text.setVisibility(View.GONE);
                                done.setVisibility(View.GONE);
                                cancel.setVisibility(View.GONE);

                                counter++;
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });


        cancel.setOnClickListener(view -> {
            if (getIntentType.equals("WithdrawRequest")){
                counter = 0;
                databaseReference2 = FirebaseDatabase.getInstance().getReference();
                databaseReference2.child("UserData").child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            TotalAmount = String.valueOf(snapshot.child("usesCurrentBalance").getValue());
                            TotalAmount_int = Double.parseDouble(TotalAmount);
                            Amount_int = Double.parseDouble(Amount);

                            if (counter == 0){
                                databaseReference3 = FirebaseDatabase.getInstance().getReference("UserData").child(UID);
                                Total = String.valueOf(TotalAmount_int + Amount_int);
                                databaseReference3.child("usesCurrentBalance").setValue(Total);
                                counter++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            databaseReference = FirebaseDatabase.getInstance().getReference(getIntentType).child(getIntentUID);
            databaseReference.child("status").setValue("Cancel!");
        });

    }


    private void addInterest(String interest_String, String interestMoney_String) {
        databaseReference2 = FirebaseDatabase.getInstance().getReference("UserData").child(UID);
        databaseReference4 = FirebaseDatabase.getInstance().getReference("InterestMoney");
        databaseReference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String interest_oneMonth = String.valueOf(snapshot.child("OneMonth").getValue());
                String interest_oneYear = String.valueOf(snapshot.child("OneYear").getValue());
                String interest_threeYears = String.valueOf(snapshot.child("FiveYears").getValue()); // Data store for three years

                Amount_double = Double.parseDouble(Amount);

                if (!interest_String.equals(" ") && Interest.equals("OneMonth")){
                    string1 =  String.valueOf(Integer.parseInt(interest_oneMonth) + Integer.parseInt(interest_String));
                }else if(!interest_String.equals(" ") && Interest.equals("OneYear")) {
                    string1 =  String.valueOf(Integer.parseInt(interest_oneYear) + Integer.parseInt(interest_String));
                }else if(!interest_String.equals(" ") && Interest.equals("ThreeYears")){
                    string1 =  String.valueOf(Integer.parseInt(interest_threeYears) + Integer.parseInt(interest_String));
                } else {
                    if (Interest.equals("OneMonth")){
                        string1 = interest_oneMonth;
                    }else if (Interest.equals("OneYear")){
                        string1 = interest_oneYear;
                    }else if (Interest.equals("ThreeYears")){
                        string1 = interest_threeYears;
                    }else {
                        string1 = interest_String;
                    }
                }

                if (!interestMoney_String.equals(" ") && Interest.equals("OneMonth")){
                    InterestMoney_double = (Amount_double * (Double.parseDouble(interest_oneMonth) / 100));
                    string2 =  String.valueOf(InterestMoney_double + Double.parseDouble(interestMoney_String));
                }else if (!interestMoney_String.equals(" ") && Interest.equals("OneYear")){
                    InterestMoney_double = (Amount_double * (Double.parseDouble(interest_oneYear) / 100));
                    string2 =  String.valueOf(InterestMoney_double + Double.parseDouble(interestMoney_String));
                }else if (!interestMoney_String.equals(" ") && Interest.equals("ThreeYears")){
                    InterestMoney_double = (Amount_double * (Double.parseDouble(interest_threeYears) / 100));
                    string2 =  String.valueOf(InterestMoney_double + Double.parseDouble(interestMoney_String));
                }
                else {
                    if (Interest.equals("OneMonth")){
                        InterestMoney_double = (Amount_double * (Double.parseDouble(interest_oneMonth) / 100));
                        string2 = String.valueOf(InterestMoney_double);
                    }else if (Interest.equals("OneYear")){
                        InterestMoney_double = (Amount_double * (Double.parseDouble(interest_oneYear) / 100));
                        string2 = String.valueOf(InterestMoney_double);
                    }else if (Interest.equals("ThreeYears")){
                        InterestMoney_double = (Amount_double * (Double.parseDouble(interest_threeYears) / 100));
                        string2 = String.valueOf(InterestMoney_double);
                    }else {
                        string2 = interestMoney_String;
                    }
                }

                databaseReference2.child("Interest").setValue(string1);
                databaseReference2.child("InterestMoney").setValue(string2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}