package com.nicklasoxhammar.cartchecking.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nicklasoxhammar.cartchecking.R;
import com.nicklasoxhammar.cartchecking.Resident;
import com.nicklasoxhammar.cartchecking.ResidentAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditResidentActivity extends AppCompatActivity {

    DatabaseReference database;

    String residentId;
    Resident resident;

    RelativeLayout searchLayout;
    RelativeLayout editLayout;

    EditText searchIdEditText;

    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText streetNameEditText;
    EditText streetNumberEditText;
    EditText apartmentNumberEditText;

    String[] invalidCharacters = {".","#","$","[","]"};

    ArrayList<EditText> editTextList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_resident);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchIdEditText = findViewById(R.id.searchIdEditText);

        searchLayout = findViewById(R.id.searchLayout);
        editLayout = findViewById(R.id.editLayout);

        database = FirebaseDatabase.getInstance().getReference();

    }

    public void searchForResident(View view){

        residentId = searchIdEditText.getText().toString();

        checkId();

    }



    public void addResidentChangesToDatabase(View view){

        resident.setFirstName(firstNameEditText.getText().toString());
        resident.setLastName(lastNameEditText.getText().toString());
        resident.setAddress(new ResidentAddress(streetNumberEditText.getText().toString(), streetNameEditText.getText().toString(), apartmentNumberEditText.getText().toString()));

        try {
            DatabaseReference residentRef = database.child("residents").child(residentId);
            Map<String, Object> residentUpdates = new HashMap<>();
            residentUpdates.put("firstName", resident.getFirstName());
            residentUpdates.put("lastName", resident.getLastName());
            residentUpdates.put("address", resident.getAddress());

            database.child("residents").child(residentId).updateChildren(residentUpdates);

            Toast.makeText(this, "Update successfully added to database!", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Log.d("Exception", "Report: " + e);

            Toast.makeText(this, "Update failed, please try again!", Toast.LENGTH_SHORT).show();
        }

        //Start MainActivity
        Intent myIntent = new Intent(EditResidentActivity.this, MainActivity.class);
        EditResidentActivity.this.startActivity(myIntent);

        finish();

    }

    public void setupEditTexts(){

        editTextList = new ArrayList<>();

        firstNameEditText = findViewById(R.id.edit_first_name_edit_text);
        firstNameEditText.setText(resident.getFirstName());
        editTextList.add(firstNameEditText);

        lastNameEditText = findViewById(R.id.edit_last_name_edit_text);
        lastNameEditText.setText(resident.getLastName());
        editTextList.add(lastNameEditText);

        streetNameEditText = findViewById(R.id.edit_street_name_edit_text);
        streetNameEditText.setText(resident.getAddress().getStreetName());
        editTextList.add(streetNameEditText);

        streetNumberEditText = findViewById(R.id.edit_street_number_edit_text);
        streetNumberEditText.setText(resident.getAddress().getStreetNumber());
        editTextList.add(streetNumberEditText);

        apartmentNumberEditText = findViewById(R.id.edit_apartment_number_edit_text);
        apartmentNumberEditText.setText(resident.getAddress().getApartmentNumber());
    }

    public void checkId(){

        if(residentId.equals("")){
            residentDoesNotExist();
            return;
        }

        for (int i = 0; i < invalidCharacters.length; i++){

            if(residentId.contains(invalidCharacters[i])){
                residentDoesNotExist();
                return;
            }
        }

        database.child("residents").child(residentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // residentId exists
                    residentExists();

                } else {
                    // residentId does not exist
                    residentDoesNotExist();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void residentExists(){

        getResidentFromDatabase();

        searchLayout.setVisibility(View.GONE);
        editLayout.setVisibility(View.VISIBLE);

    }

    public void residentDoesNotExist(){

        Toast.makeText(getApplicationContext(), "Id not found in database!", Toast.LENGTH_SHORT).show();

    }

    public void getResidentFromDatabase() {

        database.child("residents").child(residentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resident = (Resident) dataSnapshot.getValue(Resident.class);
                setupEditTexts();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
}
