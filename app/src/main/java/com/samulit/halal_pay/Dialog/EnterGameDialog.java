package com.samulit.halal_pay.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.samulit.halal_pay.Game.TicTacToe_Minimax_algo;
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
    private CustomDialogEnterGameBinding gameBinding;

    private String user;
    private DatabaseReference userRef;

    private long entry_fee = 5000;

    public EnterGameDialog(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @SuppressLint("SetTextI18n")
    public void createDialog(){
        gameBinding = CustomDialogEnterGameBinding.inflate(LayoutInflater.from(context));
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setView(gameBinding.getRoot());
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(false);

        // For background trnasparent
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        gameBinding.cancel.setOnClickListener(view1 -> alertDialog.dismiss());

        gameBinding.entryFee.setText("" + entry_fee);
        gameBinding.prizeMoney.setText("Prize Money: " + (int) entry_fee*1.5);

        gameBinding.plusFee.setOnClickListener(view1 -> {
            if (entry_fee <= 14000){
                entry_fee += 1000;

                gameBinding.entryFee.setText("" + entry_fee);
                gameBinding.prizeMoney.setText("Prize Money: " + entry_fee*1.5);
            }
        });

        gameBinding.minusFee.setOnClickListener(view1 -> {
            if (entry_fee >= 2000){
                entry_fee -= 1000;

                gameBinding.entryFee.setText("" + entry_fee);
                gameBinding.prizeMoney.setText("Prize Money: " + entry_fee*1.5);
            }
        });

        gameBinding.done.setOnClickListener(view1 -> {
            user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            userRef = FirebaseDatabase.getInstance().getReference("Game");

            Random random = new Random();

            String[] piece = {"X", "0"};
            String[] OpponentName = {"Toyota", "Mega Man", "Awesom-O", "Bishop", "Clank", "Daft Punk", "Roboto", "Robbie",
                    "Astro Boy", "Roomba", "Cindi", "Rosie", "Terminator", "Sojourner", "Rodriguez", "Wall-E"};

            int x = random.nextInt(2), y = random.nextInt(15);

            Map add = new HashMap();

            add.put("Entry Fee", entry_fee);
            add.put("Count", 1);
            add.put("isHard", "No");
            add.put("Piece Image", piece[x]);
            add.put("name", OpponentName[y]);

            userRef.child(user).updateChildren(add);

            Intent intent = new Intent(context, TicTacToe_Minimax_algo.class);
            intent.putExtra("Entry Fee", entry_fee);
            context.startActivity(intent);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }
}
