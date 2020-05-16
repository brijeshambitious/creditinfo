package com.test.creditinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewCustomerPage extends AppCompatActivity {

    TextView custName,custPhoneNumber;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer_page);

        //initialize the objects
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("users/"+currentUser.getUid());

        //back button onclick listner
        ImageView backBtnAddNewCustomer = findViewById(R.id.backBtnNewCustomer);
        backBtnAddNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewCustomerPage.this,AddCustomerPage.class);
                startActivity(intent);
                finish();
            }
        });

        custName = findViewById(R.id.customer_name_add_cust);
        custPhoneNumber = findViewById(R.id.customer_phone_number_add_cust);

        //save button onclick listner
        Button addNewCustomerSaveBtn  = findViewById(R.id.addNewCustomerSaveBtn);
        addNewCustomerSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String custname = custName.getText().toString().trim();
                String custphonenumber = custPhoneNumber.getText().toString().trim();

                if(custname.isEmpty())
                {
                    custName.setError("Enter name");
                    custName.requestFocus();
                    return;
                }

                if(custphonenumber.isEmpty())
                {
                    custPhoneNumber.setError("Enter phone number");
                    custPhoneNumber.requestFocus();
                    return;
                }

                if(custphonenumber.length() > 10 || custphonenumber.length() < 10)
                {
                    custPhoneNumber.setError("Enter valid phone number");
                    custPhoneNumber.requestFocus();
                    return;
                }

                custphonenumber = "+91" + custphonenumber;

                reference.child("customers/"+custname).child("name").setValue(custname);
                reference.child("customers/"+custname).child("phonenumber").setValue(custphonenumber);

                Intent intent = new Intent(AddNewCustomerPage.this,CustomerPage.class);
                intent.putExtra("custname",custname);
                intent.putExtra("phonenumber",custphonenumber);
                startActivity(intent);
                finish();
            }
        });

    }
}