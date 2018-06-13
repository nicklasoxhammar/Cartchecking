package com.nicklasoxhammar.cartchecking.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.CrashUtils;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nicklasoxhammar.cartchecking.Adapters.ResidentsAdapter;
import com.nicklasoxhammar.cartchecking.R;
import com.nicklasoxhammar.cartchecking.Resident;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";

    private FirebaseAuth mAuth;

    String qrString;

    FirebaseUser currentUser;

    ArrayList<Resident> residents;

    String residentId;

    String streetName;

    AutoCompleteTextView streetNameAutoCompleteTextView;

    private PopupWindow pw;
    private PopupWindow backgroundPw;

    Resident resident;

    DatabaseReference database;

    String[] invalidCharacters = {".","#","$","[","]"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        residents = new ArrayList<>();

        streetNameAutoCompleteTextView = findViewById(R.id.autoCompleteStreetTextView);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
        /*TextView textView = findViewById(R.id.userTextView);
        textView.setText("You are signed in on: " + currentUser.getEmail());*/


        database = FirebaseDatabase.getInstance().getReference();


    }

    public void startBarcodeScanActivity(View view){

        Intent intent = new Intent(this, BarcodeScanActivity.class);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                residentId = data.getStringExtra("residentId");
                checkId();


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
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

        database.child("residents").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.hasChild(residentId)){
                        residentExists(snapshot.getKey());
                        return;
                    }
                }
                // residentId does not exist
                residentDoesNotExist();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }


    public void residentExists(String streetNameKey){

        Intent intent = new Intent(MainActivity.this, IdScannedActivity.class);
        intent.putExtra("residentId", residentId);
        intent.putExtra("streetName", streetNameKey);
        MainActivity.this.startActivity(intent);

    }

    public void residentDoesNotExist(){

        Toast.makeText(getApplicationContext(), "Id not found in database!", Toast.LENGTH_SHORT).show();

    }


    public void signOut(View view){

        FirebaseAuth.getInstance().signOut();

        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(myIntent);

        finish();
    }

    public void startAddResidentActivity(View view){

        Intent myIntent = new Intent(MainActivity.this, AddResidentActivity.class);
        MainActivity.this.startActivity(myIntent);

    }

    public void startEditResidentActivity(View view){

        Intent myIntent = new Intent(MainActivity.this, EditResidentActivity.class);
        MainActivity.this.startActivity(myIntent);

    }

    public void searchStreetName(View view){

        streetName = streetNameAutoCompleteTextView.getText().toString().toLowerCase();

        if(streetName.equals("")){
            Toast.makeText(this, "Please enter a street name!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < invalidCharacters.length; i++){

            if(streetName.contains(invalidCharacters[i])){
                Toast.makeText(getApplicationContext(), "Street name not in database.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        getResidentsByStreetName();

        //hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public void getResidentsByStreetName(){

        residents.clear();

        database.child("residents").child(streetName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // residentId exists

                    //resident = (Resident) dataSnapshot.getValue(Resident.class);
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        residents.add(ds.getValue(Resident.class));
                    }

                    showStreetNamePopup();

                } else {
                    // residentId does not exist
                    Toast.makeText(getApplicationContext(), "Street name not in database.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void setUpResidentsRecyclerView(View layout){
        sortResidentList();

        LinearLayoutManager mLayoutManager;
        RecyclerView residentsRecyclerView;
        ResidentsAdapter mAdapter;

        mLayoutManager = new LinearLayoutManager(this);
        residentsRecyclerView = layout.findViewById(R.id.residentsRecyclerView);
        residentsRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ResidentsAdapter(this, mLayoutManager, residents);
        residentsRecyclerView.setAdapter(mAdapter);

    }

    public void sortResidentList(){

        Collections.sort(residents, new Comparator<Resident>() {
            @Override
            public int compare(Resident r1, Resident r2) {
            return r1.getAddress().getStreetNumberInt() - r2.getAddress().getStreetNumberInt();
        }
    });

    }

    public void showStreetNamePopup(){

        try {
            //get height and width of default display
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View backgroundLayout = inflater.inflate(R.layout.dark_background_popup, (ViewGroup) findViewById(R.id.background_popup_element));

            backgroundPw = new PopupWindow(backgroundLayout, ViewGroup.LayoutParams.MATCH_PARENT,  ViewGroup.LayoutParams.MATCH_PARENT, false);
            backgroundPw.showAtLocation(backgroundLayout, Gravity.CENTER, 0, 0);

            View layout = inflater.inflate(R.layout.street_name_popup,
                    (ViewGroup) findViewById(R.id.popup_element));

            pw = new PopupWindow(layout);
            pw.setWidth((int) (width * 0.9));
            pw.setHeight((int) (height * 0.9));
            pw.setFocusable(true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

            pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundPw.dismiss();
                }
            });

            TextView streetNameTextView = layout.findViewById(R.id.street_name_textView);
            String streetNameFirstLetterCapital = streetName.substring(0, 1).toUpperCase() + streetName.substring(1);
            streetNameTextView.setText(streetNameFirstLetterCapital);

            ImageButton exitButton = layout.findViewById(R.id.exitPopupButton);
            exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pw.dismiss();
                }
            });

            setUpResidentsRecyclerView(layout);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    //use to move objects in firebase  !BE CAREFUL! accidentally deleted the whole database using this!
    /*private void moveChildren(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            System.out.println("Copy failed");
                        } else {
                            System.out.println("Success");

                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/



}
