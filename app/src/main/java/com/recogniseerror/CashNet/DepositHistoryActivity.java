package com.recogniseerror.CashNet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.recogniseerror.CashNet.Adapter.Deposit_History_Adapter;
import com.recogniseerror.CashNet.Model.depositHistoryModel;

import java.util.ArrayList;
import java.util.Collections;

public class DepositHistoryActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private Button back;
    private FirebaseUser user;

    private ArrayList<depositHistoryModel> adapterArrayList;
    private String UserID;
    private Deposit_History_Adapter depositHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_history);

        user = FirebaseAuth.getInstance().getCurrentUser();
        UserID = user.getUid();

        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recycle);

        back.setOnClickListener(view -> onBackPressed());

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManagerV);

        adapterArrayList = new ArrayList<>();
        depositHistoryAdapter = new Deposit_History_Adapter(this, adapterArrayList);
        recyclerView.setAdapter(depositHistoryAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("DepositRequest");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapterArrayList.clear();
                depositHistoryAdapter.notifyDataSetChanged();

                if(snapshot.exists()) {

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        String key = postSnapshot.getKey();

                        String ID = postSnapshot.child("userUID").getValue().toString();

                        if (UserID.equals(ID)) {
                            depositHistoryModel info = new depositHistoryModel(key);
                            adapterArrayList.add(info);
                        }

                    }
                    Collections.reverse(adapterArrayList);
                    depositHistoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}