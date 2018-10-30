package com.example.hp.milkproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class CowActivity extends android.support.v4.app.Fragment {

    Button buttonViewPro, buttonSellCow, buttonSellMilk, buttonAddFeed,
    buttonAddVac, buttonAddDis, buttonAddTreat, buttonAddMilk, buttonAddCow, buttonView;
    ArrayList<String> stringsCategoryName;
    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cow_fragment_layout, container, false);

        progressDialog = new ProgressDialog(getActivity());

        buttonSellCow = (Button) rootView.findViewById(R.id.buttonSellCow);
        buttonSellMilk = (Button) rootView.findViewById(R.id.buttonSellMilk);
        buttonAddCow = (Button) rootView.findViewById(R.id.buttonAddCow);
        buttonView = (Button) rootView.findViewById(R.id.buttonView);

        buttonAddCow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddCowActivity.class));
            }
        });

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewCows.class));
            }
        });

        buttonSellMilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellMilk.class);
                startActivity(intent);
            }
        });

        buttonSellCow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellCow.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
