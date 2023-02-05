package com.recogniseerror.CashNet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private EditText emailText, passwordText;
    private TextView Login;
    private MaterialCardView google_login;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.email_text);
        passwordText = findViewById(R.id.password_text);
        Login = findViewById(R.id.login);
        google_login = findViewById(R.id.google_login);
        progressDialog = new ProgressDialog(LoginActivity.this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();


        // When Click Login Button
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailText.getText().toString();
                password = passwordText.getText().toString();

                if (email.isEmpty()){
                    emailText.setError("Enter an Email");
                    emailText.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailText.setError("Enter a valid Email Address");
                    emailText.requestFocus();

                } else if (password.isEmpty()){
                    passwordText.setError("Enter a Password");
                    passwordText.requestFocus();

                } else if (password.length() < 6){
                    passwordText.setError("Minimum length of a Password should be 6");
                    passwordText.requestFocus();

                }else if (!(isNetworkAvaliable(LoginActivity.this))) {
                    Toast.makeText(LoginActivity.this, "Check Your Connection", Toast.LENGTH_SHORT).show();

                } else {

                    progressDialog.show();
                    progressDialog.setMessage("Logging in...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();

                            }

                        }
                    });
                }
            }
        });

        // Login with Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_key))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }


    // When click Create an account
    public void goneRegistrationActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);
        finish();
    }


    // After Click Google Login Button It's call
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                progressDialog.show();
                progressDialog.setMessage("Logging in...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Please check your internet or try again!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            databaseReference.child("UserData").child(user.getUid().toString()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        progressDialog.dismiss();
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    }else{
                                        Save_user_data_save_on_firebase(user.getDisplayName(), user.getEmail(), user.getPhoneNumber(), user.getUid(), String.valueOf(user.getPhotoUrl()));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            Toast.makeText(LoginActivity.this, "Sorry authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void Save_user_data_save_on_firebase(String displayName, String email, String phoneNumber, String uid, String Image_User) {
        if (phoneNumber == null){
            phoneNumber = " ";
        }
        if (Image_User == null) {
            Image_User = " ";
        }

        String saveCurrentDate, saveCurrentTime;

        Calendar calFordDate = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        Map reg = new HashMap();

        reg.put("userName", displayName);
        reg.put("userEmail", email);
        reg.put("userPhone", phoneNumber);
        reg.put("userImage", Image_User);
        reg.put("userUID", uid);
        reg.put("userAge", " ");
        reg.put("userPassword", " ");
        reg.put("userLocation", " ");
        reg.put("userTotalDepositBalance", "0");
        reg.put("TotalWithdraw", "0");
        reg.put("WeekMonthYear", " ");
        reg.put("Interest", " ");
        reg.put("InterestMoney", " ");
        reg.put("usesCurrentBalance", "0");
        reg.put("HowManyTimeDeposit", "0");
        reg.put("UserCoin", 1000);
        reg.put("userMembershipTime", saveCurrentTime);
        reg.put("userMembershipDate", saveCurrentDate);

        databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
        databaseReference.child(uid).updateChildren(reg);

        progressDialog.dismiss();

        Toast.makeText(LoginActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }

    // Check Internet Connection
    private boolean isNetworkAvaliable(LoginActivity loginActivity) {
        ConnectivityManager cm = (ConnectivityManager) loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    // When Click Forgot Password Button
    public void Forgot_password(View view) {

        final EditText input = new EditText(LoginActivity.this);
        AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Reset Password")
                .setMessage("\nEnter your registered email")
                .setView(input)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String resetEmail = input.getText().toString();

                        if(resetEmail.isEmpty()){
                            Toast.makeText(LoginActivity.this, "Empty Email!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            firebaseAuth.sendPasswordResetEmail(resetEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Password Reset Failed", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Please Check Your Email & Reset Password", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}