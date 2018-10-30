package com.example.hp.milkproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WorkerProfileActivity extends Fragment {

    EditText editTextFname, editTextSname;
    Button buttonUpdate;
    FirebaseFirestore firebaseFirestore;
    String userUid;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_worker_profile, container, false);

        progressDialog = new ProgressDialog(getActivity());

        editTextFname = (EditText) rootview.findViewById(R.id.editTextFname);
        editTextSname = (EditText ) rootview.findViewById(R.id.editTextSname);
        buttonUpdate = (Button) rootview.findViewById(R.id.buttonUpdate);

        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document(userUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                editTextFname.setText(document.getString("fname"));
                editTextSname.setText(document.getString("sname"));
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextFname.getText().toString().equals("") || editTextSname.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please fill in all details", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.setMessage("Updating your account ...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Map<String, Object> map = new HashMap<>();
                    map.put("fname", editTextFname.getText().toString());
                    map.put("sname", editTextSname.getText().toString());

                    firebaseFirestore.collection("Users").document(userUid).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }else {
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });

        return rootview;
    }
}
