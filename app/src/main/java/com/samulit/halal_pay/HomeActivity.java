package com.samulit.halal_pay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.samulit.halal_pay.Fragment.HomeFragment;
import com.samulit.halal_pay.Fragment.ProfileFragment;
import com.samulit.halal_pay.Fragment.WalletFragment;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements LifecycleOwner{
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private WalletFragment walletFragment;
    private ProfileFragment profileFragment;
    private ProgressDialog progressDialog;
    private TextView PageName;
    private Button Back;
    private DatabaseReference update, databaseReference, databaseReference2;
    FirebaseUser user;

    private String UserID, TotalAmount, Interest_String, InterestMoney_String, Total, string1, string2, amount;
    private double TotalAmount_double, Amount_double, InterestMoney_double;

    private boolean isConnectWithInternet = false;

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
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

        user = FirebaseAuth.getInstance().getCurrentUser();
        UserID = user.getUid();

        databaseReference2 = FirebaseDatabase.getInstance().getReference("UserData");

        setFragment(homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
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
        });

        Back.setOnClickListener(view -> onBackPressed());


        // Check Deposit Date & And Add Money There Wallet
        checkDeposit();

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
                .setPositiveButton("yes", (dialogInterface, i) -> {
                    FirebaseAuth.getInstance().signOut();
                    finishAffinity();
                })
                .setNegativeButton("no", null)
                .create();
        dialog.show();
    }

    public void Pop_Up_Menu(View view) {
        PopupMenu popupMenu = new PopupMenu(HomeActivity.this, findViewById(R.id.popUpMenu));

        popupMenu.getMenuInflater().inflate(R.menu.drawer_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {

            if (menuItem.getTitle().equals("Signout")){
                Sign_Out();
            }else if (menuItem.getTitle().equals("About Us")){
                Intent intent = new Intent(HomeActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }else if (menuItem.getTitle().equals("Deposit History")){
                Intent intent = new Intent(HomeActivity.this, DepositHistoryActivity.class);
                startActivity(intent);
            }
            return true;

        });
        popupMenu.show();
    }

    private void Sign_Out() {
        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(HomeActivity.this);
        builder1.setMessage("Are you sure you want to log out?");
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    dialog.cancel();
                    progressDialog.show();
                    progressDialog.setMessage("Signing Out...");

                    new Handler().postDelayed(() -> {
                        FirebaseAuth.getInstance().signOut();
                        progressDialog.dismiss();
                        HomeActivity.this.finishAffinity();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    },1500);
                });

        builder1.setNegativeButton("No", (dialog, id) -> dialog.cancel());

        android.app.AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAutoUpdate();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null){
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        new Handler().postDelayed(() -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        },2*70*1000);
    }


    private void checkAutoUpdate() {
        //Check Auto Update
        update = FirebaseDatabase.getInstance().getReference("CheckUpdate");
        update.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    String version = dataSnapshot.child("version").getValue().toString();
                    String url = dataSnapshot.child("url").getValue(String.class);

                    String VersionName = BuildConfig.VERSION_NAME;

                    if (!version.equals(VersionName)) {

                        if (check_again_Internet_connection(HomeActivity.this)) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            builder.setTitle("New Version Available");
                            builder.setIcon(R.drawable.halalpay_logo);
                            builder.setCancelable(false);
                            builder.setMessage("Update Halal Pay For Better Experience")
                                    .setPositiveButton("UPDATE", (dialog, which) -> {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(url));
                                        startActivity(intent);
                                        finish();
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        }else {
                            Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean check_again_Internet_connection(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        isConnectWithInternet = netInfo != null && netInfo.isConnectedOrConnecting();
        return isConnectWithInternet;
    }


    private void checkDeposit(){
        databaseReference = FirebaseDatabase.getInstance().getReference("DepositRequest");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        String key = postSnapshot.getKey();

                        String ID = postSnapshot.child("userUID").getValue().toString();
                        String status = postSnapshot.child("status").getValue().toString();

                        if (UserID.equals(ID) && status.equals("Successfully Done")) {
                            String Date = postSnapshot.child("Date").getValue().toString();
                            List<String> list = Arrays.asList(Date.split("-"));
                            int index = checkIndex(list.get(1));

                            amount = String.valueOf(postSnapshot.child("DepositAmount").getValue());

                            // Current Date
                            Calendar calFordDate = Calendar.getInstance();
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                            String CurrentDate = currentDate.format(calFordDate.getTime());
                            List<String> list2 = Arrays.asList(CurrentDate.split("-"));
                            int index2 = checkIndex(list2.get(1));

                            // Check month == 1 && date > 0 && Year == 0
                            if (index2-index == 1 && (Integer.parseInt(list2.get(0))-Integer.parseInt(list.get(0)) > 0) &&
                                    (Integer.parseInt(list2.get(2))-Integer.parseInt(list.get(2)) == 0)){

                                AddedUserInterest(key, amount);
                                Toast.makeText(HomeActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                            }else if ((Integer.parseInt(list2.get(2))-Integer.parseInt(list.get(2)) > 0) || index2-index > 1){
                                AddedUserInterest(key, amount);
                                Toast.makeText(HomeActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(HomeActivity.this, "Not added", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(HomeActivity.this, "Not added!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void AddedUserInterest(String key, String Amount) {
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("InterestMoney").child("OneMonth");
        databaseReference = FirebaseDatabase.getInstance().getReference("DepositRequest").child(key);

        databaseReference2.child(UserID).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    TotalAmount = String.valueOf(snapshot.child("usesCurrentBalance").getValue());
                    Interest_String = String.valueOf(snapshot.child("Interest").getValue());
                    InterestMoney_String = String.valueOf(snapshot.child("InterestMoney").getValue());

                    TotalAmount_double = Double.parseDouble(TotalAmount);
                    Amount_double = Double.parseDouble(Amount);

                    databaseReference3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String interest_st = String.valueOf(snapshot.getValue());

                            Total = String.valueOf((Amount_double * (Double.parseDouble(interest_st) / 100)) + TotalAmount_double);
                            InterestMoney_double = (Amount_double * (Double.parseDouble(interest_st) / 100));

                            if (!Interest_String.equals(" ") && Integer.parseInt(Interest_String) <= Integer.parseInt(interest_st)){
                                string1 =  String.valueOf(Integer.parseInt(interest_st) - Integer.parseInt(Interest_String));
                            }else if (!Interest_String.equals(" ") && Integer.parseInt(Interest_String) >= Integer.parseInt(interest_st)){
                                string1 = String.valueOf(Integer.parseInt(Interest_String) - Integer.parseInt(interest_st));;
                            }else {
                                string1 = " ";
                            }

                            if (!InterestMoney_String.equals(" ") && InterestMoney_double >= Double.parseDouble(InterestMoney_String)){
                                string2 =  String.valueOf(InterestMoney_double - Double.parseDouble(InterestMoney_String));
                            }else if (!InterestMoney_String.equals(" ") && InterestMoney_double <= Double.parseDouble(InterestMoney_String)){
                                string2 =  String.valueOf(Double.parseDouble(InterestMoney_String) - InterestMoney_double);
                            }else {
                                string2 = " ";
                            }

                            databaseReference2.child(UserID).child("Interest").setValue(string1);
                            databaseReference2.child(UserID).child("InterestMoney").setValue(string2);
                            databaseReference2.child(UserID).child("usesCurrentBalance").setValue(Total);
                            databaseReference.child("status").setValue("Added Interest");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Toast.makeText(HomeActivity.this, "Successfully Added Money!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private int checkIndex(String s) {
        String [] strings = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int index = Arrays.asList(strings).indexOf(s);

        return index+1;
    }


}
