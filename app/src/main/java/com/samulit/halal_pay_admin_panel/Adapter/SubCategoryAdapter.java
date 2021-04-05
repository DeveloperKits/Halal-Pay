package com.samulit.halal_pay_admin_panel.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.samulit.halal_pay_admin_panel.DetailsActivity;
import com.samulit.halal_pay_admin_panel.Model.SubCategory;
import com.samulit.halal_pay_admin_panel.R;

import java.util.ArrayList;


public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    private Context pContext;
    private ArrayList<SubCategory> arrayList;
    private String type;

    public SubCategoryAdapter(Context pContext, ArrayList<SubCategory> arrayList, String type) {
        this.pContext = pContext;
        this.arrayList = arrayList;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(pContext).inflate(R.layout.sub_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubCategory category = arrayList.get(position);

        holder.databaseReference.child(type).child(category.getUID()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("userName").getValue().toString();
                    String status = snapshot.child("status").getValue().toString();
                    String date = snapshot.child("Date").getValue().toString();
                    String time = snapshot.child("Time").getValue().toString();

                    if (name.length() > 30) {
                        holder.name.setText(name.substring(1, 27) + "...");
                    }else {
                        holder.name.setText(name);
                    }
                    holder.status.setText(status);
                    holder.time.setText(date + "   " + time);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), DetailsActivity.class);
            intent.putExtra("user_uid", category.getUID());
            intent.putExtra("Type", type);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, status, time;
        private DatabaseReference databaseReference;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.c_Name_title);
            status = itemView.findViewById(R.id.status);
            time = itemView.findViewById(R.id.time);

            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
    }
}
