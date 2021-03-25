package com.samulit.halal_pay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.samulit.halal_pay.Fragment.HomeFragment;
import com.samulit.halal_pay.Fragment.ProfileFragment;
import com.samulit.halal_pay.Fragment.WalletFragment;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private WalletFragment walletFragment;
    private ProfileFragment profileFragment;
    private ProgressDialog progressDialog;
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

        progressDialog = new ProgressDialog(HomeActivity.this);

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

    public void Pop_Up_Menu(View view) {
        PopupMenu popupMenu = new PopupMenu(HomeActivity.this, findViewById(R.id.popUpMenu));

        popupMenu.getMenuInflater().inflate(R.menu.drawer_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //Toast.makeText(HomeActivity.this, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();

                if (menuItem.getTitle().equals("Signout")){
                    Sign_Out();
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void Sign_Out() {
        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(HomeActivity.this);
        builder1.setMessage("Are you sure you want to log out?");
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        progressDialog.show();
                        progressDialog.setMessage("Signing Out...");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                FirebaseAuth.getInstance().signOut();
                                progressDialog.dismiss();
                                HomeActivity.this.finishAffinity();
                                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            }
                        },1500);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        android.app.AlertDialog alert11 = builder1.create();
        alert11.show();

    }
}