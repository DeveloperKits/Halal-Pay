package com.samulit.halal_pay.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.samulit.halal_pay.Game.GameHome;
import com.samulit.halal_pay.Game.TicTacToe_Minimax_algo;
import com.samulit.halal_pay.Game.TicTocToe_Easy_Algo;
import com.samulit.halal_pay.databinding.CustomDialogEnterGameBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class EnterGameDialog {

    private final Context context;
    private final View view;
    private final long fee;
    private final double sellCost;
    private final String check, currentBalance;
    private CustomDialogEnterGameBinding gameBinding;

    private String user;
    private DatabaseReference userRef, sellRef;
    private double result;

    private int entry_fee = 2000, Entry_fee2 = 1000;

    public EnterGameDialog(Context context, View view, long fee, double sellCost, String check, String currentBalance) {
        this.context = context;
        this.view = view;
        this.fee = fee;
        this.sellCost = sellCost;
        this.check = check;
        this.currentBalance = currentBalance;
    }

    @SuppressLint("SetTextI18n")
    public void createDialog(){
        gameBinding = CustomDialogEnterGameBinding.inflate(LayoutInflater.from(context));
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setView(gameBinding.getRoot());
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(false);

        gameBinding.cancel.setOnClickListener(view1 -> alertDialog.dismiss());

        if (!check.equals("Yes")) {
            gameBinding.entryFee.setText("" + entry_fee);
            gameBinding.prizeMoney.setText("Prize Money: " + (int) entry_fee * 1.5);
        }else {
            gameBinding.entryFee.setText("" + Entry_fee2);
            gameBinding.prizeMoney.setText("Add Money: " + sellCost);

            gameBinding.play.setVisibility(View.GONE);
            gameBinding.rule.setText("1000 coin = "+sellCost);
            gameBinding.tvEntryFee.setText("Select Sell Coin:");

            result = sellCost;
        }

        gameBinding.plusFee.setOnClickListener(view1 -> {
            if (!check.equals("Yes")) {
                if (entry_fee <= 14000) {
                    entry_fee += 1000;

                    gameBinding.entryFee.setText("" + entry_fee);
                    gameBinding.prizeMoney.setText("Prize Money: " + entry_fee * 1.5);
                }
            }else {
                Entry_fee2 += 1000;

                gameBinding.entryFee.setText("" + Entry_fee2);
                gameBinding.prizeMoney.setText("Add Money: " + ((Entry_fee2 * sellCost)/1000));
                result = ((Entry_fee2 * sellCost)/1000);
            }
        });

        gameBinding.minusFee.setOnClickListener(view1 -> {
            if (!check.equals("Yes")) {
                if (entry_fee >= 2000) {
                    entry_fee -= 1000;

                    gameBinding.entryFee.setText("" + entry_fee);
                    gameBinding.prizeMoney.setText("Prize Money: " + entry_fee * 1.5);
                }
            }else {
                if (Entry_fee2 >= 2000) {
                    Entry_fee2 -= 1000;

                    gameBinding.entryFee.setText("" + Entry_fee2);
                    gameBinding.prizeMoney.setText("Add Money: " + ((Entry_fee2 * sellCost) / 1000));
                    result = ((Entry_fee2 * sellCost) / 1000);
                }
            }
        });

        gameBinding.done.setOnClickListener(view1 -> {
            if (!check.equals("Yes")) {
                if (((long) entry_fee) <= fee) {
                    user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    userRef = FirebaseDatabase.getInstance().getReference("Game");

                    Random random = new Random();

                    String[] piece = {"X", "0"};
                    String[] OpponentName = {"Toyota", "Mega Man", "Awesom-O", "Bishop", "Clank", "Daft Punk", "Roboto", "Robbie",
                            "Astro Boy", "Roomba", "Cindi", "Rosie", "Terminator", "Sojourner", "Rodriguez", "Wall-E"};

                    int x = random.nextInt(2), y = random.nextInt(16);
                    String isHard;

                    if (x == 0) {
                        isHard = "Yes";
                    } else {
                        isHard = "No";
                    }

                    Map add = new HashMap();

                    add.put("Entry Fee", entry_fee);
                    add.put("Count", 1);
                    add.put("isHard", isHard);
                    add.put("Piece Image", piece[x]);
                    add.put("name", OpponentName[y]);
                    add.put("you win", 0);
                    add.put("computer win", 0);

                    userRef.child(user).updateChildren(add);
                    Intent intent;

                    if (x == 0) {
                        intent = new Intent(context, TicTacToe_Minimax_algo.class);
                    } else {
                        intent = new Intent(context, TicTocToe_Easy_Algo.class);
                    }
                    context.startActivity(intent);

                    alertDialog.dismiss();
                } else {
                    alertDialog.dismiss();
                    Toast.makeText(context, "Sorry, you have selected more than the available balance.", Toast.LENGTH_SHORT).show();
                }

            }else {
                if (((long) Entry_fee2) <= fee) {
                    user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    userRef = FirebaseDatabase.getInstance().getReference("UserData");
                    userRef.child(user).child("usesCurrentBalance").setValue(String.valueOf(Double.parseDouble(currentBalance)+result));
                    userRef.child(user).child("UserCoin").setValue(fee - Entry_fee2);

                    sellRef = FirebaseDatabase.getInstance().getReference("SellCoinHistory");
                    Map add = new HashMap();

                    add.put("Entry Fee", Entry_fee2);
                    add.put("Money", result);
                    add.put("UID", user);
                    sellRef.push().updateChildren(add);

                    alertDialog.dismiss();
                }else {
                    alertDialog.dismiss();
                    Toast.makeText(context, "Sorry, you have selected more than the available balance.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        alertDialog.show();
    }
}
