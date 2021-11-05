package com.samulit.halal_pay.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.samulit.halal_pay.R;
import com.samulit.halal_pay.databinding.ActivityCoronaRunnerBinding;

public class Corona_Runner extends AppCompatActivity {

    private ActivityCoronaRunnerBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoronaRunnerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.back.setOnClickListener(view1 -> onBackPressed());

        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.loadUrl("file:///android_asset/index.html");
    }
}