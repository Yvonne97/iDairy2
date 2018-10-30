package com.example.hp.milkproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddMilkLiterActivity extends AppCompatActivity {
    
    EditText editTextCowMilked, editTextLitersCollected, editTextComment;
    Button buttonSubmit, buttonAddDate;
    FirebaseFirestore firebaseFirestore;
    Calendar calendar = Calendar.getInstance();
    String dateCollected;
    String userUid;
    ProgressDialog progressDialog;
    String fname, sname;
    Switch aSwitchYog, aSwitchMilk, aSwitchMala;
    boolean aBooleanYog, aBooleanMilk, aBooleanMala = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_milk_liter);

        progressDialog = new ProgressDialog(this);

        firebaseFirestore  = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (!task.isSuccessful()){
                            Toast.makeText(AddMilkLiterActivity.this, "Unable to get user name right now", Toast.LENGTH_SHORT).show();
                        }else {
                            fname = documentSnapshot.getString("fname");
                            sname = documentSnapshot.getString("sname");
                        }
                    }
                });
        
        editTextComment = (EditText) findViewById(R.id.editTextComments);
        editTextLitersCollected = (EditText) findViewById(R.id.editTextLitersCollected);
        editTextCowMilked = (EditText) findViewById(R.id.editTextCowsMilked);
        buttonAddDate = (Button) findViewById(R.id.buttonAddDate);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        aSwitchYog = (Switch) findViewById(R.id.switch1);
        aSwitchMilk = (Switch) findViewById(R.id.switch2);
        aSwitchMala = (Switch) findViewById(R.id.switch3);

        aSwitchYog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    aBooleanYog = true;
                    Toast.makeText(AddMilkLiterActivity.this, "Yoghurt Added", Toast.LENGTH_SHORT).show();
                }else {
                    aBooleanYog = false;
                    Toast.makeText(AddMilkLiterActivity.this, "Yoghurt Removed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        aSwitchMilk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    aBooleanMilk = true;
                    Toast.makeText(AddMilkLiterActivity.this, "Milk Added", Toast.LENGTH_SHORT).show();
                }else {
                    aBooleanMilk = false;
                    Toast.makeText(AddMilkLiterActivity.this, "Milk Removed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        aSwitchMala.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    aBooleanMala = true;
                    Toast.makeText(AddMilkLiterActivity.this, "Mala Added", Toast.LENGTH_SHORT).show();
                }else {
                    aBooleanMala = false;
                    Toast.makeText(AddMilkLiterActivity.this, "Mala Removed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateButtonWithDate();
            }

        };

        buttonAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddMilkLiterActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editTextComment.getText().toString();
                if (editTextCowMilked.getText().toString().equals("") || editTextLitersCollected.getText().toString().equals("") || comment.equals("") || dateCollected.equals("")){
                    Toast.makeText(AddMilkLiterActivity.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
                }else {
                    int cowsMilked = Integer.parseInt(editTextCowMilked.getText().toString());
                    int litersCollected = Integer.parseInt(editTextLitersCollected.getText().toString());
                    Toast.makeText(AddMilkLiterActivity.this, ""+dateCollected, Toast.LENGTH_SHORT).show();
                    submitData(cowsMilked, litersCollected, comment);
                }
            }
        });
    }

    private void updateButtonWithDate() {
        String dateFormat = "dd.MM.yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        buttonAddDate.setText(sdf.format(calendar.getTime()));
        dateCollected = sdf.format(calendar.getTime());
    }

    private void getAndCalculateData(){
        final FirebaseFirestore firebaseFirestore1 = FirebaseFirestore.getInstance();
        final FirebaseFirestore firebaseFirestore2 = FirebaseFirestore.getInstance();
        CollectionReference addData =  firebaseFirestore1.collection("Liters Collected").document(userUid).collection(userUid);
        addData.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(AddMilkLiterActivity.this, "Unable to calcualte data", Toast.LENGTH_SHORT).show();
                }else {
                    long totalCowsMilked = 0;
                    long totallitersCollected = 0;
                    for (DocumentSnapshot document : task.getResult()) {
                        //Log.d(TAG, document.getId() + " => " + document.getData());
                        totalCowsMilked = document.getLong("cowsMilked") + totalCowsMilked;
                        totallitersCollected = document.getLong("litersCollected") + totallitersCollected;
                        //Toast.makeText(AddMilkLiterActivity.this, ""+document.getLong("cowsMilked"), Toast.LENGTH_SHORT).show();
                        //totalCowsMilked = (int) document.get("cowsMilked");
                    }

                    Toast.makeText(AddMilkLiterActivity.this, ""+totalCowsMilked, Toast.LENGTH_SHORT).show();

                    Map<String, Object> map = new HashMap<>();
                    map.put("totalCowsMilked", totalCowsMilked);
                    map.put("totalLitersCollected", totallitersCollected);
                    map.put("fname", fname);
                    map.put("sname", sname);
                    map.put("uid", userUid);

                    firebaseFirestore2.collection("Workers").document(userUid).set(map);

                }
            }
        });
    }

    private void submitData(int cowsMilked, int litersCollected, String comment){
        progressDialog.setMessage("Submitting ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Map<String, Object> map = new HashMap<>();
        map.put("cowsMilked", cowsMilked);
        map.put("litersCollected", litersCollected);
        map.put("comment", comment);
        map.put("yoghurt", aBooleanYog);
        map.put("milk", aBooleanMilk);
        map.put("mala", aBooleanMala);
        map.put("date", dateCollected);
        
        firebaseFirestore.collection("Liters Collected").document(userUid).collection(userUid)
                .document(dateCollected).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddMilkLiterActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                getAndCalculateData();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddMilkLiterActivity.this, "Unable to submit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        super.onStart();
    }
}
