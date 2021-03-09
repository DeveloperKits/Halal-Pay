package com.samulit.halal_pay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.samulit.halal_pay.Fragment.HomeFragment;
import com.samulit.halal_pay.Fragment.ProfileFragment;
import com.samulit.halal_pay.Fragment.WalletFragment;


public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private WalletFragment walletFragment;
    private ProfileFragment profileFragment;
    private TextView PageName;
    private Button Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        PageName = findViewById(R.id.pageName);
        Back = findViewById(R.id.back);
        bottomNavigationView = findViewById(R.id.Bottom_nav);
        homeFragment = new HomeFragment();
        walletFragment = new WalletFragment();
        profileFragment = new ProfileFragment();

        setFragment(homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home :
                        setFragment(homeFragment);
                        PageName.setText("Home");
                        return true;

                    case R.id.wallet :
                        setFragment(walletFragment);
                        PageName.setText("Wallet");
                        return true;

                    case R.id.profile :
                        setFragment(profileFragment);
                        PageName.setText("Profile");
                        return true;

                    default:
                        return false;
                }
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.full_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this)
                .setMessage("Are you sure! You want to exit this app?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("no", null)
                .create();
        dialog.show();
    }
}