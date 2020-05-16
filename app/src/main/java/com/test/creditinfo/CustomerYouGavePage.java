package com.test.creditinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomerYouGavePage extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference,referenceT;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_you_gave_page);

        final String custname = getIntent().getStringExtra("custname");
        final String phonenumber = getIntent().getStringExtra("phonenumber");

        //initialize the firebase objects
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("users/"+currentUser.getUid()+"/customers/"+custname);
        referenceT = firebaseDatabase.getReference("users/"+currentUser.getUid()+"/alltransactions");

        final TextView showAmountTextView = findViewById(R.id.youGaveAmount);
        TextView receiverNameTextView = findViewById(R.id.receiverName);
        receiverNameTextView.setText(custname);

        final TextInputEditText enterAmount = findViewById(R.id.enterAmountUGavePage);
        enterAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    showAmountTextView.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0)
                {
                    showAmountTextView.setText("0");
                }
            }
        });

        //back Button on click listner
        AppCompatImageView backBtnYouGavePage = findViewById(R.id.backBtnYouGavePage);
        backBtnYouGavePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerYouGavePage.this,CustomerPage.class);
                intent.putExtra("custname",custname);
                intent.putExtra("phonenumber",phonenumber);
                startActivity(intent);
                finish();
            }
        });

        //youGaveSaveBtn onClickListner
        Button youGaveSaveBtn  = findViewById(R.id.youGaveSaveBtn);
        youGaveSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount = enterAmount.getText().toString().trim();
                String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
                String milis = Long.toString(System.currentTimeMillis());

                if(amount.isEmpty())
                {
                    enterAmount.setError("Enter amount");
                    enterAmount.requestFocus();
                    return;
                }

                reference.child("Transactions").child(milis).child("yougave").setValue(amount);
                reference.child("Transactions").child(milis).child("yougot").setValue("");
                reference.child("Transactions").child(milis).child("date").setValue(date);

                referenceT.child(milis).child("customer").setValue(custname);
                referenceT.child(milis).child("date").setValue(date);
                referenceT.child(milis).child("yougave").setValue(amount);
                referenceT.child(milis).child("yougot").setValue("");

                Intent intent = new Intent(CustomerYouGavePage.this,CustomerPage.class);
                intent.putExtra("custname",custname);
                intent.putExtra("phonenumber",phonenumber);
                startActivity(intent);
                finish();
            }
        });
    }
}