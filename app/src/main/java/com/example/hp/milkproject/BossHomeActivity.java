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
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.milkproject.model.dataModels;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class BossHomeActivity extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    String userUid;
    FirestoreRecyclerAdapter firestoreRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_boss_home, container, false);

        recyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseFirestore = FirebaseFirestore.getInstance();
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final Query query = firebaseFirestore.collection("Worker");

        FirestoreRecyclerOptions<dataModels> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<dataModels>()
                .setQuery(query, dataModels.class)
                .build();

        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<dataModels, BossHomeActivity.dataHolder>(firestoreRecyclerOptions) {

            @NonNull
            @Override
            public BossHomeActivity.dataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.boss_home_recycler_layout, parent, false);

                return new BossHomeActivity.dataHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull BossHomeActivity.dataHolder holder, int position, @NonNull final dataModels model) {

                holder.textFullNames.setText(String.valueOf(model.getFname())+" "+String.valueOf(model.getSname()));


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), WorkerDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("uid", model.getUid());
                        bundle.putString("fname", model.getFname());
                        bundle.putString("sname", model.getSname());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

            }
        };

        firestoreRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(firestoreRecyclerAdapter);


        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        firestoreRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firestoreRecyclerAdapter.stopListening();
    }

    public class dataHolder extends RecyclerView.ViewHolder {

        public TextView textFullNames;

        public dataHolder(View itemView) {
            super(itemView);
            textFullNames = itemView.findViewById(R.id.textViewFullNames);
        }
    }

}
