package com.samulit.halal_pay_admin_panel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.samulit.halal_pay_admin_panel.Adapter.SubCategoryAdapter;
import com.samulit.halal_pay_admin_panel.Model.SubCategory;

import java.util.ArrayList;
import java.util.Objects;

public class DepositList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private SubCategoryAdapter subCategoryAdapter;

    private ArrayList<SubCategory> arrayList;
    private String getIntentType, string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        getIntentType = getIntent().getStringExtra("Type");
        string = getIntent().getStringExtra("string");

        arrayList = new ArrayList<>();
        subCategoryAdapter = new SubCategoryAdapter(this, arrayList, "DepositRequest");
        recyclerView.setAdapter(subCategoryAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("DepositRequest");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                subCategoryAdapter.notifyDataSetChanged();

                if (snapshot.exists()){

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String InterestType = String.valueOf(ds.child("InterestType").getValue());
                        String status = ds.child("status").getValue().toString();

                        if (string.equals("Successfully Done")) {
                            if (InterestType.equals(getIntentType) && string.equals(status)) {
                                SubCategory category = ds.getValue(SubCategory.class);
                                Objects.requireNonNull(category).setUID(ds.getKey());
                                arrayList.add(category);
                            }

                        }else {
                            if (InterestType.equals(getIntentType) && !status.equals("Successfully Done")){
                                SubCategory category = ds.getValue(SubCategory.class);
                                Objects.requireNonNull(category).setUID(ds.getKey());
                                arrayList.add(category);
                            }
                        }
                    }

                }

                subCategoryAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}