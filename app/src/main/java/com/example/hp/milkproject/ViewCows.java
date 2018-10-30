package com.example.hp.milkproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.milkproject.model.dataModels;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewCows extends AppCompatActivity {

    ListView listView;
    FirebaseListAdapter<dataModels> dataFirebaseListAdapter;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_fragment_layout);

        listView = (ListView) findViewById(R.id.listView);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cows");

        FirebaseListOptions<dataModels> options = new FirebaseListOptions.Builder<dataModels>()
                .setQuery(databaseReference, dataModels.class)
                .setLayout(R.layout.cows_list)
                .build();

        dataFirebaseListAdapter = new FirebaseListAdapter<dataModels>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull dataModels model, final int position) {
                TextView textViewName = (TextView) v.findViewById(R.id.textViewName);
                textViewName.setText(model.getName());
                TextView textViewBirth = (TextView) v.findViewById(R.id.textViewBirth);
                textViewBirth.setText(model.getBirth());
                TextView textViewBreed = (TextView) v.findViewById(R.id.textViewBreed);
                textViewBreed.setText(model.getBreed());;
                TextView textViewKG = (TextView) v.findViewById(R.id.textViewKG);
                textViewKG.setText(model.getKg());

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ViewCows.this, getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        listView.setAdapter(dataFirebaseListAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        dataFirebaseListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dataFirebaseListAdapter.stopListening();
    }
}
