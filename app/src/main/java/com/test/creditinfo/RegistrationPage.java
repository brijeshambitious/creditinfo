package com.test.creditinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        //call Login with phone activity
        Button regLoginBtn = (Button) findViewById(R.id.regLoginBtn);
        regLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationPage.this , LoginViaPhone.class);
                startActivity(intent);
                finish();
            }
        });


        //send form data to registration OTP page
        final TextInputEditText phoneNumber,userName,businessName,email,password;

        userName = findViewById(R.id.userName_registration);
        businessName = findViewById(R.id.businessName_registration);
        email = findViewById(R.id.email_registration);
        phoneNumber = findViewById(R.id.phoneNumber_registration);
        password = findViewById(R.id.password_registration);
        findViewById(R.id.registrationBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = userName.getText().toString().trim();
                String businessname = businessName.getText().toString().trim();
                String number = phoneNumber.getText().toString().trim();
                String emails = email.getText().toString().trim();
                String passwordn = password.getText().toString().trim();

                if(username.isEmpty())
                {
                    userName.setError("Name is required");
                    userName.requestFocus();
                    return;
                }

                if(emails.isEmpty())
                {
                    email.setError("Email is required");
                    email.requestFocus();
                    return;
                }

                if(number.isEmpty() || number.length() < 10)
                {
                    phoneNumber.setError("Valid number is required");
                    phoneNumber.requestFocus();
                    return;
                }

                if(passwordn.isEmpty())
                {
                    password.setError("Enter Password");
                    password.requestFocus();
                    return;
                }
                if(passwordn.length() < 6)
                {
                    password.setError("Atleast 6 Characters");
                    password.requestFocus();
                    return;
                }

                String numbers = "+" + "91" + number;
                Intent intent = new Intent(RegistrationPage.this,RegistrationOTPPage.class);
                intent.putExtra("username",username);
                intent.putExtra("businessname",businessname);
                intent.putExtra("phonenumber",numbers);
                intent.putExtra("email",emails);
                intent.putExtra("password",passwordn);

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
