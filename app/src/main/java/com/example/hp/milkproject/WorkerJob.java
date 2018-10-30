package com.example.hp.milkproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class WorkerJob extends AppCompatActivity {

    Button buttonViewPro, buttonSellCow, buttonSellMilk, buttonAddFeed,
            buttonAddVac, buttonAddDis, buttonAddTreat, buttonAddMilk, buttonAddCow, buttonView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_job);

        buttonAddFeed = (Button) findViewById(R.id.buttonAddFeed);
        buttonAddVac = (Button) findViewById(R.id.buttonAddVaccine);
        buttonAddDis = (Button) findViewById(R.id.buttonAddDisease);
        buttonAddCow = (Button) findViewById(R.id.buttonAddCow);
        buttonView = (Button) findViewById(R.id.buttonView);
        buttonAddTreat = (Button) findViewById(R.id.buttonAddTraet);

        buttonAddCow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkerJob.this, AddDisease.class));
            }
        });

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkerJob.this, ViewCows.class));
            }
        });


        buttonAddDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkerJob.this, AddMilking.class);
                startActivity(intent);
            }
        });

        buttonAddFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkerJob.this, AddFedd.class);
                startActivity(intent);
            }
        });

        buttonAddVac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkerJob.this, AddVaccine.class);
                startActivity(intent);
            }
        });

        buttonAddTreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkerJob.this, AddTreat.class);
                startActivity(intent);
            }
        });

    }
}
