package com.samulit.halal_pay.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.samulit.halal_pay.HomeActivity;
import com.samulit.halal_pay.R;


public class HomeFragment extends Fragment {
    private MaterialCardView Donation, Business_Loan;
    private ImageView Profile_Image;
    private TextView UserName, UserBalance, UserPercentage, Percentage_Day;
    private Button back;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Donation = view.findViewById(R.id.donation);
        Business_Loan = view.findViewById(R.id.business_loan);
        Profile_Image = view.findViewById(R.id.profileImage);
        UserName = view.findViewById(R.id.UserName);
        UserBalance = view.findViewById(R.id.UserBalance);
        UserPercentage = view.findViewById(R.id.UserPercentage);
        Percentage_Day = view.findViewById(R.id.percentage_day);
        back = view.findViewById(R.id.back);



        return view;
    }

}