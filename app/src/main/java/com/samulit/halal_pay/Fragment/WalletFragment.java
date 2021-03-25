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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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


public class WalletFragment extends Fragment {
    private MaterialCardView Deposit, Withdraw;
    private ImageView Profile_Image;
    private TextView UserName, Available_balance, TotalBalance, Withdraw_Balance, Week, Month, Year;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private String Transfer_Type, UserID, userName, userImage, usersCurrentBalance, usersTotalBalance, userWithdrawBalance ,WeekMonthYear;

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

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            UserID = firebaseUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("UserData").child(firebaseUser.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        userName = snapshot.child("userName").getValue().toString();
                        userImage = snapshot.child("userImage").getValue().toString();
                        usersCurrentBalance = snapshot.child("usesCurrentBalance").getValue().toString();
                        usersTotalBalance = snapshot.child("userTotalDepositBalance").getValue().toString();
                        userWithdrawBalance = snapshot.child("TotalWithdraw").getValue().toString();
                        WeekMonthYear = snapshot.child("WeekMonthYear").getValue().toString();

                        UserName.setText(userName);
                        Available_balance.setText(usersCurrentBalance + " BDT");
                        TotalBalance.setText(usersTotalBalance + " BDT");
                        Withdraw_Balance.setText(userWithdrawBalance + " BDT");

                        if (!userImage.equals(" ")){
                            Picasso.get().load(userImage).fit().centerInside().placeholder(R.drawable.loading_gif__).into(Profile_Image);
                        }else {
                            Picasso.get().load(R.drawable.prodile_pic2).fit().centerInside().into(Profile_Image);
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
        }

        return view;
    }


    private void deposit() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_box_deposit,null);

        final TextView merchantNumber = mView.findViewById(R.id.merchantNum);
        final EditText paymentID = mView.findViewById(R.id.paymentID);
        final EditText number = mView.findViewById(R.id.paymentNumber);
        Button btn_okay = (Button)mView.findViewById(R.id.done);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel);
        RadioGroup radioGroup = mView.findViewById(R.id.radioGroup);
        merchantNumber.setText("Merchant Number: 01857959953");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i)
                {
                    case R.id.bkash:
                        Transfer_Type = "Bkash";
                        merchantNumber.setText("Merchant Number: 01857959953");
                        break;
                    case R.id.rocket:
                        Transfer_Type = "Rocket";
                        merchantNumber.setText("Merchant Number: 01814976752");
                        break;
                    case R.id.nogod:
                        Transfer_Type = "Nogod";
                        merchantNumber.setText("Merchant Number: 018238433756");
                        break;

                }
            }
        });

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void withdraw() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_box,null);
        final EditText money = (EditText)mView.findViewById(R.id.money);
        final EditText number = (EditText)mView.findViewById(R.id.number);
        Button btn_okay = (Button)mView.findViewById(R.id.done);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}