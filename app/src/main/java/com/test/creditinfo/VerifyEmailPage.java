package com.test.creditinfo;

import  androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VerifyEmailPage extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email_page);

        String userName = getIntent().getStringExtra("username");
        String businessName = getIntent().getStringExtra("businessname");
        final String email = getIntent().getStringExtra("email");
        final String phoneNumber = getIntent().getStringExtra("phonenumber");
        final String password = getIntent().getStringExtra("password");


        final UserHelperClass helperClass = new UserHelperClass(userName,businessName,phoneNumber,email,password);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("users");

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(VerifyEmailPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(VerifyEmailPage.this, "Verification Email send", Toast.LENGTH_SHORT).show();

                                                firebaseAuth.signInWithEmailAndPassword(email, password)
                                                        .addOnCompleteListener(VerifyEmailPage.this, new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()) {

                                                                    reference.child(user.getUid()).setValue(helperClass);

                                                                        Intent intent = new Intent(VerifyEmailPage.this, RegistrationOTPPage.class);
                                                                        intent.putExtra("phonenumber",phoneNumber);
                                                                        intent.putExtra("email",email);
                                                                        intent.putExtra("password",password);
                                                                        startActivity(intent);
                                                                        finish();

                                                                    }
                                                            }
                                                        });
                                            }
                                            else {
                                                Toast.makeText(VerifyEmailPage.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {

                            Toast.makeText(VerifyEmailPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });


        //Onclick verifyEmailBtn

        Button verifyEmailBtn = findViewById(R.id.verifyEmailBtn);
        verifyEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                PackageManager managerClock = getPackageManager();
                intent = managerClock.getLaunchIntentForPackage("com.google.android.gm");
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(intent);

                finish();
            }
        });

        //login Button onclick listner
        Button loginBtn = findViewById(R.id.loginVerifyEmail);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerifyEmailPage.this,LoginViaPhone.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
