package com.samulit.halal_pay.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.samulit.halal_pay.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class WalletFragment extends Fragment {
    private MaterialCardView Deposit, Withdraw;
    private ImageView Profile_Image;
    private TextView UserName, Available_balance, TotalBalance, Withdraw_Balance, Week, Month, Year, InterestMoney,
            InterestTest, SevenDays, FifteenDays, OneYear, FiveYears, OneMonth, Hint;

    private DatabaseReference databaseReference, databaseReference2, databaseReference3;
    private FirebaseUser firebaseUser;

    private String Transfer_Type, withdraw_Type, UserID, userName, userImage, usersCurrentBalance, userEmail,
            usersTotalBalance, userWithdrawBalance ,WeekMonthYear, bkash_mer, rocket_mer, nogod_mer, InterestType,
            interest_money, SevenDays_St, OneMonth_st, FifteenDays_st, OneYear_st, FiveYears_st, currentUserBalance, interest;


    public WalletFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        Deposit = view.findViewById(R.id.deposit);
        Withdraw = view.findViewById(R.id.withdraw_card);
        Profile_Image = view.findViewById(R.id.profileImage);
        UserName = view.findViewById(R.id.UserName);
        Week = view.findViewById(R.id.week);
        Month = view.findViewById(R.id.month);
        Year = view.findViewById(R.id.year);
        TotalBalance = view.findViewById(R.id.Total_Balance);
        Available_balance = view.findViewById(R.id.available);
        Withdraw_Balance = view.findViewById(R.id.withdraw);
        InterestMoney = view.findViewById(R.id.InterestMoney);
        InterestTest = view.findViewById(R.id.InterestText);
        Hint = view.findViewById(R.id.hint);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            UserID = firebaseUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("UserData").child(firebaseUser.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        userEmail = snapshot.child("userEmail").getValue().toString();
                        userName = snapshot.child("userName").getValue().toString();
                        userImage = snapshot.child("userImage").getValue().toString();
                        usersCurrentBalance = snapshot.child("usesCurrentBalance").getValue().toString();
                        usersTotalBalance = snapshot.child("userTotalDepositBalance").getValue().toString();
                        userWithdrawBalance = snapshot.child("TotalWithdraw").getValue().toString();
                        WeekMonthYear = snapshot.child("WeekMonthYear").getValue().toString();
                        interest_money = snapshot.child("InterestMoney").getValue().toString();
                        interest = snapshot.child("Interest").getValue().toString();

                        UserName.setText(userName);
                        Available_balance.setText(usersCurrentBalance + " BDT");
                        TotalBalance.setText(usersTotalBalance + " BDT");
                        Withdraw_Balance.setText(userWithdrawBalance + " BDT");

                        if (!userImage.equals(" ")){
                            Picasso.get().load(userImage).fit().centerInside().placeholder(R.drawable.loading_gif__).into(Profile_Image);
                        }else {
                            Picasso.get().load(R.drawable.prodile_pic2).fit().centerInside().into(Profile_Image);
                        }

                        if (!interest_money.equals(" ")){
                            InterestMoney.setText(interest_money + " BDT");
                            InterestMoney.setVisibility(View.VISIBLE);
                            InterestTest.setVisibility(View.VISIBLE);
                        }else {
                            Hint.setVisibility(View.VISIBLE);
                            InterestMoney.setText( "0 BDT");
                            InterestMoney.setVisibility(View.VISIBLE);
                            InterestTest.setVisibility(View.VISIBLE);
                        }


                        if (!WeekMonthYear.equals(" ")){
                            if (WeekMonthYear.equals("Week")) {
                                Week.setBackground(getResources().getDrawable(R.drawable.background_text));
                            }else if (WeekMonthYear.equals("Month")){
                                Month.setBackground(getResources().getDrawable(R.drawable.background_text));
                            }else {
                                Year.setBackground(getResources().getDrawable(R.drawable.background_text));
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            Withdraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    withdraw();
                }
            });

            Deposit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deposit();
                }
            });

            databaseReference2 = FirebaseDatabase.getInstance().getReference("InterestMoney");
            databaseReference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        SevenDays_St = String.valueOf(snapshot.child("OneWeek").getValue());
                        FifteenDays_st = String.valueOf(snapshot.child("FifteenDays").getValue());
                        OneMonth_st = String.valueOf(snapshot.child("OneMonth").getValue());
                        OneYear_st = String.valueOf(snapshot.child("OneYear").getValue());
                        FiveYears_st = String.valueOf(snapshot.child("FiveYears").getValue());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return view;
    }


    @SuppressLint("NonConstantResourceId")
    private void withdraw() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_box,null);
        final EditText money = (EditText)mView.findViewById(R.id.money);
        final EditText number = (EditText)mView.findViewById(R.id.number);
        Button btn_okay = (Button)mView.findViewById(R.id.done);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel);
        RadioGroup radioGroup = mView.findViewById(R.id.radioGroup);

        withdraw_Type = "Bkash";

        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            switch(i) {
                case R.id.bkash:
                    withdraw_Type = "Bkash";
                    number.setHint("Enter your bkash number...");
                    break;
                case R.id.rocket:
                    withdraw_Type = "Rocket";
                    number.setHint("Enter your rocket number...");
                    break;
                case R.id.nogod:
                    withdraw_Type = "Nogod";
                    number.setHint("Enter your nogod number...");
                    break;

            }
        });

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btn_cancel.setOnClickListener(v -> alertDialog.dismiss());
        btn_okay.setOnClickListener(v -> {
            String storeMoney, StoreNumber;
            storeMoney = money.getText().toString();
            StoreNumber = number.getText().toString();

            if (storeMoney.equals(" ") || storeMoney.equals("")){
                money.setError("Enter how much money");
                money.requestFocus();
            }else if (Double.parseDouble(storeMoney) >= Double.parseDouble(usersCurrentBalance)){
                money.setError("Reduce the amount of your money a bit");
                money.requestFocus();
            }else if (Double.parseDouble(storeMoney) < 30 ){
                money.setError("There will be more than 30 inputs");
                money.requestFocus();
            } else if (StoreNumber.length() != 11){
                number.setError("Check your number");
                number.requestFocus();
            }else {

                Calendar calFordDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                String saveCurrentDate = currentDate.format(calFordDate.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                String saveCurrentTime = currentTime.format(calFordDate.getTime());

                Map reg = new HashMap();
                reg.put("userName", userName);
                reg.put("userEmail", userEmail);
                reg.put("withdrawMoney", storeMoney);
                reg.put("WithdrawPhone", StoreNumber);
                reg.put("userUID", UserID);
                reg.put("Date", saveCurrentDate);
                reg.put("Time", saveCurrentTime);
                reg.put("status", "pending...");
                reg.put("withdrawType", withdraw_Type);

                databaseReference = FirebaseDatabase.getInstance().getReference("WithdrawRequest");
                databaseReference.push().updateChildren(reg);

                Toast.makeText(getContext(), "Successfully done! You will receive updates within 24 hours.", Toast.LENGTH_LONG).show();

                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }



    // Edit somethings

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    private void deposit() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_box_deposit,null);

        final TextView merchantNumber = mView.findViewById(R.id.merchantNum);
        final EditText paymentID = mView.findViewById(R.id.paymentID);
        final EditText money = mView.findViewById(R.id.money);
        final EditText number = mView.findViewById(R.id.paymentNumber);
        final LinearLayout linearLayout1 = mView.findViewById(R.id.lNum);
        final LinearLayout linearLayout2 = mView.findViewById(R.id.lID);
        Button btn_okay = (Button)mView.findViewById(R.id.done);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel);
        RadioGroup radioGroup = mView.findViewById(R.id.radioGroup);
        RadioGroup radioGroup2 = mView.findViewById(R.id.radioGroup2);
        OneMonth = mView.findViewById(R.id.oneMonth);

        /*SevenDays = mView.findViewById(R.id.sevenDays);
        FifteenDays = mView.findViewById(R.id.fifteenDays);
        OneYear = mView.findViewById(R.id.oneYear);
        FiveYears = mView.findViewById(R.id.FiveYears);*/

        /*SevenDays.setText("1 Week   -------> " + SevenDays_St + "%");
        FifteenDays.setText("15 Days  -------> " + FifteenDays_st + "%");
        OneYear.setText("1 Year    -------> " + OneYear_st + "%");
        FiveYears.setText("5 Years  -------> " + FiveYears_st + "%");*/

        number.setHint("Your Bkash Number");
        OneMonth.setText("1 Month -------> " + OneMonth_st + "%");
        Transfer_Type = "Bkash";
        InterestType = "OneMonth";

        databaseReference = FirebaseDatabase.getInstance().getReference("MerchantNumber");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    bkash_mer = snapshot.child("Bkash").getValue().toString();
                    rocket_mer = snapshot.child("Rocket").getValue().toString();
                    nogod_mer = snapshot.child("Nogod").getValue().toString();

                    if (bkash_mer.isEmpty()){
                        merchantNumber.setText("Merchant Number: Sorry not available");
                    }else {
                        merchantNumber.setText("Merchant Number: " + bkash_mer);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            switch(i) {
                case R.id.bkash:
                    Transfer_Type = "Bkash";
                    linearLayout1.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    if (bkash_mer.isEmpty()){
                        merchantNumber.setText("Sorry not available");
                    }else {
                        merchantNumber.setText("Merchant Number: " + bkash_mer);
                    }
                    number.setHint("Your Bkash Number");
                    break;

                case R.id.rocket:
                    Transfer_Type = "Rocket";
                    linearLayout1.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    if (rocket_mer.isEmpty()){
                        merchantNumber.setText("Merchant Number: Sorry not available");
                    }else {
                        merchantNumber.setText("Merchant Number: " + rocket_mer);
                    }
                    number.setHint("Your Rocket Number");
                    break;

                case R.id.nogod:
                    Transfer_Type = "Nogod";
                    linearLayout1.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    if (nogod_mer.isEmpty()){
                        merchantNumber.setText("Merchant Number: Sorry not available");
                    }else {
                        merchantNumber.setText("Merchant Number: " + nogod_mer);
                    }
                    number.setHint("Your Nogod Number");
                    break;

                case R.id.wallet:
                    Transfer_Type = "Wallet Balance";
                    merchantNumber.setText("Your Wallet Current Balance : " + usersCurrentBalance);
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.GONE);
                    break;
            }
        });

        /*radioGroup2.setOnCheckedChangeListener((radioGroup1, i) -> {
            switch(i) {
                case R.id.sevenDays:
                    InterestType = "OneWeek";
                    break;
                case R.id.fifteenDays:
                    InterestType = "FifteenDays";
                    break;
                case R.id.oneMonth:
                    InterestType = "OneMonth";
                    break;
                case R.id.oneYear:
                    InterestType = "OneYear";
                    break;
                case R.id.FiveYears:
                    InterestType = "FiveYears";
                    break;
            }
        });*/

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btn_cancel.setOnClickListener(v -> alertDialog.dismiss()); // OnClickListener replace with lambda
        btn_okay.setOnClickListener(v -> {
            String storePaymentID, StoreNumber, Money;
            storePaymentID = paymentID.getText().toString();
            StoreNumber = number.getText().toString();
            Money = money.getText().toString();

            if (!Transfer_Type.equals("Wallet Balance")) {
                if (storePaymentID.equals(" ") || storePaymentID.equals("")) {
                    paymentID.setError("Enter Yor Transfer ID");
                    paymentID.requestFocus();
                } else if (StoreNumber.length() != 11) {
                    number.setError("Check your number");
                    number.requestFocus();
                } else if (Money.equals(" ") || Money.equals("")) {
                    number.setError("Enter valid Amount");
                    number.requestFocus();
                } else {

                    Calendar calFordDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                    String saveCurrentDate = currentDate.format(calFordDate.getTime());

                    SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                    String saveCurrentTime = currentTime.format(calFordDate.getTime());

                    Map reg = new HashMap();
                    reg.put("userName", userName);
                    reg.put("userEmail", userEmail);
                    reg.put("PaymentID", storePaymentID);
                    reg.put("depositNumber", StoreNumber);
                    reg.put("userUID", UserID);
                    reg.put("Date", saveCurrentDate);
                    reg.put("Time", saveCurrentTime);
                    reg.put("status", "pending...");
                    reg.put("DepositAmount", Money);
                    reg.put("depositType", Transfer_Type);
                    reg.put("InterestType", InterestType);

                    databaseReference = FirebaseDatabase.getInstance().getReference("DepositRequest");
                    databaseReference.push().updateChildren(reg);

                    Toast.makeText(getContext(), "Successfully done! You will receive updates within 24 hours.", Toast.LENGTH_LONG).show();

                    alertDialog.dismiss();
                }
            }else {

                if(Double.parseDouble(usersCurrentBalance) > Double.parseDouble(Money)){
                    currentUserBalance = String.valueOf(Double.parseDouble(usersCurrentBalance) - Double.parseDouble(Money));
                    String Interest_Money = String.valueOf(Double.parseDouble(Money) * (Double.parseDouble(OneMonth_st) / 100));

                    databaseReference3 = FirebaseDatabase.getInstance().getReference("UserData").child(firebaseUser.getUid());
                    databaseReference3.child("usesCurrentBalance").setValue(currentUserBalance);
                    databaseReference3.child("Interest").setValue(String.valueOf(Double.parseDouble(interest) + Double.parseDouble(OneMonth_st)));
                    databaseReference3.child("InterestMoney").setValue(String.valueOf(Double.parseDouble(interest_money) + Double.parseDouble(Interest_Money)));
                    databaseReference3.child("userTotalDepositBalance").setValue(String.valueOf(Double.parseDouble(usersTotalBalance) + Double.parseDouble(Money)));

                    Calendar calFordDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                    String saveCurrentDate = currentDate.format(calFordDate.getTime());

                    SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                    String saveCurrentTime = currentTime.format(calFordDate.getTime());

                    Map reg = new HashMap();
                    reg.put("userName", userName);
                    reg.put("userEmail", userEmail);
                    reg.put("PaymentID", "None");
                    reg.put("depositNumber", "None");
                    reg.put("userUID", UserID);
                    reg.put("Date", saveCurrentDate);
                    reg.put("Time", saveCurrentTime);
                    reg.put("status", "Successfully Done");
                    reg.put("DepositAmount", Money);
                    reg.put("depositType", Transfer_Type);
                    reg.put("InterestType", InterestType);

                    databaseReference = FirebaseDatabase.getInstance().getReference("DepositRequest");
                    databaseReference.push().updateChildren(reg);

                    Toast.makeText(getContext(), "Successfully done!", Toast.LENGTH_LONG).show();

                    alertDialog.dismiss();
                }

            }

        }); // OnClickListener replace with lambda

        alertDialog.show();
    }
}