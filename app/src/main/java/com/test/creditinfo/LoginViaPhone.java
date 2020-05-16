package com.test.creditinfo;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginViaPhone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_via_phone);

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.loginPhoneToolbar);
        setSupportActionBar(toolbar);

        //call login with email activity
        Button bnLoginWithEmail = (Button) findViewById(R.id.loginWithEmail);
        bnLoginWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(LoginViaPhone.this , LoginViaEmail.class);
               startActivity(intent);
               finish();
            }
        });

        //call the registration activity
        Button phoneRegistratonBtn = (Button) findViewById(R.id.phoneRegistrationBtn);
        phoneRegistratonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginViaPhone.this , RegistrationPage.class);
                startActivity(intent);
                finish();
            }
        });


        //call PhoneOTPPage with proceed button
        final EditText loginViaPhoneEditText  = findViewById(R.id.loginViaPhoneEditText);

        Button proceedBtn = (Button) findViewById(R.id.phoneProceedBtn);
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = loginViaPhoneEditText.getText().toString().trim();

                if(number.isEmpty() || number.length() < 10 || number.length() > 10)
                {
                    loginViaPhoneEditText.setError("Enter the valid number");
                    loginViaPhoneEditText.requestFocus();
                    return;
                }

                    String numbers = "+" + "91" + number;
                    Intent intent = new Intent(LoginViaPhone.this, PhoneOTPPage.class);
                    intent.putExtra("phonenumber",numbers);

                    startActivity(intent);
                    finish();
                }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
            {
                Intent intent = new Intent(this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }
}
