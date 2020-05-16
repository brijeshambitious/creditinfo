package com.test.creditinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private DatabaseReference mDatabase,mDatabase2;
    private FirebaseUser currentUser;
    private RecyclerView recyclerView;
    int youGaveAmount,youGotAmount,remainingAmount=0,uGivePreAmount=0,uGetPreAmount=0;
    TextView uWillGiveTextView,uWillGetTextView;
    SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users/"+currentUser.getUid()+"/customers");
       View view = inflater.inflate(R.layout.fragment_home,container,false);

       //searchView implementation
       searchView = view.findViewById(R.id.searchCustomer);
       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               firebaseSerach(query);
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               firebaseSerach(newText);
               return false;
           }
       });


       uWillGiveTextView = view.findViewById(R.id.uWillGiveTextView);
       uWillGetTextView = view.findViewById(R.id.uWillGetTextView);

       recyclerView = view.findViewById(R.id.customerListRecycler);
       recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //ViewReport onclick listner
        Button viewReportBtn = view.findViewById(R.id.viewReportBtn);
        viewReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ViewReportPage.class);
                startActivity(intent);
            }
        });


        return view;

    }

    private void firebaseSerach(String searchText)
    {
        Query firebaseSearchQuery = mDatabase.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<CustomerList,CustomerListViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<CustomerList, CustomerListViewHolder>
                        (CustomerList.class,R.layout.customer_list_view,CustomerListViewHolder.class,firebaseSearchQuery) {
                    @Override
                    protected void populateViewHolder(CustomerListViewHolder customerListViewHolder, final CustomerList customerList, int i) {

                        customerListViewHolder.setName(customerList.getName());

                        customerListViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final String custname = customerList.getName();
                                String phonenumber = customerList.getPhonenumber();

                                Intent intent = new Intent(getContext(),CustomerPage.class);
                                intent.putExtra("custname",custname);
                                intent.putExtra("phonenumber",phonenumber);
                                startActivity(intent);
                            }
                        });

                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        remainingAmount = 0;
        youGaveAmount = 0;
        youGotAmount = 0;
        uGetPreAmount = 0;
        uGivePreAmount = 0;

        FirebaseRecyclerAdapter<CustomerList,CustomerListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CustomerList, CustomerListViewHolder>
                (CustomerList.class,R.layout.customer_list_view,CustomerListViewHolder.class,mDatabase) {

            private List<CustomerList> exampleList;
            private  List<CustomerList> exampleListFull;

            @Override
            protected void populateViewHolder(final CustomerListViewHolder customerListViewHolder, final CustomerList customerList, final int i) {
                customerListViewHolder.setName(customerList.getName());
                final String custName = customerList.getName();

                customerListViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String custname = customerList.getName();
                        String phonenumber = customerList.getPhonenumber();

                        Intent intent = new Intent(getContext(),CustomerPage.class);
                        intent.putExtra("custname",custname);
                        intent.putExtra("phonenumber",phonenumber);
                        startActivity(intent);
                    }
                });

                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users/"+currentUser.getUid()+"/customers/"+custName+"/Transactions");


                //get data from database
                mDatabase2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                                if(childSnapShot.exists()) {

                                    if (!(childSnapShot.child("yougave").getValue() == null)) {

                                        if(childSnapShot.child("yougave").getValue().toString().length() > 0) {
                                            youGaveAmount = Integer.parseInt(childSnapShot.child("yougave").getValue().toString()) + youGaveAmount;
                                        }
                                        }

                                    if (!(childSnapShot.child("yougot").getValue() == null)) {
                                        if (childSnapShot.child("yougot").getValue().toString().length() > 0) {
                                            youGotAmount = Integer.parseInt(childSnapShot.child("yougot").getValue().toString()) + youGotAmount;
                                        }
                                    }
                                }
                            }
                            if (youGaveAmount > youGotAmount) {
                                remainingAmount = youGaveAmount - youGotAmount;
                                customerListViewHolder.setAmount("\u20B9" + Integer.toString(remainingAmount));
                                customerListViewHolder.setCreditStatus("You will get");
                            }
                            if (youGotAmount > youGaveAmount) {
                                remainingAmount = youGotAmount - youGaveAmount;
                                customerListViewHolder.setAmount("\u20B9" + Integer.toString(remainingAmount));
                                customerListViewHolder.setCreditStatus("You will give");

                            }
                            if (youGaveAmount == youGotAmount) {
                                customerListViewHolder.setAmount("");
                                customerListViewHolder.setCreditStatus("Setteled Up");

                            }

                            if (customerListViewHolder.getCreditStatus() == "You will give") {
                                setUGiveCreditAmount(remainingAmount);
                                remainingAmount = 0;
                                youGaveAmount = 0;
                                youGotAmount = 0;
                            }

                            if (customerListViewHolder.getCreditStatus() == "You will get") {
                                setUGetCreditAmount(remainingAmount);
                                remainingAmount = 0;
                                youGaveAmount = 0;
                                youGotAmount = 0;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static  class CustomerListViewHolder extends RecyclerView.ViewHolder
    {
            View mView;
            public CustomerListViewHolder(View itemView)
            {
                super(itemView);
                mView = itemView;
            }

            public void setName(String title)
            {
                TextView postName = mView.findViewById(R.id.customerName);
                postName.setText(title);
            }

            public void setAmount(String amount)
            {
                TextView postAmount = mView.findViewById(R.id.customer_amount);
                postAmount.setText(amount);
            }

            public void setCreditStatus(String status)
            {
                TextView postStatus = mView.findViewById(R.id.credit_status);
                postStatus.setText(status);
            }

            public String getCreditStatus()
            {
                TextView postStatus = mView.findViewById(R.id.credit_status);
                return postStatus.getText().toString();
            }
    }

    public void setUGiveCreditAmount(int amount)
    {
        uGivePreAmount = uGivePreAmount + amount;

        uWillGiveTextView.setText("\u20B9" + Integer.toString(uGivePreAmount));
    }

    public void setUGetCreditAmount(int amount)
    {
        uGetPreAmount  = uGetPreAmount + amount;
        uWillGetTextView.setText("\u20B9" + Integer.toString(uGetPreAmount));
    }
}