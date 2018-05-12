package com.nicklasoxhammar.cartchecking.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nicklasoxhammar.cartchecking.R;
import com.nicklasoxhammar.cartchecking.Resident;
import com.nicklasoxhammar.cartchecking.ResidentAddress;

import java.util.ArrayList;

public class AddResidentActivity extends AppCompatActivity {

    DatabaseReference database;

    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText streetNameEditText;
    EditText streetNumberEditText;
    EditText apartmentNumberEditText;

    ArrayList<EditText> editTextList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resident);

        database = FirebaseDatabase.getInstance().getReference();

        addEditTexts();


    }

    public void AddResidentToDatabase(View view){

        for (EditText e : editTextList){

            if (e.getText().toString().equals("") || e.getText().toString() == null){
                Toast.makeText(this, "Please fill in the required fields!", Toast.LENGTH_SHORT).show();
            }
        }

        ResidentAddress address = new ResidentAddress(streetNumberEditText.getText().toString(), streetNameEditText.getText().toString(), apartmentNumberEditText.getText().toString());

        Resident resident = new Resident(firstNameEditText.getText().toString(), lastNameEditText.getText().toString(), address);

        try {
            String residentId = database.child("residents").push().getKey();
            database.child("residents").child(residentId).setValue(resident);
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

        streetNameEditText = findViewById(R.id.street_name_edit_text);
        editTextList.add(streetNameEditText);

        streetNumberEditText = findViewById(R.id.street_number_edit_text);
        editTextList.add(streetNumberEditText);

        apartmentNumberEditText = findViewById(R.id.apartment_number_edit_text);
    }
}
