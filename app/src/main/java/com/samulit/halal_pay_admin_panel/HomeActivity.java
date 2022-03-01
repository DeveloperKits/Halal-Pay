package com.samulit.halal_pay_admin_panel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private TextView PageName;
    private Button Back;
    private DatabaseReference databaseReference, databaseReference2;
    private Uri resultUri, contentURI;
    StorageReference storageReference;
    StorageTask storageTask;
    StorageReference fileReference;

    private String imageUri;
    private static final int PICK_FROM_GALLERY = 1;
    private int check;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseApp.initializeApp(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        PageName = findViewById(R.id.pageName);
        Back = findViewById(R.id.back);

        storageReference = FirebaseStorage.getInstance().getReference("ImageHome");

        progressDialog = new ProgressDialog(HomeActivity.this);

        Back.setOnClickListener(view -> onBackPressed());

        FirebaseAuth.getInstance().signInAnonymously();

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


    // Deposit .....................................................................................
    public void Deposit_List(View view) {
        Create_helper_dialog2();
    }

    private void Create_helper_dialog2() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        View view = getLayoutInflater().inflate(R.layout.helper_dialog2,null);

        Button successful = (Button)view.findViewById(R.id.button);
        Button pending = (Button)view.findViewById(R.id.button2);
        Button cancel = (Button)view.findViewById(R.id.button3);

        alert.setView(view);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        successful.setOnClickListener(view1 -> {
            create_intent("Successfully Done");
            alertDialog.dismiss();
        });

        pending.setOnClickListener(view1 -> {
            create_intent("pending...");
            alertDialog.dismiss();
        });

        cancel.setOnClickListener(view1 -> {
            create_intent("Cancel!");
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void create_intent(String s) {
        Intent intent = new Intent(HomeActivity.this, DepositList.class);
        intent.putExtra("string", s);
        startActivity(intent);
    }
    // End Deposit .................................................................................


    public void Donation_List(View view) {
        Intent intent = new Intent(HomeActivity.this, DonationList.class);
        startActivity(intent);
    }

    public void Withdraw_List(View view) {
        Create_helper_dialog(2);
    }

    public void Business_Loan_List(View view) {
        Create_helper_dialog(3);
    }

    private void Create_helper_dialog(int i) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        View view = getLayoutInflater().inflate(R.layout.helper_dialog,null);

        Button successful = (Button)view.findViewById(R.id.button);
        Button pending = (Button)view.findViewById(R.id.button2);

        alert.setView(view);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        successful.setOnClickListener(view1 -> {
            String string;
            if(i == 1){
                string = "Successfully Done";
            }else {
                string = "done...";
            }

            create_intent(string, i);
            alertDialog.dismiss();
        });

        pending.setOnClickListener(view1 -> {
            String string;
            if(i == 1){
                string = " ";
            }else {
                string = "pending...";
            }

            create_intent(string, i);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void create_intent(String string, int i) {
        Intent intent;

        if (i==1){
            intent = new Intent(HomeActivity.this, DepositList.class);
            intent.putExtra("string", string);
            startActivity(intent);
        }else if(i==2){
            intent = new Intent(HomeActivity.this, WithdrawList.class);
            intent.putExtra("string", string);
            startActivity(intent);
        }else {
            intent = new Intent(HomeActivity.this, BusinessLoanList.class);
            intent.putExtra("string", string);
            startActivity(intent);
        }
    }


    public void Merchant_Number(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_box_edit_profile,null);

        final EditText Bkash = mView.findViewById(R.id.email);
        final EditText Rocket = mView.findViewById(R.id.name);
        final EditText Nogod = mView.findViewById(R.id.age);
        Button btn_okay = (Button)mView.findViewById(R.id.done);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel);

        databaseReference = FirebaseDatabase.getInstance().getReference("MerchantNumber");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Bkash.setText(snapshot.child("Bkash").getValue().toString());
                    Rocket.setText(snapshot.child("Rocket").getValue().toString());
                    Nogod.setText(snapshot.child("Nogod").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_cancel.setOnClickListener(v -> alertDialog.dismiss()); // OnClickListener replace with lambda
        btn_okay.setOnClickListener(v -> {

            databaseReference = FirebaseDatabase.getInstance().getReference("MerchantNumber");
            Map mer = new HashMap();

            if (!Bkash.getText().toString().equals(" ")){
                mer.put("Bkash", Bkash.getText().toString());
            }
            if (!Rocket.getText().toString().equals(" ")){
                mer.put("Rocket", Rocket.getText().toString());
            }
            if (!Nogod.getText().toString().equals(" ")){
                mer.put("Nogod", Nogod.getText().toString());
            }

            databaseReference.updateChildren(mer);
            Toast.makeText(getApplicationContext(), "Successfully Done!", Toast.LENGTH_SHORT).show();

            alertDialog.dismiss();

        }); // OnClickListener replace with lambda
        alertDialog.show();
    }

    @SuppressLint("SetTextI18n")
    public void Set_User_Type_Interest(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_box_edit_profile,null);

        final EditText threeYears = mView.findViewById(R.id.email);
        final EditText oneYear = mView.findViewById(R.id.name);
        final EditText oneMonth = mView.findViewById(R.id.age);
        final EditText fifteenDays = mView.findViewById(R.id.fifteenDays);
        final EditText oneWeek = mView.findViewById(R.id.week);
        final TextView enter1 = mView.findViewById(R.id.enter1);
        final TextView enter2 = mView.findViewById(R.id.enter2);
        final TextView enter3 = mView.findViewById(R.id.enter3);
        final LinearLayout ll1 = mView.findViewById(R.id.ll1);
        final LinearLayout ll2 = mView.findViewById(R.id.ll2);
        final LinearLayout ll3 = mView.findViewById(R.id.ll3);
        Button btn_okay = (Button)mView.findViewById(R.id.done);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel);

        enter1.setText("3 Years:    ");
        enter2.setText("1 Year :     ");
        enter3.setText("1 Month:   ");
        threeYears.setHint("Enter Interest...");
        oneYear.setHint("Enter Interest...");
        oneMonth.setHint("Enter Interest...");

        databaseReference = FirebaseDatabase.getInstance().getReference("InterestMoney");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    threeYears.setText(snapshot.child("ThreeYears").getValue().toString());
                    oneYear.setText(snapshot.child("OneYear").getValue().toString());
                    oneMonth.setText(snapshot.child("OneMonth").getValue().toString());
                    fifteenDays.setText(snapshot.child("FifteenDays").getValue().toString());
                    oneWeek.setText(snapshot.child("OneWeek").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_cancel.setOnClickListener(v -> alertDialog.dismiss()); // OnClickListener replace with lambda
        btn_okay.setOnClickListener(v -> {

            databaseReference = FirebaseDatabase.getInstance().getReference("InterestMoney");
            Map mer = new HashMap();

            if (!threeYears.getText().toString().equals(" ")){
                mer.put("ThreeYears", threeYears.getText().toString());
            }
            if (!oneYear.getText().toString().equals(" ")){
                mer.put("OneYear", oneYear.getText().toString());
            }
            if (!oneMonth.getText().toString().equals(" ")){
                mer.put("OneMonth", oneMonth.getText().toString());
            }
            /*if (!fifteenDays.getText().toString().equals(" ")){
                mer.put("FifteenDays", fifteenDays.getText().toString());
            }
            if (!oneWeek.getText().toString().equals(" ")){
                mer.put("OneWeek", oneWeek.getText().toString());
            }*/

            databaseReference.updateChildren(mer);
            Toast.makeText(getApplicationContext(), "Successfully Done!", Toast.LENGTH_SHORT).show();

            alertDialog.dismiss();

        }); // OnClickListener replace with lambda
        alertDialog.show();
    }

    public void Set_Help_Number(View view) {
        final EditText input = new EditText(this);
        input.setHint("Enter Mobile Number...");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        databaseReference = FirebaseDatabase.getInstance().getReference("HelpNumber");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Set Help Number");
        alert.setView(input);
        alert.setCancelable(false);

        alert.setPositiveButton("Done", (dialogInterface, i) -> {
            String number = input.getText().toString();

            databaseReference.child("number").setValue(number);

            Toast.makeText(this, "Successfully done!", Toast.LENGTH_LONG).show();

        });

        alert.setNegativeButton("Cancel", (dialogInterface, i) -> {
            // Nothing
        });

        alert.show();
    }

    public void Set_About_Us(View view) {
        final EditText input = new EditText(this);
        input.setHint("Enter About Us  Text...");

        databaseReference = FirebaseDatabase.getInstance().getReference("AboutUsText");
        databaseReference.child("Text").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    input.setText(Objects.requireNonNull(snapshot.getValue()).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Set About us");
        alert.setView(input);
        alert.setCancelable(false);

        alert.setPositiveButton("Done", (dialogInterface, i) -> {
            String text = input.getText().toString();

            databaseReference.child("Text").setValue(text);

            Toast.makeText(this, "Successfully done!", Toast.LENGTH_LONG).show();

        });

        alert.setNegativeButton("Cancel", (dialogInterface, i) -> {
            // Nothing
        });

        alert.show();
    }

    public void donationImage(View view) {
        check = 1;
        Change_Profile_Image();
    }

    public void BusinessImage(View view) {
        check = 2;
        Change_Profile_Image();
    }

    // Change Image """"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
    private void Change_Profile_Image() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_FROM_GALLERY);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY
                && resultCode == RESULT_OK && data.getData() != null) {

            contentURI = data.getData();

            CropImage.activity(contentURI).start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                SaveImageOnFirebaseStorage();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void SaveImageOnFirebaseStorage() {

        if (resultUri!= null) {
            progressDialog.setMessage("Updating Profile Picture");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            databaseReference2 = FirebaseDatabase.getInstance().getReference("ImageHome");
            if (PICK_FROM_GALLERY == 1) {
                fileReference = storageReference.child("donationImage");
            }else {
                fileReference = storageReference.child("businessImage");
            }

            storageTask = fileReference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri uri = urlTask.getResult();
                    imageUri = uri.toString();

                    if (check == 1) {
                        databaseReference2.child("donationImage").setValue(imageUri);
                    }else if(check == 2){
                        databaseReference2.child("businessImage").setValue(imageUri);
                    }else {
                        databaseReference2.child("gameImage").setValue(imageUri);
                    }

                    Toast.makeText(getApplicationContext(), "Update Successfully!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Error", e.getMessage());
                progressDialog.dismiss();
            });

        }else {
            Toast.makeText(getApplicationContext(), "Select An Image", Toast.LENGTH_SHORT).show();
        }
    }

    public void GameImage(View view) {
        check = 3;
        Change_Profile_Image();
    }

    public void Shop(View view) {
        startActivity(new Intent(this, ShopHelp.class));
    }

    public void coinCost(View view) {
        final EditText input = new EditText(this);
        input.setHint("Enter Mobile Number...");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        databaseReference = FirebaseDatabase.getInstance().getReference("Coin");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Set Sell Coin Cost");
        alert.setView(input);
        alert.setCancelable(false);

        alert.setPositiveButton("Done", (dialogInterface, i) -> {
            String number = input.getText().toString();

            databaseReference.child("sellCoinCost").setValue(number);

            Toast.makeText(this, "Successfully done!", Toast.LENGTH_LONG).show();

        });

        alert.setNegativeButton("Cancel", (dialogInterface, i) -> {
            // Nothing
        });

        alert.show();
    }
    // End Change Image """"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

}