package com.test.creditinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewReportPage extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private RecyclerView recyclerView;
    int youGaveAmount,youGotAmount,remainingAmount,totalEntries;
    TextView netBalance,youGaveAmountTextView,youGotAmounttextView,uGavetextView,uGotTextView,totalEntriesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report_page);

        netBalance = findViewById(R.id.netBalance);
        totalEntriesTextView = findViewById(R.id.totalEntries);
        youGaveAmountTextView = findViewById(R.id.youGaveAmountTextView);
        youGotAmounttextView = findViewById(R.id.youGotAmountTextView);
        uGavetextView = findViewById(R.id.uGaveTextViewTransaction);
        uGotTextView = findViewById(R.id.uGotTextViewTransaction);


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users/"+currentUser.getUid()+"/alltransactions");

        //get values from firebase
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapShot : dataSnapshot.getChildren())
                {
                    totalEntries = totalEntries + 1;
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
                        netBalance.setText( "\u20B9" + Integer.toString(remainingAmount));
                        youGaveAmountTextView.setText(Integer.toString(youGaveAmount));
                        youGotAmounttextView.setText(Integer.toString(youGotAmount));
                        uGavetextView.setText(Integer.toString(youGaveAmount));
                        uGotTextView.setText(Integer.toString(youGotAmount));
                    }
                    if(youGotAmount > youGaveAmount)
                    {
                        remainingAmount = youGotAmount - youGaveAmount;
                        netBalance.setText( "\u20B9" + Integer.toString(remainingAmount));
                        youGaveAmountTextView.setText(Integer.toString(youGaveAmount));
                        youGotAmounttextView.setText(Integer.toString(youGotAmount));
                        uGavetextView.setText(Integer.toString(youGaveAmount));
                        uGotTextView.setText(Integer.toString(youGotAmount));

                    }
                    if(youGaveAmount == youGotAmount)
                    {
                        netBalance.setText( "\u20B9" + Integer.toString(remainingAmount));
                        youGaveAmountTextView.setText(Integer.toString(youGaveAmount));
                        youGotAmounttextView.setText(Integer.toString(youGotAmount));
                        uGavetextView.setText(Integer.toString(youGaveAmount));
                        uGotTextView.setText(Integer.toString(youGotAmount));

                    }
                }
                String totalE = Integer.toString(totalEntries) + " Entries";
                totalEntriesTextView.setText(totalE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        recyclerView = findViewById(R.id.viewReportListRecycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //backBtnViewReport onclick listner
        AppCompatImageView backBtnViewReport = findViewById(R.id.backBtnViewReport);
        backBtnViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewReportPage.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ViewReportList,ViewReportListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ViewReportList, ViewReportListViewHolder>
                (ViewReportList.class,R.layout.transactions_list,ViewReportListViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(ViewReportListViewHolder viewReportListViewHolder, ViewReportList viewReportList,int i) {
                    viewReportListViewHolder.setName(viewReportList.getCustomer());
                    viewReportListViewHolder.setDate(viewReportList.getDate());
                    viewReportListViewHolder.setYouGave(viewReportList.getYougave());
                    viewReportListViewHolder.setYouGot(viewReportList.getYougot());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public  static class ViewReportListViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public ViewReportListViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name)
        {
            TextView postName = mView.findViewById(R.id.transactionCustName);
            postName.setText(name);
        }

        public void setDate(String date)
        {
            TextView postDate = mView.findViewById(R.id.transactionDate);
            postDate.setText(date);
        }

        public void setYouGave(String amount)
        {
            TextView postYouGave = mView.findViewById(R.id.transactionYouGave);
            postYouGave.setText(amount);
        }

        public void setYouGot(String amount)
        {
            TextView postYouGot = mView.findViewById(R.id.transactionYouGot);
            postYouGot.setText(amount);
        }
    }
}
