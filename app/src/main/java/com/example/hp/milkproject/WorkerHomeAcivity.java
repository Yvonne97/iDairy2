package com.example.hp.milkproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.milkproject.model.dataModels;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class WorkerHomeAcivity extends Fragment {

    FirebaseFirestore firebaseFirestore;
    String userUid;
    DatabaseReference databaseReference;
    FirebaseListAdapter<dataModels> dataFirebaseListAdapter;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_worker_home, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        listView = (ListView) rootview.findViewById(R.id.listView);
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null){
            userUid = bundle.getString("uid");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Report");

        FirebaseListOptions<dataModels> options = new FirebaseListOptions.Builder<dataModels>()
                .setQuery(databaseReference, dataModels.class)
                .setLayout(R.layout.report_list)
                .build();

        dataFirebaseListAdapter = new FirebaseListAdapter<dataModels>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull dataModels model, final int position) {
                if (model.getUid().equals(userUid)) {
                    if (model.getName() != null) {
                        TextView textViewName = (TextView) v.findViewById(R.id.textViewName);
                        textViewName.setText(model.getName());
                        textViewName.setVisibility(View.VISIBLE);
                    }
                    if (model.getQuantity() != null) {
                        TextView textViewBirth = (TextView) v.findViewById(R.id.textViewQuantity);
                        textViewBirth.setText(model.getQuantity());
                        textViewBirth.setVisibility(View.VISIBLE);
                    }
                    if (model.getMilkedTime() != null) {
                        TextView textViewBreed = (TextView) v.findViewById(R.id.textViewmilked);
                        textViewBreed.setText(model.getMilkedTime());
                        textViewBreed.setVisibility(View.VISIBLE);
                    }

                    if (model.getKg() != null) {
                        TextView textViewKG = (TextView) v.findViewById(R.id.textViewKG);
                        textViewKG.setText(model.getKg());
                        textViewKG.setVisibility(View.VISIBLE);
                    }

                    if (model.getMessage() != null) {
                        TextView textViewMessage = (TextView) v.findViewById(R.id.textViewMessage);
                        textViewMessage.setText(model.getMessage());
                        textViewMessage.setVisibility(View.VISIBLE);
                    }
                    if (model.getVacName() != null) {
                        TextView textViewVac = (TextView) v.findViewById(R.id.textView5vacnam);
                        textViewVac.setText(model.getVacName());
                        textViewVac.setVisibility(View.VISIBLE);
                    }
                    if (model.getFeedName() != null) {
                        TextView textViewfeed = (TextView) v.findViewById(R.id.textView4feedname);
                        textViewfeed.setText(model.getFeedName());
                        textViewfeed.setVisibility(View.VISIBLE);
                    }
                    if (model.getCost() != null) {
                        TextView textViewcost = (TextView) v.findViewById(R.id.textView6cost);
                        textViewcost.setText(model.getCost());
                        textViewcost.setVisibility(View.VISIBLE);
                    }
                    if (model.getDate() != null) {
                        TextView textViewdate = (TextView) v.findViewById(R.id.textView3date);
                        textViewdate.setText(model.getDate());
                        textViewdate.setVisibility(View.VISIBLE);
                    }
                }

            }
        };

        listView.setAdapter(dataFirebaseListAdapter);


        return rootview;
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
