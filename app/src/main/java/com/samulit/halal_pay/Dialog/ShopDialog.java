package com.samulit.halal_pay.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.samulit.halal_pay.HomeActivity;
import com.samulit.halal_pay.R;
import com.samulit.halal_pay.databinding.CustomDialogShopBinding;

import java.text.DecimalFormat;

public class ShopDialog {
    private final Context context;
    private final View view;
    private final String currentBalance;
    private final long coin;

    private CustomDialogShopBinding shopBinding;
    private DatabaseReference databaseReference;

    private long coin_text, amount_text, coin1, amount1, coin2, amount2, coin3, amount3, coin4, amount4, coin5, amount5, coin6, amount6, count=0;
    private String coins;
    private String amount;

    public ShopDialog(Context context, View view, String currentBalance, long coin) {
        this.context = context;
        this.view = view;
        this.currentBalance = currentBalance;
        this.coin = coin;
    }

    @SuppressLint("SetTextI18n")
    public void createDialog(){
        shopBinding = CustomDialogShopBinding.inflate(LayoutInflater.from(context));
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setView(shopBinding.getRoot());
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(false);

        shopBinding.availableBalance.setText("Balance: " + new DecimalFormat("##.##").format(Double.parseDouble(currentBalance)));
        shopBinding.coinNote.setText(String.valueOf(coin));

        databaseReference = FirebaseDatabase.getInstance().getReference("Coin");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coin1 = Long.parseLong(snapshot.child("1st").child("coin").getValue(String.class));
                coin2 = Long.parseLong(snapshot.child("2nd").child("coin").getValue(String.class));
                coin3 = Long.parseLong(snapshot.child("3rd").child("coin").getValue(String.class));
                coin4 = Long.parseLong(snapshot.child("4rd").child("coin").getValue(String.class));
                coin5 = Long.parseLong(snapshot.child("5th").child("coin").getValue(String.class));
                coin6 = Long.parseLong(snapshot.child("6th").child("coin").getValue(String.class));

                amount1 = Long.parseLong(snapshot.child("1st").child("amount").getValue(String.class));
                amount2 = Long.parseLong(snapshot.child("2nd").child("amount").getValue(String.class));
                amount3 = Long.parseLong(snapshot.child("3rd").child("amount").getValue(String.class));
                amount4 = Long.parseLong(snapshot.child("4rd").child("amount").getValue(String.class));
                amount5 = Long.parseLong(snapshot.child("5th").child("amount").getValue(String.class));
                amount6 = Long.parseLong(snapshot.child("6th").child("amount").getValue(String.class));

                shopBinding.text1.setText(String.valueOf(coin1));
                shopBinding.text2.setText(String.valueOf(coin2));
                shopBinding.text3.setText(String.valueOf(coin3));
                shopBinding.text4.setText(String.valueOf(coin4));
                shopBinding.text5.setText(String.valueOf(coin5));
                shopBinding.text6.setText(String.valueOf(coin6));

                shopBinding.button1.setText("BDT " + amount1);
                shopBinding.button2.setText("BDT " + amount2);
                shopBinding.button3.setText("BDT " + amount3);
                shopBinding.button4.setText("BDT " + amount4);
                shopBinding.button5.setText("BDT " + amount5);
                shopBinding.button6.setText("BDT " + amount6);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        View.OnClickListener clickListener = view -> {

            switch(view.getId()){
                case R.id.button1:
                    coin_text = coin1;
                    amount_text = amount1;
                    break;

                case R.id.button2:
                    coin_text = coin2;
                    amount_text = amount2;
                    break;

                case R.id.button3:
                    coin_text = coin3;
                    amount_text = amount3;
                    break;

                case R.id.button4:
                    coin_text = coin4;
                    amount_text = amount4;
                    break;

                case R.id.button5:
                    coin_text = coin5;
                    amount_text = amount5;
                    break;

                default:
                    coin_text = coin6;
                    amount_text = amount6;
                    break;
            }

            if (amount_text <= (long) Math.round(Double.parseDouble(currentBalance))) {
                alertDialog.dismiss();
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Are you sure! Would you like to buy coins?")
                        .setPositiveButton("yes", (dialogInterface, i) -> {
                            storeInFirebase(coin_text, amount_text);
                            alertDialog.dismiss();
                            Toast.makeText(context, "Congratulations! Your coin collection is complete", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("no", null)
                        .create();
                dialog.show();
            }else {
                alertDialog.dismiss();
                Toast.makeText(context, "Sorry, you have selected more than the available balance.", Toast.LENGTH_SHORT).show();
            }
        };

        shopBinding.button1.setOnClickListener(clickListener);
        shopBinding.button2.setOnClickListener(clickListener);
        shopBinding.button3.setOnClickListener(clickListener);
        shopBinding.button4.setOnClickListener(clickListener);
        shopBinding.button5.setOnClickListener(clickListener);
        shopBinding.button6.setOnClickListener(clickListener);

        shopBinding.cancel.setOnClickListener(view1 -> alertDialog.dismiss());

        alertDialog.show();
    }

    private void storeInFirebase(long coin_text, long amount_text){
        databaseReference = FirebaseDatabase.getInstance().getReference("UserData").child(FirebaseAuth.getInstance().getUid());
        databaseReference.child("usesCurrentBalance").setValue(String.valueOf(Double.parseDouble(currentBalance) - (double) amount_text));
        databaseReference.child("UserCoin").setValue(coin + coin_text);
    }
}
