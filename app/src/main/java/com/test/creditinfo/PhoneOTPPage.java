package com.test.creditinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneOTPPage extends AppCompatActivity {

    private String verificationID;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private EditText enterOTPEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_otppage);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        enterOTPEditText = findViewById(R.id.enterOTPEditText);

        String phoneNumber = getIntent().getStringExtra("phonenumber");

        sendVerificationCode(phoneNumber);

        //submit button onclick listner
        Button submitOTPBtn = findViewById(R.id.submitLoginOTPBtn);
        submitOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = enterOTPEditText.getText().toString().trim();

                if(code.isEmpty() || code.length() < 6)
                {
                    enterOTPEditText.setError("Enter OTP Code");
                    enterOTPEditText.requestFocus();
                    return;
                }

                verifyCode(code);
            }
        });
    }

    private void verifyCode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,code);
        signInWithCredentials(credential);
    }

    private void signInWithCredentials(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(PhoneOTPPage.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(PhoneOTPPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                120,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack

        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationID = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            if(code != null)
            {
                verifyCode(code);
            }

            if(code == null)
            {
                signInWithCredentials(phoneAuthCredential);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneOTPPage.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
