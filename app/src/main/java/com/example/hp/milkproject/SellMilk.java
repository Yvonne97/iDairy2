package com.example.hp.milkproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellMilk extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText editTextDate, editTextQuantity, editTextCost;
    Button buttonDone;
    Spinner spinner;
    ArrayList<String> stringsCategoryName;
    String cowName;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_milk);

        progressDialog = new ProgressDialog(this);

        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity);
        editTextCost = (EditText) findViewById(R.id.editTextCost);

        buttonDone = (Button) findViewById(R.id.buttonDone);
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cowName.equals("Choose Cow")){
                    Toast.makeText(SellMilk.this, "Please Choose a cow", Toast.LENGTH_SHORT).show();
                }else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("date", "date " +editTextDate.getText().toString());
                    map.put("name", cowName);
                    map.put("quantity", "quantity "+editTextQuantity.getText().toString());
                    map.put("cost", "cost "+editTextCost.getText().toString());
                    map.put("message", "Milk was sold");
                    map.put("uid", FirebaseAuth.getInstance().getUid());


                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Report").push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });
                }
            }
        });

        getCategoryNames();

    }

    private void getCategoryNames() {

        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (stringsCategoryName != null) {
            stringsCategoryName.clear();
        }

        stringsCategoryName = new ArrayList<String>();
        stringsCategoryName.add("Choose Cow");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Cows").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AddMilking.GetName getName = postSnapshot.getValue(AddMilking.GetName.class);
                    stringsCategoryName.add(getName.getName());
                }
                final ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(SellMilk.this, android.R.layout.simple_spinner_item, stringsCategoryName);
                adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapterCategory);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static class GetName {
        String name;

        public GetName() {
        }

        public GetName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        cowName = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
