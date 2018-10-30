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

import com.example.hp.milkproject.model.dataModels;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class WorkerMessageActvity extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    String userUid;
    FirestoreRecyclerAdapter firestoreRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_boss_message, container, false);

        recyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseFirestore = FirebaseFirestore.getInstance();
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final Query query = firebaseFirestore.collection("Chats").document(userUid).collection(userUid);

        FirestoreRecyclerOptions<dataModels> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<dataModels>()
                .setQuery(query, dataModels.class)
                .build();

        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<dataModels, WorkerMessageActvity.dataHolder>(firestoreRecyclerOptions) {

            @NonNull
            @Override
            public WorkerMessageActvity.dataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.boss_message_worker_layout, parent, false);

                return new WorkerMessageActvity.dataHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull WorkerMessageActvity.dataHolder holder, int position, @NonNull final dataModels model) {
                holder.textViewDate.setText(model.getDate());
                holder.textViewMessage.setText(model.getMessage());
                holder.textFullNames.setText(model.getFullNames());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MessageActvity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("uid", model.getUid());
                        bundle.putString("fullNames", model.getFullNames());
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

        public TextView textFullNames, textViewDate, textViewMessage;

        public dataHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textFullNames = itemView.findViewById(R.id.textViewFullNames);
        }
    }

}
