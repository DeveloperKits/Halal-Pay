package com.samulit.halal_pay.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.samulit.halal_pay.HomeActivity;
import com.samulit.halal_pay.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {
    private MaterialCardView Donation, Business_Loan, Profile_card;
    private ImageView Profile_Image;
    private TextView UserName, UserBalance, UserPercentage, Percentage_Day, Invisible_Text;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    String UserID, userName, userImage, usersCurrentBalance, WeekMonthYear, Transfer_Type, bkash_mer, rocket_mer, nogod_mer;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Donation = view.findViewById(R.id.donation);
        Business_Loan = view.findViewById(R.id.business_loan);
        Profile_card = view.findViewById(R.id.profileCard);
        Profile_Image = view.findViewById(R.id.profileImage);
        UserName = view.findViewById(R.id.UserName);
        UserBalance = view.findViewById(R.id.UserBalance);
        UserPercentage = view.findViewById(R.id.UserPercentage);
        Percentage_Day = view.findViewById(R.id.percentage_day);
        Invisible_Text = view.findViewById(R.id.invisible);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            UserID = firebaseUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("UserData").child(firebaseUser.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        userName = snapshot.child("userName").getValue().toString();
                        userImage = snapshot.child("userImage").getValue().toString();
                        usersCurrentBalance = snapshot.child("usesCurrentBalance").getValue().toString();
                        WeekMonthYear = snapshot.child("WeekMonthYear").getValue().toString();

                        UserName.setText(userName);
                        UserBalance.setText(usersCurrentBalance + " BDT");

                        if (!userImage.equals(" ")){
                            Picasso.get().load(userImage).fit().centerInside().placeholder(R.drawable.loading_gif__).into(Profile_Image);
                        }else {
                            Picasso.get().load(R.drawable.prodile_pic2).fit().centerInside().into(Profile_Image);
                        }

                        if (WeekMonthYear.equals(" ")){
                            Percentage_Day.setVisibility(View.GONE);
                            UserPercentage.setVisibility(View.GONE);
                            Invisible_Text.setVisibility(View.VISIBLE);
                        }else {
                            Percentage_Day.setText(WeekMonthYear);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            Donation.setOnClickListener(view1 -> {
                donation();
            });
        }

        return view;
    }

    private void donation() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_box_deposit,null);

        final TextView merchantNumber = mView.findViewById(R.id.merchantNum);
        final EditText paymentID = mView.findViewById(R.id.paymentID);
        final EditText money = mView.findViewById(R.id.money);
        final EditText number = mView.findViewById(R.id.paymentNumber);
        Button btn_okay = (Button)mView.findViewById(R.id.done);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel);
        RadioGroup radioGroup = mView.findViewById(R.id.radioGroup);

        money.setHint("How many tk you donated");
        number.setHint("Your Bkash Number");
        Transfer_Type = "Bkash";

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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.bkash:
                        Transfer_Type = "Bkash";
                        if (bkash_mer.isEmpty()){
                            merchantNumber.setText("Sorry not available");
                        }else {
                            merchantNumber.setText("Merchant Number: " + bkash_mer);
                        }
                        number.setHint("Your Bkash Number");
                        break;
                    case R.id.rocket:
                        Transfer_Type = "Rocket";
                        if (rocket_mer.isEmpty()){
                            merchantNumber.setText("Merchant Number: Sorry not available");
                        }else {
                            merchantNumber.setText("Merchant Number: " + rocket_mer);
                        }
                        number.setHint("Your Rocket Number");
                        break;
                    case R.id.nogod:
                        Transfer_Type = "Nogod";
                        if (nogod_mer.isEmpty()){
                            merchantNumber.setText("Merchant Number: Sorry not available");
                        }else {
                            merchantNumber.setText("Merchant Number: " + nogod_mer);
                        }
                        number.setHint("Your Nogod Number");
                        break;

                }
            }
        });

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_cancel.setOnClickListener(v -> alertDialog.dismiss()); // OnClickListener replace with lambda
        btn_okay.setOnClickListener(v -> {

            String storePaymentID, StoreNumber, Money;
            storePaymentID = paymentID.getText().toString();
            StoreNumber = number.getText().toString();
            Money = money.getText().toString();

            if (storePaymentID.equals(" ") || storePaymentID == null){
                paymentID.setError("Enter Yor Transfer ID");
                paymentID.requestFocus();
            } else if (StoreNumber.length() != 11){
                number.setError("Check your number");
                number.requestFocus();
            }else if (Money.equals(" ") || Money == null){
                number.setError("Enter valid Amount");
                number.requestFocus();
            }else {

                Calendar calFordDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                String saveCurrentDate = currentDate.format(calFordDate.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                String saveCurrentTime = currentTime.format(calFordDate.getTime());

                Map reg = new HashMap();
                reg.put("userName", userName);
                reg.put("PaymentID", storePaymentID);
                reg.put("depositNumber", StoreNumber);
                reg.put("userUID", UserID);
                reg.put("Date", saveCurrentDate);
                reg.put("Time", saveCurrentTime);
                reg.put("status", "pending...");
                reg.put("DonationAmount", Money);
                reg.put("depositType", Transfer_Type);

                databaseReference = FirebaseDatabase.getInstance().getReference("DonationRequest");
                databaseReference.push().updateChildren(reg);


                Toast.makeText(getContext(), "Successfully done! You will receive updates within 24 hours.", Toast.LENGTH_LONG).show();

                alertDialog.dismiss();
            }

        }); // OnClickListener replace with lambda
        alertDialog.show();
    }

}