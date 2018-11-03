package com.nicklasoxhammar.cartchecking.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nicklasoxhammar.cartchecking.CartCheck;
import com.nicklasoxhammar.cartchecking.R;
import com.nicklasoxhammar.cartchecking.Resident;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class IdScannedActivity extends AppCompatActivity {

    ArrayList<CheckBox> nonRecyclableCheckBoxList;
    ArrayList<CheckBox> cartCheckBoxList;

    CheckBox correctlyRecycledCheckBox;
    CheckBox cartNotSetOutCheckBox;
    CheckBox onlyTrashSetOutCheckBox;

    TextView textView;
    EditText commentTextView;

    DatabaseReference database;

    String residentId;
    String streetNameKey;
    Resident resident;

    //String route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_scanned);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nonRecyclableCheckBoxList = new ArrayList<>();
        cartCheckBoxList = new ArrayList<>();
        setupCheckBoxList();

        database = FirebaseDatabase.getInstance().getReference();

        textView = findViewById(R.id.id_scanned_text_view);
        commentTextView = findViewById(R.id.comment_text_view);

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            Log.d("TAG", "onCreate: no extras=!");
        } else {
            residentId = extras.getString("residentId");
            streetNameKey = extras.getString("streetName");
            /*route = extras.getString("routeString");
            getResidentFromDatabase();*/

            getResident();
        }

    }

    private void getResident() {

        resident = MainActivity.route.get(streetNameKey).get(residentId);

        textView.setText("This cart belongs to " + resident.getLastName() + " at " + resident.getStreetNumber() + " " + resident.getStreetName());

    }

   /* public void getResidentFromDatabase() {

        database.child(route).child(streetNameKey).child(residentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resident = (Resident) dataSnapshot.getValue(Resident.class);

                textView.setText("This cart belongs to " + resident.getLastName() + " at " + resident.getStreetNumber() + " " + resident.getStreetName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }*/


    public void Report(View view) {

        String setOut = "1";
        String correctlyRecycled = "1";

        Boolean isSomethingChecked = false;

        ArrayList<String> nonRecyclables = new ArrayList<>();

        for (CheckBox c : nonRecyclableCheckBoxList) {
            if (c.isChecked()) {
                nonRecyclables.add(c.getText().toString());
            }
        }


        if (correctlyRecycledCheckBox.isChecked()) {
            correctlyRecycled = "0";
            isSomethingChecked = true;
        } else if (cartNotSetOutCheckBox.isChecked()) {
            setOut = "0";
            isSomethingChecked = true;
        } else if (onlyTrashSetOutCheckBox.isChecked()) {
            setOut = "3";
            isSomethingChecked = true;
        }

        if (!isSomethingChecked) {
            Toast.makeText(this, "Please use the first three checkboxes!", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (String s : nonRecyclables) {
            sb.append(s);
            sb.append(", ");
        }
        String nonRecyclablesString = sb.toString();

        String notes = commentTextView.getText().toString() + "  " + nonRecyclablesString;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM", Locale.US);
        String strDate = dateFormat.format((Calendar.getInstance().getTime()));

        CartCheck cartCheck = new CartCheck(strDate, notes, correctlyRecycled, setOut);


        try {
            MainActivity.route.get(streetNameKey).get(residentId).alreadyChecked = true;
            MainActivity.residentsAdapter.notifyDataSetChanged();
            database.child(MainActivity.routeString).child(streetNameKey).child(residentId).child("cartChecks").push().setValue(cartCheck);
            Toast.makeText(getBaseContext(), "Report successful, it will be added to database when you have an internet connection!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.d("Exception", "Report: " + e);

            Toast.makeText(this, "Report failed, please try again!", Toast.LENGTH_SHORT).show();
        }

        finish();

    }

    public void setupCheckBoxList() {

        //Add the three first checkboxes to cartCheckBoxList
        correctlyRecycledCheckBox = findViewById(R.id.correctlyRecycledCheckBox);
        cartCheckBoxList.add(correctlyRecycledCheckBox);

        cartNotSetOutCheckBox = findViewById(R.id.cartsNotSetOutCheckBox);
        cartCheckBoxList.add(cartNotSetOutCheckBox);

        onlyTrashSetOutCheckBox = findViewById(R.id.onlyTrashSetOutCheckBox);
        cartCheckBoxList.add(onlyTrashSetOutCheckBox);


        //Add the rest to nonRecyclableCheckBoxList
        CheckBox garbageCheckBox = findViewById(R.id.garbageCheckBox);
        nonRecyclableCheckBoxList.add(garbageCheckBox);

        CheckBox plasticBagCheckBox = findViewById(R.id.plasticBagCheckBox);
        nonRecyclableCheckBoxList.add(plasticBagCheckBox);

        CheckBox foodCheckBox = findViewById(R.id.foodCheckBox);
        nonRecyclableCheckBoxList.add(foodCheckBox);

        CheckBox clothingCheckBox = findViewById(R.id.clothingCheckBox);
        nonRecyclableCheckBoxList.add(clothingCheckBox);

        CheckBox tanglerCheckBox = findViewById(R.id.tanglerCheckBox);
        nonRecyclableCheckBoxList.add(tanglerCheckBox);

        CheckBox bigItemCheckBox = findViewById(R.id.bigItemCheckBox);
        nonRecyclableCheckBoxList.add(bigItemCheckBox);

        CheckBox tissuesCheckBox = findViewById(R.id.tissuesCheckBox);
        nonRecyclableCheckBoxList.add(tissuesCheckBox);

        CheckBox styrofoamCheckBox = findViewById(R.id.styrofoamCheckBox);
        nonRecyclableCheckBoxList.add(styrofoamCheckBox);

        CheckBox strawsCheckBox = findViewById(R.id.strawsCheckBox);
        nonRecyclableCheckBoxList.add(strawsCheckBox);

        CheckBox cleanButCheckBox = findViewById(R.id.cleanButCheckBox);
        nonRecyclableCheckBoxList.add(cleanButCheckBox);

        CheckBox recyclablesInTrashCheckBox = findViewById(R.id.recyclablesInTrashCheckBox);
        nonRecyclableCheckBoxList.add(recyclablesInTrashCheckBox);


        //Makes sure that you can only check one of the cartCheckBoxes at once
        for (CheckBox c : cartCheckBoxList) {
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (CheckBox b : cartCheckBoxList) {
                        if (b.getId() != view.getId()) {
                            b.setChecked(false);
                        }
                    }
                }
            });
        }
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
