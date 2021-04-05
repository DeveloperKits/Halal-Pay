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

public class DonationList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private SubCategoryAdapter subCategoryAdapter;

    private ArrayList<SubCategory> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();
        subCategoryAdapter = new SubCategoryAdapter(this, arrayList, "DonationRequest");
        recyclerView.setAdapter(subCategoryAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("DonationRequest");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                subCategoryAdapter.notifyDataSetChanged();

                if (snapshot.exists()){

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        SubCategory category = new SubCategory(ds.getKey());
                        arrayList.add(category);
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