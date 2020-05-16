package com.test.creditinfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class MoreFragment extends Fragment {

    CircleImageView imageView;
    TextView userName,businessName;
    DatabaseReference reference;
    FirebaseUser currentUser;
    FirebaseStorage storage;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_more,container,false);

        CardView settingsCardView = v.findViewById(R.id.settingsCardView);
        final CardView subSettingCardView = v.findViewById(R.id.subSettingCardView);
        final ImageView settingDirectionIcon = v.findViewById(R.id.settingDirectionIcon);
        final CardView aboutAppCardView = v.findViewById(R.id.aboutAppcardView);
        final CardView viewProfileCardView = v.findViewById(R.id.userProfile);
        final RelativeLayout.LayoutParams aboutParams = (RelativeLayout.LayoutParams) aboutAppCardView.getLayoutParams();
        final RelativeLayout.LayoutParams subSettingsParam = (RelativeLayout.LayoutParams) subSettingCardView.getLayoutParams();

        storage = FirebaseStorage.getInstance();
        imageView = v.findViewById(R.id.customer_photo_more);

        //get data from firebase

        userName = v.findViewById(R.id.user_name_more);
        businessName = v.findViewById(R.id.business_name_more);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

            reference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        String name = dataSnapshot.child("name").getValue().toString();
                        String businessname = dataSnapshot.child("businessname").getValue().toString();

                        userName.setText(name);
                        businessName.setText(businessname);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    userName.setText("");
                    businessName.setText("");
                }
            });

        settingsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subSettingCardView.getVisibility() == View.INVISIBLE) {
                    subSettingCardView.setVisibility(View.VISIBLE);
                    settingDirectionIcon.setImageResource(R.drawable.arrow_down_settings);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW,R.id.userProfile);
                    aboutAppCardView.performClick();
                }
                else
                {
                    subSettingCardView.setVisibility(View.INVISIBLE);
                    settingDirectionIcon.setImageResource(R.drawable.arrow_right_settings);
                    aboutParams.addRule(RelativeLayout.BELOW,R.id.subSettingCardView);
                    aboutAppCardView.performClick();
                }
            }
        });


        aboutAppCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subSettingCardView.getVisibility() == View.VISIBLE )
                {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
                    params.addRule(RelativeLayout.BELOW , R.id.subSettingCardView);
                    //params.setMarginStart(60);
                    params.setMargins(20,20,20,0);
                    v.setLayoutParams(params);
                }
                if(subSettingCardView.getVisibility() == View.INVISIBLE )
                {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
                    params.addRule(RelativeLayout.BELOW , R.id.settingsCardView);
                    //params.setMarginStart(60);
                    params.setMargins(20,20,20,0);
                    v.setLayoutParams(params);
                }
            }
        });


        //calling ViewProfilePage activity
        viewProfileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , ViewProfilePage.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        //call update Button on click listner
        Button updateBtnMoreFragment = v.findViewById(R.id.updateBtnMoreFragment);
        updateBtnMoreFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),UpdateProfilePage.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //aboutAppIcon onclick listner method
        final CardView subAboutAppCardView = v.findViewById(R.id.subAboutAppCardView);
        ImageView aboutAppIcon = v.findViewById(R.id.aboutAppIcon);
        aboutAppIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subAboutAppCardView.getVisibility() == View.INVISIBLE) {
                    subAboutAppCardView.setVisibility(View.VISIBLE);
                }
                else
                {
                    subAboutAppCardView.setVisibility(View.INVISIBLE);
                }
            }
        });

        //DeleteData text View alert dialog
        final AlertDialog.Builder builder;
        TextView deleteDataTextView = v.findViewById(R.id.deleteDataTextView);

        builder = new AlertDialog.Builder(getContext());

        deleteDataTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setMessage("Deleting the data will remove all the transactions and customers").setTitle("Delete Data")
                        .setCancelable(false)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              DatabaseReference  dReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()+"/customers");
                              dReference.removeValue();

                                DatabaseReference  dTReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()+"/alltransactions");
                                dTReference.removeValue();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Delete Data");
                    alert.show();
            }
        });


        //logout TextView alert dialog display on click
        TextView logoutTextView = v.findViewById(R.id.logoutTextView);

        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Do you want to logout ?").setTitle("Logout")
                       .setCancelable(false)
                       .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               //logout the user
                               FirebaseAuth.getInstance().signOut();

                               Intent intent = new Intent(getContext(),LoginViaPhone.class);
                               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                               startActivity(intent);
                           }
                       })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Logout");
                alert.show();

            }
        });


        //Delete Account alert dialog display on click
        TextView deleteAccountTextView = v.findViewById(R.id.deleteAccountTextView);

        deleteAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Do you want to delete your account").setTitle("Delete Account")
                        .setCancelable(false)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                     //Delete all data related to user
                                    DatabaseReference  dreference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
                                    dreference.removeValue();

                                //Delete userAccount
                                currentUser.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(getContext(),LoginViaPhone.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                }
                                                else
                                                {
                                                    Toast.makeText(getContext(),task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                       dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Delete Account");
                alert.show();
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        //get profile picture from firestorage
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
            }
        });


    }
}
