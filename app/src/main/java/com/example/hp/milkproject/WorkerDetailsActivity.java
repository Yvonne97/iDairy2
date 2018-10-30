package com.example.hp.milkproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class WorkerDetailsActivity extends AppCompatActivity {

    String fname, sname, uid;
    int totalCowsMilked, totalLitreCollected;
    TextView textViewFullNames;
    Button buttonMessage, buttonShowData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fname = bundle.getString("fname");
        sname = bundle.getString("sname");
        uid = bundle.getString("uid");


        textViewFullNames = (TextView) findViewById(R.id.textViewFullNames);
        textViewFullNames.setText(fname+" "+sname);

        buttonMessage = (Button) findViewById(R.id.buttonMessage);
        buttonShowData = (Button) findViewById(R.id.buttonShow);

        buttonShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkerDetailsActivity.this, ShowDataActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid", uid);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkerDetailsActivity.this, MessageActvity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid", uid);
                bundle.putString("fullNames", fname+" "+sname);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

}
