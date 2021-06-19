package com.samulit.halal_pay.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.samulit.halal_pay.Model.depositHistoryModel;
import com.samulit.halal_pay.R;

import java.util.ArrayList;

public class Deposit_History_Adapter extends RecyclerView.Adapter<Deposit_History_Adapter.viewHolder> {

    private final Context pContext;
    private final ArrayList<depositHistoryModel> historyModel;

    public Deposit_History_Adapter(Context pContext, ArrayList<depositHistoryModel> historyModel) {
        this.pContext = pContext;
        this.historyModel = historyModel;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(pContext).inflate(R.layout.deposit_history_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final depositHistoryModel info = historyModel.get(position);
        final String key = info.getID();

        if (position == historyModel.size()-1){
            holder.viewX.setVisibility(View.GONE);
            holder.depositNumber.setPadding(0,0,0,50);
        }

        holder.databaseReference.child(key).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String payID = snapshot.child("PaymentID").getValue().toString();
                String date = snapshot.child("Date").getValue().toString();
                String time = snapshot.child("Time").getValue().toString();
                String Amount = snapshot.child("DepositAmount").getValue().toString();
                String number = snapshot.child("depositNumber").getValue().toString();
                String type = snapshot.child("depositType").getValue().toString();
                String Status = snapshot.child("status").getValue().toString();

                holder.paymentID.setText("Pay ID: " + payID);
                holder.DateTime.setText(date + "  " + time);
                holder.status.setText(Status);
                holder.depositNumber.setText("Dep Num: " + number);
                holder.depositType.setText("Dep Type: " + type);
                holder.Amount.setText("+ ৳" + Amount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return historyModel.size();
    }


    public static class viewHolder extends RecyclerView.ViewHolder {

        private final TextView paymentID, DateTime, depositType, Amount, depositNumber, status;
        private final View viewX;
        private final DatabaseReference databaseReference;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            paymentID = itemView.findViewById(R.id.transID);
            DateTime = itemView.findViewById(R.id.dateTime);
            depositType = itemView.findViewById(R.id.depType);
            Amount = itemView.findViewById(R.id.amount);
            depositNumber = itemView.findViewById(R.id.depNum);
            status = itemView.findViewById(R.id.status);
            viewX = itemView.findViewById(R.id.view);

            databaseReference = FirebaseDatabase.getInstance().getReference("DepositRequest");
        }
    }
}
