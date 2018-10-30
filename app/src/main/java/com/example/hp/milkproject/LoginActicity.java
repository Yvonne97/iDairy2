package com.example.hp.milkproject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActicity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    Button buttonLogin, buttonSignUp;
    String mainEmail, mainPassword;
    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;
    Task<DocumentSnapshot> documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acticity);

//        INITIALIZE PROGRESS DIALOG
        progressDialog = new ProgressDialog(this);

//        LOG IN USER IF HE/SHE HAS PREVIOUSLY LOGGED IN
        autoLoginUser();

//      INITIALIZATION
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(LoginActicity.this);
                dialog.setContentView(R.layout.check_worker_signup_layout);
                //dialog.setTitle("Add New Worker");
                dialog.setCancelable(false);

                final EditText editTextAddWorker = (EditText) dialog.findViewById(R.id.editTextAddWorker);
                Button buttonAddWorker = (Button) dialog.findViewById(R.id.buttonAdd);
                Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                buttonAddWorker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editTextAddWorker.getText().toString().equals("")){
                            Toast.makeText(LoginActicity.this, "Please add the email address", Toast.LENGTH_LONG).show();
                        }else {
                            final ProgressDialog progressDialog = new ProgressDialog(LoginActicity.this);
                            progressDialog.setMessage("Checking if account exists");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            final String email = editTextAddWorker.getText().toString();
                            FirebaseFirestore firebaseFirestore  = FirebaseFirestore.getInstance();
                            firebaseFirestore.collection("Added Workers").document(email).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot document = task.getResult();
                                            if (!task.getResult().exists()){
                                                Toast.makeText(LoginActicity.this, "Sorry you are not allowed to access this application, contact your employer", Toast.LENGTH_LONG).show();
                                                progressDialog.dismiss();
                                                dialog.dismiss();
                                            }else {
                                                dialog.dismiss();
                                                Intent intent = new Intent(LoginActicity.this, SignUpWorkerActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("email", email);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                                finish();
                                            }
                                            //Toast.makeText(LoginActicity.this, document.getString("email"), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

                dialog.show();
            }
        });
        
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainEmail = editTextEmail.getText().toString();
                mainPassword = editTextPassword.getText().toString();

                if (mainEmail.equals("") || mainPassword.equals("")){
                    Toast.makeText(LoginActicity.this, "Please fill in all details to login", Toast.LENGTH_SHORT).show();
                }else {
                    loginUser(mainEmail, mainPassword);
                }
            }
        });
    }

    private void loginUser(String email, String password){
        progressDialog.setMessage("Logging you in ....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActicity.this, "Unable to login", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.dismiss();
                    String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    documentReference = firebaseFirestore.collection("Users").document(userUid)
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()){
                                        if (document.get("role").equals("Boss")){
                                            //                    LOGIN USER TO BOSS PAGE
                                            Intent intent = new Intent(LoginActicity.this, BossTabActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            //                    LOGIN USER TO BOSS PAGE
                                            Intent intent = new Intent(LoginActicity.this, WorkerTabActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }else {
                                            Toast.makeText(LoginActicity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void autoLoginUser(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            //Toast.makeText(LoginActicity.this, "Unable to login", Toast.LENGTH_SHORT).show();
            documentReference = firebaseFirestore.collection("Users").document(userUid)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                if (document.get("role").equals("Boss")){
                                    //                    LOGIN USER TO BOSS PAGE
                                    Intent intent = new Intent(LoginActicity.this, BossTabActivity.class);
                                    startActivity(intent);
                                    finish();
                                    progressDialog.dismiss();
                                }else {
                                    //                    LOGIN USER TO WORKER PAGE
                                    Intent intent = new Intent(LoginActicity.this, WorkerTabActivity.class);
                                    startActivity(intent);
                                    finish();
                                    progressDialog.dismiss();
                                }
                            }else {
                                Toast.makeText(LoginActicity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            progressDialog.dismiss();
        }
    }
}
