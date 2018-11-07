package com.example.hp.milkproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class BossProfileActivity extends Fragment {

    EditText editTextFname, editTextSname;
    Button buttonUpdate;
    ImageView imageViewProfileImage;
    FirebaseFirestore firebaseFirestore;
    String userUid;
    ProgressDialog progressDialog;
    StorageReference firebaseStorage;
    private static final int CAMERA_PERMISSION = 3;
//    private static final int CAMERA_PERMISSION = 3;
    Uri uri, downloadUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_boss_profile, container, false);

        progressDialog = new ProgressDialog(getActivity());

        editTextFname = (EditText) rootview.findViewById(R.id.editTextFname);
        editTextSname = (EditText ) rootview.findViewById(R.id.editTextSname);
        buttonUpdate = (Button) rootview.findViewById(R.id.buttonUpdate);
        imageViewProfileImage = (ImageView) rootview.findViewById(R.id.imageViewProfileImage);

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

        imageViewProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImageDialog();
            }
        });

        imageProfile();

        return rootview;
    }
    
    private void imageProfile(){
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Image").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    Toast.makeText(getActivity(), "no image", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else {
                    GetImage getImage = dataSnapshot.getValue(GetImage.class);
                    String url = getImage.getImg();
                    Glide.with(getActivity()).load(url).into(imageViewProfileImage);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    
    public static class GetImage {
        String img;

        public GetImage() {
        }

        public GetImage(String img) {
            this.img = img;
        }

        public String getImg() {
            return img;
        }
    }

    private void takeImageDialog(){
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, CAMERA_PERMISSION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_PERMISSION && resultCode == RESULT_OK){
            final Uri uri = data.getData();

            progressDialog.setMessage("Uploading Image ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Glide.with(getActivity()).load(taskSnapshot.getDownloadUrl()).into(imageViewProfileImage);
                    Map<String, String> map = new HashMap<>();
                    map.put("img", taskSnapshot.getDownloadUrl().toString());
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Image").child(FirebaseAuth.getInstance().getUid());
                    databaseReference.setValue(map);
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

