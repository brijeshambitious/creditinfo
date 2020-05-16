package com.test.creditinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
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

public class CustomerYouGotPage extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference,referenceT;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_you_got_page);

        final String custname = getIntent().getStringExtra("custname");
        final String phonenumber = getIntent().getStringExtra("phonenumber");

        //initialize the firebase objects
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("users/"+currentUser.getUid()+"/customers/"+custname);
        referenceT = firebaseDatabase.getReference("users/"+currentUser.getUid()+"/alltransactions");

        final TextView showAmount = findViewById(R.id.youGotAmount);
        final TextInputEditText enterAmountEditText = findViewById(R.id.enterAmountUGotPage);

        TextView receiverNameTextView = findViewById(R.id.senderName);
        receiverNameTextView.setText(custname);

        enterAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showAmount.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0)
                {
                    showAmount.setText("0");
                }
            }
        });

        //back Button on click listner
        AppCompatImageView backBtnYouGotPage = findViewById(R.id.backBtnYouGotPage);
        backBtnYouGotPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerYouGotPage.this,CustomerPage.class);
                intent.putExtra("custname",custname);
                intent.putExtra("phonenumber",phonenumber);
                startActivity(intent);
                finish();
            }
        });

        //youGotSaveBtn onClickListner
        Button youGotSaveBtn  = findViewById(R.id.youGotSaveBtn);
        youGotSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount = enterAmountEditText.getText().toString().trim();
                String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
                String milis = Long.toString(System.currentTimeMillis());

                if(amount.isEmpty())
                {
                    enterAmountEditText.setError("Enter amount");
                    enterAmountEditText.requestFocus();
                    return;
                }

                reference.child("Transactions").child(milis).child("yougot").setValue(amount);
                reference.child("Transactions").child(milis).child("yougave").setValue("");
                reference.child("Transactions").child(milis).child("date").setValue(date);

                referenceT.child(milis).child("customer").setValue(custname);
                referenceT.child(milis).child("date").setValue(date);
                referenceT.child(milis).child("yougot").setValue(amount);
                referenceT.child(milis).child("yougave").setValue("");

                Intent intent = new Intent(CustomerYouGotPage.this,CustomerPage.class);
                intent.putExtra("custname",custname);
                intent.putExtra("phonenumber",phonenumber);
                startActivity(intent);
                finish();
            }
        });
    }
}