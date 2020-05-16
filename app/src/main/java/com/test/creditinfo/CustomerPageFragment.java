package com.test.creditinfo;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class CustomerPageFragment extends Fragment {

    public CustomerPageFragment() {
        // Required empty public constructor
    }

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private RecyclerView recyclerView;
    int youGaveAmount,youGotAmount,remainingAmount;
    TextView creditAmountTextView,creditStatusTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_customer_page, container, false);

        String custname = getArguments().getString("customername");

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users/"+currentUser.getUid()+"/customers/"+custname+"/Transactions");

        //get data from database
         mDatabase.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 if(!dataSnapshot.exists())
                 {
                     LinearLayout linearLayout = view.findViewById(R.id.customerPageLinearLayout);
                     linearLayout.setVisibility(View.INVISIBLE);
                     return;
                 }

                 for (DataSnapshot childSnapShot : dataSnapshot.getChildren())
                 {
                    try {

                        if(!childSnapShot.child("yougave").getValue().equals("")){
                            youGaveAmount = Integer.parseInt(childSnapShot.child("yougave").getValue().toString()) + youGaveAmount;
                        }

                        if(!childSnapShot.child("yougot").getValue().equals(""))
                        {
                            youGotAmount = Integer.parseInt(childSnapShot.child("yougot").getValue().toString()) + youGotAmount;
                        }

                    }
                    catch (Exception e)
                    {

                    }

                     if(youGaveAmount > youGotAmount) {
                         remainingAmount = youGaveAmount - youGotAmount;
                         creditAmountTextView.setText( "\u20B9" + Integer.toString(remainingAmount));
                         creditStatusTextView.setText("You will get");
                     }
                     if(youGotAmount > youGaveAmount)
                     {
                         remainingAmount = youGotAmount - youGaveAmount;
                         creditAmountTextView.setText( "\u20B9" + Integer.toString(remainingAmount));
                         creditStatusTextView.setText("You will give");
                     }
                     if(youGaveAmount == youGotAmount)
                     {
                         creditStatusTextView.setText("Setteled up!!");
                         creditAmountTextView.setText("");
                     }
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });


        recyclerView = view.findViewById(R.id.transactionListRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        creditAmountTextView = view.findViewById(R.id.credit_amount_customerFragment);
        creditStatusTextView = view.findViewById(R.id.credit_status_customerFragment);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<CustomerTransactionsList,CustomerTransactionListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CustomerTransactionsList, CustomerTransactionListViewHolder>
                (CustomerTransactionsList.class,R.layout.customer_page_entry_layout,CustomerTransactionListViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(CustomerTransactionListViewHolder customerTransactionListViewHolder, CustomerTransactionsList customerTransactionsList, int i) {
                customerTransactionListViewHolder.setDate(customerTransactionsList.getDate());
                customerTransactionListViewHolder.setYouGaveAmount(customerTransactionsList.getYougave());
                customerTransactionListViewHolder.setYouGotAmount(customerTransactionsList.getYougot());
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CustomerTransactionListViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public CustomerTransactionListViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setDate(String date)
        {
            TextView postDate = mView.findViewById(R.id.customerFragmentDateColumn);
            postDate.setText(date);
        }

        public void setYouGaveAmount(String amount)
        {
            TextView postYouGaveAmount = mView.findViewById(R.id.customerFragmentYouGave);

            if(amount.isEmpty())
            {
                postYouGaveAmount.setText("");
                return;
            }
            postYouGaveAmount.setText("\u20B9"+ amount);
        }

        public void setYouGotAmount(String amount)
        {
            TextView postYouGotAmount = mView.findViewById(R.id.customerFragmentYouGot);

            if(amount.isEmpty())
            {
                postYouGotAmount.setText("");
                return;
            }

            postYouGotAmount.setText("\u20B9"+amount);
        }
    }
}