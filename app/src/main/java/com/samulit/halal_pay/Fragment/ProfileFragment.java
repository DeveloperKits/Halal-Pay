package com.samulit.halal_pay.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.samulit.halal_pay.R;


public class ProfileFragment extends Fragment {
    private Button Edit;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Edit = view.findViewById(R.id.edit);

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfile();
            }
        });

        return view;
    }

    private void EditProfile() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_box_edit_profile,null);
        final EditText EditEmail = (EditText)mView.findViewById(R.id.email);
        final EditText EditName = (EditText)mView.findViewById(R.id.name);
        final EditText EditAge = (EditText)mView.findViewById(R.id.age);
        Button btn_okay = (Button)mView.findViewById(R.id.done);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel);


        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}