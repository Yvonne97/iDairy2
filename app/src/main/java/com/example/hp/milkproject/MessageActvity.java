package com.example.hp.milkproject;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.milkproject.model.dataModels;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MessageActvity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    String userUid, otherUserUid, fullNames, otherFullNames;
    FirestoreRecyclerAdapter firestoreRecyclerAdapter;
    FloatingActionButton floatingActionButtonSend;
    EditText editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_actvity);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        otherUserUid = bundle.getString("uid");
        otherFullNames = bundle.getString("fullNames");

        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        floatingActionButtonSend = (FloatingActionButton) findViewById(R.id.floatingActionButtonSend);

        editTextMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(firestoreRecyclerAdapter.getItemCount() - 1);
            }
        });

        floatingActionButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy");
                String formattedDate = df.format(c);

                String message = editTextMessage.getText().toString();
                if (message.equals("")){
                    Toast.makeText(MessageActvity.this, "Please add a message to send", Toast.LENGTH_SHORT).show();
                }else {
                    editTextMessage.setText("");
                    final Map map = new HashMap();
                    map.put("message", message);
                    map.put("date", formattedDate);
                    map.put("uid", userUid);
                    map.put("fullNames", fullNames);

                    final Map map1 = new HashMap();
                    map1.put("message", message);
                    map1.put("date", formattedDate);
                    map1.put("uid", otherUserUid);
                    map1.put("fullNames", otherFullNames);

                    firebaseFirestore.collection("Messages").document(userUid).collection(otherUserUid).add(map)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()){
                                        firebaseFirestore.collection("Messages").document(otherUserUid).collection(userUid).add(map)
                                                .addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {

                                                    }
                                                });
                                    }
                                }
                            });

                    firebaseFirestore.collection("Chats").document(userUid).collection(userUid).document(otherUserUid).set(map1)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    firebaseFirestore.collection("Chats").document(otherUserUid).collection(otherUserUid).document(userUid).set(map)
                                            .addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()){
                                                    }
                                                }
                                            });
                                }
                            });
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseFirestore = FirebaseFirestore.getInstance();
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getData(userUid);

        final Query query = firebaseFirestore.collection("Messages").document(userUid).collection(otherUserUid).orderBy("date", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<dataModels> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<dataModels>()
                .setQuery(query, dataModels.class)
                .build();

        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<dataModels, MessageActvity.dataHolder>(firestoreRecyclerOptions) {

            @NonNull
            @Override
            public MessageActvity.dataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.messaging_layout, parent, false);

                return new MessageActvity.dataHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MessageActvity.dataHolder holder, int position, @NonNull final dataModels model) {
                if (userUid.equals(model.getUid())){
                    holder.relativeLayout1.setVisibility(View.GONE);
                    holder.relativeLayout2.setVisibility(View.VISIBLE);
                    holder.textViewDate2.setText(model.getDate());
                    holder.textViewMessage2.setText(model.getMessage());
                    holder.textFullNames2.setText(model.getFullNames());
                }else {
                    holder.relativeLayout2.setVisibility(View.GONE);
                    holder.relativeLayout1.setVisibility(View.VISIBLE);
                    holder.textViewDate1.setText(model.getDate());
                    holder.textViewMessage1.setText(model.getMessage());
                    holder.textFullNames1.setText(model.getFullNames());
                }

            }
        };

        firestoreRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(firestoreRecyclerAdapter);
        final int speedScroll = 100;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                if(count < firestoreRecyclerAdapter.getItemCount()){
                    recyclerView.scrollToPosition(++count);
                    handler.postDelayed(this,speedScroll);
                }
            }
        };

        handler.postDelayed(runnable,speedScroll);

    }

    private void getData(String userUid1){
        FirebaseFirestore firebaseFirestore1 = FirebaseFirestore.getInstance();
        firebaseFirestore1.collection("Users").document(userUid1).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                fullNames = document.getString("fname")+" "+document.getString("sname");
                Toast.makeText(MessageActvity.this, fullNames, Toast.LENGTH_SHORT).show();
            }
        });
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

        public TextView textFullNames1, textViewDate1, textViewMessage1;
        public TextView textFullNames2, textViewDate2, textViewMessage2;
        RelativeLayout relativeLayout1, relativeLayout2;

        public dataHolder(View itemView) {
            super(itemView);
            relativeLayout1 = itemView.findViewById(R.id.relative1);
            relativeLayout2 = itemView.findViewById(R.id.relative2);
            textViewDate1 = itemView.findViewById(R.id.textViewDate1);
            textViewMessage1 = itemView.findViewById(R.id.textViewMessage1);
            textFullNames1 = itemView.findViewById(R.id.textViewFullNames1);
            textViewDate2 = itemView.findViewById(R.id.textViewDate2);
            textViewMessage2 = itemView.findViewById(R.id.textViewMessage2);
            textFullNames2 = itemView.findViewById(R.id.textViewFullNames2);

        }
    }
}
