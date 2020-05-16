package com.test.creditinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddCustomerPage extends AppCompatActivity {

    private int CONTACTS_PERMISSION_CODE = 1;
    SearchView addCustomerPageSearchView;
    RecyclerView contactListRecycler;
    ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer_page);



        if(ContextCompat.checkSelfPermission(AddCustomerPage.this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED){

            //contactListRecycler method
            contactListRecycler = findViewById(R.id.contactListRecycler);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            RecyclerView.LayoutManager layoutManager= linearLayoutManager;
            contactListRecycler.setLayoutManager(layoutManager);
            adapter = new ContactAdapter(this,getContacts());
            contactListRecycler.setAdapter(adapter);

        }
        else {
            requestContactsPermission();
        }

        //search View ontextChange listner
        addCustomerPageSearchView = findViewById(R.id.addCustomerPageSearchView);

        addCustomerPageSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                contactListRecycler = findViewById(R.id.contactListRecycler);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                RecyclerView.LayoutManager layoutManager= linearLayoutManager;
                contactListRecycler.setLayoutManager(layoutManager);
                adapter = new ContactAdapter(getApplicationContext(),getContacts());
                contactListRecycler.setAdapter(adapter);

                List<ModelContacts> newList = new ArrayList<>();
                newList = getContacts();

                List<ModelContacts> updatelist = new ArrayList<>();

                for(ModelContacts contacts : newList)
                {
                    if(contacts.getName().contains(query))
                    {
                        updatelist.add(contacts);
                    }
                }
                adapter.updateList(updatelist);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                contactListRecycler = findViewById(R.id.contactListRecycler);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                RecyclerView.LayoutManager layoutManager= linearLayoutManager;
                contactListRecycler.setLayoutManager(layoutManager);
                adapter = new ContactAdapter(getApplicationContext(),getContacts());
                contactListRecycler.setAdapter(adapter);

                List<ModelContacts> newList = new ArrayList<>();
                newList = getContacts();

                List<ModelContacts> updatelist = new ArrayList<>();

                for(ModelContacts contacts : newList)
                {
                    if(contacts.getName().contains(newText))
                    {
                        updatelist.add(contacts);
                    }
                }
                adapter.updateList(updatelist);
                return true;
            }
        });

        ImageView backButton = findViewById(R.id.backButtonIcon);

        //back button onclick Listner
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCustomerPage.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //call AddNewCustomerPage activity
        CardView addNewCustomerCardView = findViewById(R.id.addNewCustomerCardView);

        addNewCustomerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCustomerPage.this , AddNewCustomerPage.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void requestContactsPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to display contacts")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddCustomerPage.this, new String[] {Manifest.permission.READ_CONTACTS},CONTACTS_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS},CONTACTS_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CONTACTS_PERMISSION_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<ModelContacts> getContacts()
    {
        //List<ModelContacts> list = new ArrayList<>();
        HashSet<ModelContacts> listT = new HashSet<>();

        Cursor cursor = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        cursor.moveToFirst();

        while(cursor.moveToNext())
        {

            listT.add(new ModelContacts(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    ,cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));

        }

        List<ModelContacts> list = new ArrayList<>(listT);

        return list;
    }

}