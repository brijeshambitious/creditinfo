package com.test.creditinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfilePage extends AppCompatActivity {

    CircleImageView imageView;
    TextView namet,businessName,phoneNumber,emailt;
    DatabaseReference reference;
    FirebaseUser currentUser;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_page);

        namet = findViewById(R.id.view_profile_name);
        businessName = findViewById(R.id.view_profile_business_name);
        phoneNumber = findViewById(R.id.view_profile_phone_number);
        emailt = findViewById(R.id.view_profile_email);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();

        //getting data from firebase
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String businessname = dataSnapshot.child("businessname").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String phonenumber = dataSnapshot.child("phonenumber").getValue().toString();

                    namet.setText(name);
                    businessName.setText(businessname);
                    phoneNumber.setText(phonenumber);
                    emailt.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewProfilePage.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                namet.setText("");
                businessName.setText("");
                phoneNumber.setText("");
                emailt.setText("");
            }
        });


        //Back button click Listner
        ImageView backBtnViewProfile = findViewById(R.id.backBtnViewProfile);
        backBtnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfilePage.this , MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Update button onClick Listner
        Button viewProfileUpdateBtn = findViewById(R.id.viewProfileUpdateBtn);
        viewProfileUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfilePage.this,UpdateProfilePage.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        //get profile picture from firestorage
        imageView = findViewById(R.id.view_profile_photo);

        StorageReference storageRef = storage.getReference();

        storageRef.child("Images/"+currentUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String downloadURL = uri.toString();
                Picasso.get().load(downloadURL).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ViewProfilePage.this, exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
