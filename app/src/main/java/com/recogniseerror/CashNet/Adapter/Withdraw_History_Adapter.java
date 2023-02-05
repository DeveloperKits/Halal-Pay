package com.recogniseerror.CashNet.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.recogniseerror.CashNet.Model.depositHistoryModel;
import com.recogniseerror.CashNet.R;

import java.util.ArrayList;

public class Withdraw_History_Adapter extends RecyclerView.Adapter<Withdraw_History_Adapter.viewHolder> {

    private final Context pContext;
    private final ArrayList<depositHistoryModel> historyModel;

    public Withdraw_History_Adapter(Context pContext, ArrayList<depositHistoryModel> historyModel){
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
            holder.withdrawNumber.setPadding(0,0,0,50);
        }

        holder.databaseReference.child(key).addValueEventListener(new ValueEventListener() {
            @SuppressLint({"SetTextI18n", "ResourceAsColor"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String date = snapshot.child("Date").getValue().toString();
                String time = snapshot.child("Time").getValue().toString();
                String Amount = snapshot.child("withdrawMoney").getValue().toString();
                String number = snapshot.child("WithdrawPhone").getValue().toString();
                String type = snapshot.child("withdrawType").getValue().toString();
                String Status = snapshot.child("status").getValue().toString();

                holder.DateTime.setTextColor(ContextCompat.getColor(pContext, R.color.red_light));
                holder.DateTime.setText(date + "  " + time);
                holder.status.setText(Status);
                holder.withdrawNumber.setText("Dep Num: " + number);
                holder.withdrawType.setText("Dep Type: " + type);
                holder.Amount.setText("+ ৳" + Amount);
                holder.interest_Text.setVisibility(View.GONE);
                holder.paymentID.setVisibility(View.GONE);
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

    public class viewHolder extends RecyclerView.ViewHolder {

        private final TextView paymentID, DateTime, withdrawType, Amount, withdrawNumber, status, interest_Text;
        private final View viewX;
        private final DatabaseReference databaseReference;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            interest_Text = itemView.findViewById(R.id.InterestText);
            paymentID = itemView.findViewById(R.id.transID);
            DateTime = itemView.findViewById(R.id.dateTime);
            withdrawType = itemView.findViewById(R.id.depType);
            Amount = itemView.findViewById(R.id.amount);
            withdrawNumber = itemView.findViewById(R.id.depNum);
            status = itemView.findViewById(R.id.status);
            viewX = itemView.findViewById(R.id.view);

            databaseReference = FirebaseDatabase.getInstance().getReference("WithdrawRequest");
        }
    }
}
