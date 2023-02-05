package com.recogniseerror.CashNet.Game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.recogniseerror.CashNet.Dialog.EnterGameDialog;
import com.recogniseerror.CashNet.Dialog.ShopDialog;
import com.recogniseerror.CashNet.R;
import com.recogniseerror.CashNet.databinding.ActivityGameHomeBinding;

public class GameHome extends AppCompatActivity {

    private ActivityGameHomeBinding gameHomeBinding;

    private String UserID, usersCurrentBalance;
    private long UserCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameHomeBinding = ActivityGameHomeBinding.inflate(getLayoutInflater());
        View view = gameHomeBinding.getRoot();
        setContentView(view);

        UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
        databaseReference.child(UserID).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserCoin = (long) snapshot.child("UserCoin").getValue();
                    gameHomeBinding.coinNote.setText(String.valueOf(UserCoin));
                    usersCurrentBalance = snapshot.child("usesCurrentBalance").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gameHomeBinding.coronaRunner.setOnClickListener(view1 -> startActivity(new Intent(this, Corona_Runner.class)));

        gameHomeBinding.ticTocToe.setOnClickListener(view1 -> {
            @SuppressLint("InflateParams") View mView = getLayoutInflater().inflate(R.layout.custom_dialog_enter_game,null);
            EnterGameDialog gameDialog = new EnterGameDialog(this, mView, UserCoin, 1, " ", " ");
            gameDialog.createDialog();
        });

        gameHomeBinding.back.setOnClickListener(view1 -> onBackPressed());

        gameHomeBinding.addCoin.setOnClickListener(view1 -> {
            @SuppressLint("InflateParams") View mView = getLayoutInflater().inflate(R.layout.custom_dialog_shop,null);
            @SuppressLint("InflateParams") View views = getLayoutInflater().inflate(R.layout.custom_dialog_enter_game,null);
            ShopDialog shopDialog = new ShopDialog(this, mView, usersCurrentBalance, UserCoin, views);
            shopDialog.createDialog();
        });
    }
}