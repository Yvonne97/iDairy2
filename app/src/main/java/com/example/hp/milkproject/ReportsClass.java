package com.example.hp.milkproject;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hp.milkproject.model.dataModels;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportsClass extends Fragment {

    ListView listView;
    DatabaseReference databaseReference;
    FirebaseListAdapter<dataModels> dataFirebaseListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.report_fragment_layout, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Report");

        listView = (ListView) rootView.findViewById(R.id.listView);

        FirebaseListOptions<dataModels> options = new FirebaseListOptions.Builder<dataModels>()
                .setQuery(databaseReference, dataModels.class)
                .setLayout(R.layout.report_list)
                .build();

        dataFirebaseListAdapter = new FirebaseListAdapter<dataModels>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull dataModels model, final int position) {
                if (model.getName() != null){
                    TextView textViewName = (TextView) v.findViewById(R.id.textViewName);
                    textViewName.setText(model.getName());
                    textViewName.setVisibility(View.VISIBLE);
                }
                if (model.getQuantity() != null){
                    TextView textViewBirth = (TextView) v.findViewById(R.id.textViewQuantity);
                    textViewBirth.setText(model.getQuantity());
                    textViewBirth.setVisibility(View.VISIBLE);
                }if (model.getMilkedTime() != null){
                    TextView textViewBreed = (TextView) v.findViewById(R.id.textViewmilked);
                    textViewBreed.setText(model.getMilkedTime());
                    textViewBreed.setVisibility(View.VISIBLE);
                }

                if (model.getKg() != null){
                    TextView textViewKG = (TextView) v.findViewById(R.id.textViewKG);
                    textViewKG.setText(model.getKg());
                    textViewKG.setVisibility(View.VISIBLE);
                }

                if (model.getMessage() != null){
                    TextView textViewMessage = (TextView) v.findViewById(R.id.textViewMessage);
                    textViewMessage.setText(model.getMessage());
                    textViewMessage.setVisibility(View.VISIBLE);
                }
                if (model.getVacName() != null){
                    TextView textViewVac = (TextView) v.findViewById(R.id.textView5vacnam);
                    textViewVac.setText(model.getVacName());
                    textViewVac.setVisibility(View.VISIBLE);
                }
                if (model.getFeedName() != null){
                    TextView textViewfeed = (TextView) v.findViewById(R.id.textView4feedname);
                    textViewfeed.setText(model.getFeedName());
                    textViewfeed.setVisibility(View.VISIBLE);
                }
                if (model.getCost() != null){
                    TextView textViewcost = (TextView) v.findViewById(R.id.textView6cost);
                    textViewcost.setText(model.getCost());
                    textViewcost.setVisibility(View.VISIBLE);
                }
                if (model.getDate() != null){
                    TextView textViewdate = (TextView) v.findViewById(R.id.textView3date);
                    textViewdate.setText(model.getDate());
                    textViewdate.setVisibility(View.VISIBLE);
                }

            }
        };

        listView.setAdapter(dataFirebaseListAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        dataFirebaseListAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        dataFirebaseListAdapter.stopListening();
    }
}
