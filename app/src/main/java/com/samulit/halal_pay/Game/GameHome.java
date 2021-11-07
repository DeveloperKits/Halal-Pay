package com.samulit.halal_pay.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.samulit.halal_pay.R;
import com.samulit.halal_pay.databinding.ActivityGameHomeBinding;

public class GameHome extends AppCompatActivity {

    private ActivityGameHomeBinding gameHomeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameHomeBinding = ActivityGameHomeBinding.inflate(getLayoutInflater());
        View view = gameHomeBinding.getRoot();
        setContentView(view);

        gameHomeBinding.coronaRunner.setOnClickListener(view1 -> startActivity(new Intent(this, Corona_Runner.class)));

        gameHomeBinding.ticTocToe.setOnClickListener(view1 -> startActivity(new Intent(this, TicTacToe_Minimax_algo.class)));

        gameHomeBinding.back.setOnClickListener(view1 -> onBackPressed());
    }
}