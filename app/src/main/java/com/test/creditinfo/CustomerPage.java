package com.test.creditinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerPage extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseUser currentUser;
    TextView custNameTextView,phoneNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_page);

        custNameTextView = findViewById(R.id.cust_name_customer_page);
        phoneNumberTextView = findViewById(R.id.phonenumber_customer_page);

        final String custname = getIntent().getStringExtra("custname");
        custNameTextView.setText(custname);

        final String phonenumber = getIntent().getStringExtra("phonenumber");
        phoneNumberTextView.setText(phonenumber);

        //get data from firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("users/"+currentUser.getUid()+"customers/"+custname);

        //backBtnCustomerPage onclickListner
        AppCompatImageView backBtnCustomerPage = findViewById(R.id.backBtnCustomerPage);
        backBtnCustomerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerPage.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //calling a CustomerPageFragment

        Bundle bundle = new Bundle();
        bundle.putString("customername",custname);


         FragmentManager fm = getSupportFragmentManager();
        CustomerPageFragment fragment = new CustomerPageFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.customerPageFragmentLayout,fragment).commit();


        //calling a customerPageUGave Activity
        Button customerPageUGaveBtn = findViewById(R.id.customerPageUGaveBtn);
        customerPageUGaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerPage.this,CustomerYouGavePage.class);
                intent.putExtra("custname",custname);
                intent.putExtra("phonenumber",phonenumber);
                startActivity(intent);
                finish();
            }
        });

        //calling a customerPageUGot Activity
        Button customerPageUGotBtn = findViewById(R.id.customerPageUGotBtn);
        customerPageUGotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerPage.this,CustomerYouGotPage.class);
                intent.putExtra("custname",custname);
                intent.putExtra("phonenumber",phonenumber);
                startActivity(intent);
                finish();
            }
        });
    }
}