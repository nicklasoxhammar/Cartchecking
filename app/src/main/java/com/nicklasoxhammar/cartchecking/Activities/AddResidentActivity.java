package com.nicklasoxhammar.cartchecking.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nicklasoxhammar.cartchecking.Adapters.StreetsAdapter;
import com.nicklasoxhammar.cartchecking.R;
import com.nicklasoxhammar.cartchecking.Resident;
import com.nicklasoxhammar.cartchecking.ResidentAddress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AddResidentActivity extends AppCompatActivity {

   /* DatabaseReference database;

    EditText firstNameEditText;
    EditText lastNameEditText;
    AutoCompleteTextView streetNameAutoText;
    EditText streetNumberEditText;
    EditText apartmentNumberEditText;

    ArrayList<String> streets;

    ArrayList<EditText> editTextList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resident);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance().getReference();

        addEditTexts();
        setupAutoComplete();


    }

    public void AddResidentToDatabase(View view){

        for (EditText e : editTextList){

            if (e.getText().toString().equals("") || e.getText().toString() == null){
                Toast.makeText(this, "Please fill in the required fields!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        ResidentAddress address = new ResidentAddress(streetNumberEditText.getText().toString(), streetNameAutoText.getText().toString(), apartmentNumberEditText.getText().toString());

        Resident resident = new Resident(firstNameEditText.getText().toString(), lastNameEditText.getText().toString(), address);

        String streetName = streetNameAutoText.getText().toString().toLowerCase();

        try {
            String residentId = database.child("residents").child(streetName).push().getKey();
            resident.setID(residentId);
            database.child("residents").child(streetName).child(residentId).setValue(resident);
            Toast.makeText(this, "Resident successfully added to database!", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "ResidentId: " + residentId);

        }catch (Exception e){
            Log.d("Exception", "Report: " + e);

            Toast.makeText(this, "Addition failed, please try again!", Toast.LENGTH_SHORT).show();
        }

        //Start MainActivity
        Intent myIntent = new Intent(AddResidentActivity.this, MainActivity.class);
        AddResidentActivity.this.startActivity(myIntent);

        finish();

    }

    public void addEditTexts(){

        editTextList = new ArrayList<>();

        firstNameEditText = findViewById(R.id.first_name_edit_text);
        editTextList.add(firstNameEditText);

        lastNameEditText = findViewById(R.id.last_name_edit_text);
        editTextList.add(lastNameEditText);

        streetNameAutoText = findViewById(R.id.street_name_auto_text);
        editTextList.add(streetNameAutoText);

        streetNumberEditText = findViewById(R.id.street_number_edit_text);
        editTextList.add(streetNumberEditText);

        apartmentNumberEditText = findViewById(R.id.apartment_number_edit_text);
    }

    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setupAutoComplete(){

        streets = new ArrayList<>();

        database.child("residents").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    streets.add(snapshot.getKey());
                }

                //sort alphabetically
                Collections.sort(streets, new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        return s1.compareTo(s2);
                    }
                });

                //Add streets to autocomplete textview
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_dropdown_item_1line, streets);
                streetNameAutoText.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }*/
}
