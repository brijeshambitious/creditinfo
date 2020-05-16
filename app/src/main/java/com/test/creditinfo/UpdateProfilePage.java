package com.test.creditinfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfilePage extends AppCompatActivity {

    private CircleImageView updateProfilePhoto;
    Uri imageUri;
    DatabaseReference reference;
    FirebaseUser currentUser;
    StorageReference mStorageReference,dStorageReference,ddStorageReference;
    StorageTask uploadtask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_page);

        mStorageReference = FirebaseStorage.getInstance().getReference("Images");


        //update data on save button click listner
        Button saveBtn = findViewById(R.id.updateProfileSaveBtn);
        final EditText userName = findViewById(R.id.update_name_edittext);
        final EditText businessName = findViewById(R.id.update_businessname_edittext);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = userName.getText().toString().trim();
                String businessname = businessName.getText().toString().trim();

                if(username.isEmpty())
                {
                    userName.setError("Enter name");
                    userName.requestFocus();
                    return;
                }

                if(uploadtask != null && uploadtask.isInProgress())
                {
                    Toast.makeText(UpdateProfilePage.this, "Upload is in progress", Toast.LENGTH_SHORT).show();
                }
                else {

                    //first delete file if available
                    dStorageReference = FirebaseStorage.getInstance().getReference();
                    ddStorageReference = dStorageReference.child("images/"+currentUser.getUid());

                    // Delete the file
                    dStorageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });



                    fileUploader();
                }

                reference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("name");
                reference.setValue(username);

                reference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("businessname");
                reference.setValue(businessname);


                Toast.makeText(UpdateProfilePage.this, "Updated successfully", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(UpdateProfilePage.this,ViewProfilePage.class);
                startActivity(intent);
                finish();
            }
        });

        //choosing photo from gallery
        updateProfilePhoto = findViewById(R.id.update_profile_photo);
        updateProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent,1);
            }
        });

        //back button onClickListner
        ImageView backBtnUpdateProfile = findViewById(R.id.backBtnUpdateProfile);
        backBtnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProfilePage.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    //choosing photo from galary
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri = data.getData();

            updateProfilePhoto.setImageURI(imageUri);

        }
    }

    private String getExtention(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }


    private void fileUploader()
    {
        StorageReference reference = mStorageReference.child(currentUser.getUid());


       uploadtask = reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(UpdateProfilePage.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(UpdateProfilePage.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
