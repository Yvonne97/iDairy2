package com.example.hp.milkproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddCowActivity extends AppCompatActivity {

    EditText editTextName, editTextCode, editTextBirth, editTextKG, editTextBreed;
    Button buttonDone;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cow);

        progressDialog = new ProgressDialog(this);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextCode = (EditText) findViewById(R.id.editTextCowCode);
        editTextBirth = (EditText) findViewById(R.id.editTextBirth);
        editTextKG = (EditText) findViewById(R.id.editTextKG);
        editTextBreed = (EditText) findViewById(R.id.editTextBread);
        buttonDone = (Button) findViewById(R.id.buttonDone);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextName.getText().toString().equals("")){

                }else {
                    progressDialog.setMessage("Adding cow...");
                    progressDialog.show();

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", editTextName.getText().toString());
                    map.put("code", "code "+editTextCode.getText().toString());
                    map.put("birth", "birth "+editTextBirth.getText().toString());
                    map.put("kg", "kg "+editTextKG.getText().toString());
                    map.put("uid", FirebaseAuth.getInstance().getUid());
                    map.put("breed", "breed "+editTextBreed.getText().toString());
                    map.put("message", "A cow was added");

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Report").push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });

                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Cows").child(editTextName.getText().toString());
                    databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddCowActivity.this, "Cow added", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddCowActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }
}
