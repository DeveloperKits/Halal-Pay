package com.samulit.halal_pay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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


public class RegistrationActivity extends AppCompatActivity {

    private EditText nameText, emailText, passwordText, numberText;
    private TextView Sign_up;
    private MaterialCardView google_login;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    private String user_id, name, email, number, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        nameText = findViewById(R.id.name_text);
        emailText = findViewById(R.id.email_text);
        passwordText = findViewById(R.id.password_text);
        numberText = findViewById(R.id.number_text);
        Sign_up = findViewById(R.id.Sign_Up);
        google_login = findViewById(R.id.google_login);
        progressDialog = new ProgressDialog(RegistrationActivity.this);

        firebaseAuth = FirebaseAuth.getInstance();

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


        // When click Sign Up Text button
        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameText.getText().toString().trim();
                email =  emailText.getText().toString().trim();
                number = numberText.getText().toString().trim();
                password = passwordText.getText().toString().trim();


                // Give a message if user can't give one field empty
                if (name.isEmpty()){
                    nameText.setError("Enter a Name");
                    nameText.requestFocus();

                } else if (email.isEmpty()){
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

                } else if (number.isEmpty()){
                    numberText.setError("Enter a Phone Number");
                    numberText.requestFocus();

                } else if (number.length() != 11){
                    numberText.setError("Minimum length of a Number should be 11");
                    numberText.requestFocus();

                } else if (!Patterns.PHONE.matcher(number).matches()){
                    numberText.setError("Enter a valid Phone Number");
                    numberText.requestFocus();

                }else if (!(isNetworkAvaliable(RegistrationActivity.this))) {
                    Toast.makeText(RegistrationActivity.this, "Please Check Your Connection", Toast.LENGTH_SHORT).show();

                } else {

                    // For show loading when click the registration Button
                    progressDialog.setTitle("Creating Account");
                    progressDialog.setMessage("Please wait, we are creating your account");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    // End

                    // Create Account On Firebase
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            } else {

                                Sign_Up_With_email_And_Personal_Information();

                            }
                        }
                    });

                }
                // End

            }
        });
    }

    private void Sign_Up_With_email_And_Personal_Information() {

        String saveCurrentDate, saveCurrentTime;

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        user_id = firebaseAuth.getCurrentUser().getUid();

        name = nameText.getText().toString();
        email = emailText.getText().toString();
        number = numberText.getText().toString();
        password = passwordText.getText().toString();

        Map reg = new HashMap();

        reg.put("userName", name);
        reg.put("userEmail", email);
        reg.put("userPhone", number);
        reg.put("userImage", " ");
        reg.put("userUID", user_id);
        reg.put("userPassword", password);
        reg.put("userAge", " ");
        reg.put("userLocation", " ");
        reg.put("userTotalDepositBalance", "0");
        reg.put("TotalWithdraw", "0");
        reg.put("WeekMonthYear", " ");
        reg.put("Interest", " ");
        reg.put("InterestMoney", " ");
        reg.put("usesCurrentBalance", "0");
        reg.put("HowManyTimeDeposit", "0");
        reg.put("userMembershipTime", saveCurrentTime);
        reg.put("userMembershipDate", saveCurrentDate);

        databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
        databaseReference.child(user_id).updateChildren(reg);

        progressDialog.dismiss();
        Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
        finish();
    }


    // When click Login Button
    public void goneLoginActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
                progressDialog.setMessage("Wait Some moment...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("UserData").child(user.getUid().toString()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        progressDialog.dismiss();
                                        startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                    }else{
                                        Save_user_data_save_on_firebase(user.getDisplayName(), user.getEmail(), user.getPhoneNumber(), user.getUid(), String.valueOf(user.getPhotoUrl()));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            Toast.makeText(RegistrationActivity.this, "Sorry authentication failed.", Toast.LENGTH_SHORT).show();
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
        reg.put("userMembershipTime", saveCurrentTime);
        reg.put("userMembershipDate", saveCurrentDate);

        databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
        databaseReference.child(uid).updateChildren(reg);

        Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    // Check Internet Connection
    private boolean isNetworkAvaliable(RegistrationActivity registrationActivity) {
        ConnectivityManager cm = (ConnectivityManager) registrationActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}