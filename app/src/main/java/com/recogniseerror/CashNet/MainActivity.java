package com.recogniseerror.CashNet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements LifecycleObserver {

    private FirebaseUser user;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();
        //updateUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent goToMain;
                if (user != null){
                    goToMain = new Intent(MainActivity.this, HomeActivity.class);
                }else {
                    goToMain = new Intent(MainActivity.this, LoginActivity.class);
                }
                startActivity(goToMain);
                finish();
            }
        },1200);
    }



    // where need to change edit there and change
    private void updateUser() {

        userRef = FirebaseDatabase.getInstance().getReference("UserData");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userRef.child(snapshot.getKey())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String, Object> postValues = new HashMap<>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        postValues.put(snapshot.getKey(),snapshot.getValue());
                                    }

                                    postValues.put("UserCoin", 6000);
                                    //postValues.put("vPanelAccess", "No");
                                    userRef.child(snapshot.getKey()).updateChildren(postValues);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
