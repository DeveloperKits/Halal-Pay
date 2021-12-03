package com.samulit.halal_pay.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.samulit.halal_pay.Game.TicTacToe_Minimax_algo;
import com.samulit.halal_pay.databinding.CustomDialogEnterGameBinding;

public class EnterGameDialog {

    private final Context context;
    private final View view;
    private CustomDialogEnterGameBinding gameBinding;

    private long entry_fee = 5000;

    public EnterGameDialog(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @SuppressLint("SetTextI18n")
    public void createDialog(){
        gameBinding = CustomDialogEnterGameBinding.inflate(LayoutInflater.from(context));
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setView(view);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(false);

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        gameBinding.cancel.setOnClickListener(view1 -> alertDialog.dismiss());

        gameBinding.entryFee.setText("" + entry_fee);
        gameBinding.prizeMoney.setText("Prize Money: " + entry_fee*1.5);

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
            Intent intent = new Intent(context, TicTacToe_Minimax_algo.class);
            intent.putExtra("Entry Fee", entry_fee);
            context.startActivity(intent);
        });
    }
}
