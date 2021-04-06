package com.samulit.halal_pay.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.samulit.halal_pay.R;
import com.samulit.halal_pay.RegistrationActivity;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {
    private Button Edit, Change_Profile_Image;
    private ImageView Profile_Image;
    private TextView UserName, UserEmail, UserPassword, UserAge, UserPhone, Help_Number, Month, Year;
    private ProgressDialog progressDialog;

    private DatabaseReference databaseReference, databaseReference2;
    private FirebaseUser firebaseUser;
    StorageReference storageReference;
    StorageTask storageTask;
    Uri contentURI, resultUri;

    private static final int PICK_FROM_GALLERY = 1;
    private String Transfer_Type, UserID, userName, userImage, email, password, age, imageUri, WeekMonthYear, userPhone;
    private boolean isChecked = false;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Edit = view.findViewById(R.id.edit);
        Change_Profile_Image = view.findViewById(R.id.editImage);
        Profile_Image = view.findViewById(R.id.profileImage);
        UserName = view.findViewById(R.id.name);
        UserPhone = view.findViewById(R.id.number);
        Help_Number = view.findViewById(R.id.help);
        UserPassword = view.findViewById(R.id.password);
        UserEmail = view.findViewById(R.id.email);
        UserAge = view.findViewById(R.id.age);

        progressDialog = new ProgressDialog(getActivity());


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            UserID = firebaseUser.getUid();
            storageReference = FirebaseStorage.getInstance().getReference().child("UserImage").child(UserID);;
            databaseReference = FirebaseDatabase.getInstance().getReference("UserData").child(UserID);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        userName = snapshot.child("userName").getValue().toString();
                        userImage = snapshot.child("userImage").getValue().toString();
                        email = snapshot.child("userEmail").getValue().toString();
                        password = snapshot.child("userPassword").getValue().toString();
                        age = snapshot.child("userAge").getValue().toString();
                        WeekMonthYear = snapshot.child("WeekMonthYear").getValue().toString();
                        userPhone = snapshot.child("userPhone").getValue().toString();

                        UserName.setText(userName);
                        UserEmail.setText(email);
                        if (!password.equals(" ")) {
                            UserPassword.setText(password);
                        } else {
                            UserPassword.setText("Login With Google Account");
                        }

                        if (userPhone.equals(" ")){
                            UserPhone.setText("Set Your Number");
                        }else {
                            UserPhone.setText(userPhone);
                        }

                        if (!age.equals(" ")) {
                            UserAge.setText(age);
                        } else {
                            UserAge.setText("Enter Your Age");
                        }

                        if (!userImage.equals(" ")) {
                            Picasso.get().load(userImage).fit().centerInside().placeholder(R.drawable.loading_gif__).into(Profile_Image);
                        } else {
                            Picasso.get().load(R.drawable.prodile_pic2).fit().centerInside().into(Profile_Image);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            databaseReference2 = FirebaseDatabase.getInstance().getReference("HelpNumber").child("number");
            databaseReference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        Help_Number.setText(String.valueOf(snapshot.getValue()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditProfile();
                }
            });


            Change_Profile_Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Change_Profile_Image();
                }
            });
        }

        return view;
    }

    // Change Image """"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
    private void Change_Profile_Image() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_FROM_GALLERY);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_GALLERY
                && resultCode == RESULT_OK && data.getData() != null) {

            contentURI = data.getData();

            CropImage.activity(contentURI)
                    .setAspectRatio(1,1)
                    .start(getActivity(),this);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                Picasso.get().load(resultUri).fit().centerInside().into(Profile_Image);
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

            StorageReference fileReference = storageReference.child("ProfilePic");
            storageTask = fileReference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri uri = urlTask.getResult();
                    imageUri = uri.toString();

                    databaseReference.child("userImage").setValue(imageUri);
                    Toast.makeText(getActivity(), "Update Successfully!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(e -> Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show());

        }else {
            Toast.makeText(getActivity(), "Select An Image", Toast.LENGTH_SHORT).show();
        }
    }
    // End Change Image """"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""



    // When Click Edit Button
    private void EditProfile () {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_box_edit_profile, null);
        final EditText EditEmail = (EditText) mView.findViewById(R.id.email);
        final EditText EditName = (EditText) mView.findViewById(R.id.name);
        final EditText EditNumber = (EditText) mView.findViewById(R.id.number);
        final EditText EditAge = (EditText) mView.findViewById(R.id.age);
        final EditText EditPassword = (EditText) mView.findViewById(R.id.password);
        final EditText Edit_new_Password = (EditText) mView.findViewById(R.id.new_password);
        final LinearLayout linearLayout = (LinearLayout) mView.findViewById(R.id.password_linearLayout);
        final LinearLayout New_linearLayout = (LinearLayout) mView.findViewById(R.id.new_password_linearLayout);
        final SwitchMaterial CheckButton = (SwitchMaterial) mView.findViewById(R.id.checkbox);
        final TextView hint = (TextView) mView.findViewById(R.id.hint);
        Button btn_okay = (Button) mView.findViewById(R.id.done);
        Button btn_cancel = (Button) mView.findViewById(R.id.cancel);

        EditEmail.setText(UserEmail.getText().toString());
        EditName.setText(UserName.getText().toString());
        EditAge.setText(UserAge.getText().toString());
        EditNumber.setText(UserPhone.getText().toString());

        if (UserPassword.getText().equals("Login With Google Account")){
            linearLayout.setVisibility(View.GONE);
            CheckButton.setVisibility(View.GONE);
            New_linearLayout.setVisibility(View.GONE);
            hint.setVisibility(View.GONE);
        }else {
            CheckButton.setVisibility(View.VISIBLE);

            CheckButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        linearLayout.setVisibility(View.VISIBLE);
                        New_linearLayout.setVisibility(View.VISIBLE);
                        hint.setVisibility(View.VISIBLE);
                        isChecked = true;
                    }else {
                        linearLayout.setVisibility(View.GONE);
                        New_linearLayout.setVisibility(View.GONE);
                        hint.setVisibility(View.GONE);
                        isChecked = false;
                    }
                }
            });

        }

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_okay.setOnClickListener(v -> {
            alertDialog.dismiss();

            databaseReference = FirebaseDatabase.getInstance().getReference("UserData");

            if (isNetworkAvaliable()) {
                String names = " ", emails = " ", ages = " ", numbers = " ", newPassword = " ", OldPassword = " ";

                if (!userName.contentEquals(EditName.getText().toString())) {
                    names = EditName.getText().toString();
                    databaseReference.child(UserID).child("userName").setValue(names);
                }
                if (!email.contentEquals(EditEmail.getText().toString())) {
                    emails = EditEmail.getText().toString();
                    databaseReference.child(UserID).child("userEmail").setValue(emails);
                }
                if (!age.contentEquals(EditAge.getText().toString())) {
                    ages = EditAge.getText().toString();
                    databaseReference.child(UserID).child("userAge").setValue(ages);
                }

                if (!userPhone.contentEquals(EditNumber.getText().toString())) {
                    numbers = EditNumber.getText().toString();
                    databaseReference.child(UserID).child("userPhone").setValue(numbers);
                }

                if (isChecked) {
                    if (Edit_new_Password.getText() != null && Edit_new_Password.getText().length() > 6 &&
                            EditPassword.getText() != null && EditPassword.getText().length() > 6 &&
                            EditPassword.getText().toString().equals(password)) {

                        newPassword = Edit_new_Password.getText().toString();
                        OldPassword = EditPassword.getText().toString();
                        saveEditInformationOnFirebase(newPassword, OldPassword);

                    }else if (Edit_new_Password.getText() != null && EditPassword.getText() != null){
                        Toast.makeText(getContext(), "Successfully update...", Toast.LENGTH_LONG).show();
                    }else if (Edit_new_Password.getText() != null || EditPassword.getText() != null){
                        Toast.makeText(getContext(), "Successfully update...", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Successfully update...", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(getContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }

    private void saveEditInformationOnFirebase(String newPassword, String oldPassword) {
        progressDialog.setMessage("Please wait, update your account");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (!newPassword.equals(" ") && !oldPassword.equals(" ")) {

            if (!newPassword.equals(oldPassword)) {
                AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);
                firebaseUser.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    firebaseUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Password updated", Toast.LENGTH_SHORT).show();
                                                databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
                                                databaseReference.child(UserID).child("userPassword").setValue(newPassword);
                                            } else {
                                                Toast.makeText(getContext(), "Sorry password not updated. Please try again", Toast.LENGTH_SHORT).show();
                                            }
                                            progressDialog.dismiss();
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "Sorry password not updated. Please try again", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
            }else {
                Toast.makeText(getContext(), "Your password is the same", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        }else {
            progressDialog.dismiss();
        }

    }

    // Check Internet
    private boolean isNetworkAvaliable() {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            return mNetworkInfo != null;

        }catch (NullPointerException e){
            return false;

        }
    }


}


