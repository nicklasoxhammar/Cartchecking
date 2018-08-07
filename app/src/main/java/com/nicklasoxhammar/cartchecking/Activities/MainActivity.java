package com.nicklasoxhammar.cartchecking.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nicklasoxhammar.cartchecking.Adapters.ResidentsAdapter;
import com.nicklasoxhammar.cartchecking.Adapters.RoutesAdapter;
import com.nicklasoxhammar.cartchecking.Adapters.StreetsAdapter;
import com.nicklasoxhammar.cartchecking.R;
import com.nicklasoxhammar.cartchecking.Resident;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";

    private Menu menu;

    private FirebaseAuth mAuth;

    String qrString;

    RecyclerView streetsRecyclerView;

    FirebaseUser currentUser;

    String route;

    ProgressBar mProgressView;

    ArrayList<String> streets;
    ArrayList<String> routes;

    ArrayList<Resident> residents;

    String residentId;

    Toolbar toolbar;

    Boolean popupOpen = false;


    String streetName;

    AutoCompleteTextView streetNameAutoCompleteTextView;

    private PopupWindow pw;
    private PopupWindow backgroundPw;

    Resident resident;

    DatabaseReference database;

    String[] invalidCharacters = {".","#","$","[","]"};

    public void closePopup(){
        pw.dismiss();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressView = findViewById(R.id.search_progress);

        residents = new ArrayList<>();

        streetNameAutoCompleteTextView = findViewById(R.id.autoCompleteStreetTextView);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
        /*TextView textView = findViewById(R.id.userTextView);
        textView.setText("You are signed in on: " + currentUser.getEmail());*/


        database = FirebaseDatabase.getInstance().getReference();

        //setupStreetsRecyclerView();

        makeActionOverflowMenuShown();
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        //get routes and open choose route window
        getRoutesFromDatabase();

      
    }

    public void setRoute(String route){
        this.route = route;
        toolbar.setTitle(route);

        setupStreetsRecyclerView();
    }


    public void chooseRoute(){

        if(popupOpen){return;}

        popupOpen = true;

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

            final View layout = inflater.inflate(R.layout.street_name_popup,
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
                    popupOpen = false;
                    findViewById(R.id.searchButton).setClickable(true);
                }
            });

            TextView streetNameTextView = layout.findViewById(R.id.street_name_textView);
            streetNameTextView.setText("Choose Route");

            ImageButton exitButton = layout.findViewById(R.id.exitPopupButton);
            exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pw.dismiss();
                }
            });

            setUpPopupRecyclerView(layout);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_route:
                chooseRoute();
                return true;

            case R.id.action_scan:
                startBarcodeScanActivity();
                return true;

            case R.id.action_signOut:
                signOut();
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void makeActionOverflowMenuShown() {
        //devices with hardware menu button (e.g. Samsung Note) don't show action overflow menu
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void startBarcodeScanActivity(){

        if(route == null || route == ""){
            Toast.makeText(this, "Please choose a route!", Toast.LENGTH_SHORT).show();
            return;
        }

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

        database.child(route).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.hasChild(residentId)){
                        startIdScannedActivity(snapshot.getKey(), residentId);
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


    public void startIdScannedActivity(String streetNameKey, String residentId){

        Intent intent = new Intent(MainActivity.this, IdScannedActivity.class);
        intent.putExtra("residentId", residentId);
        intent.putExtra("streetName", streetNameKey);
        intent.putExtra("route", route);
        MainActivity.this.startActivity(intent);

    }

    public void residentDoesNotExist(){

        Toast.makeText(getApplicationContext(), "Id not found in database!", Toast.LENGTH_SHORT).show();

    }


    public void signOut(){

        FirebaseAuth.getInstance().signOut();

        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(myIntent);

        finish();
    }

   /* public void startAddResidentActivity(View view){

        Intent myIntent = new Intent(MainActivity.this, AddResidentActivity.class);
        MainActivity.this.startActivity(myIntent);

    }

    public void startEditResidentActivity(View view){

        Intent myIntent = new Intent(MainActivity.this, EditResidentActivity.class);
        MainActivity.this.startActivity(myIntent);

    }*/

    public void searchStreetName(View view){

        view.setClickable(false);

        streetName = streetNameAutoCompleteTextView.getText().toString().toUpperCase();

        if(streetName.equals("")){
            Toast.makeText(this, "Please enter a street name!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.searchButton).setClickable(true);
            return;
        }

        for (int i = 0; i < invalidCharacters.length; i++){

            if(streetName.contains(invalidCharacters[i])){
                Toast.makeText(getApplicationContext(), "Street name not in database.", Toast.LENGTH_SHORT).show();
                findViewById(R.id.searchButton).setClickable(true);
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

        database.child(route).child(streetName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // residentId exists

                    //showProgress(true);

                    //resident = (Resident) dataSnapshot.getValue(Resident.class);
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        residents.add(ds.getValue(Resident.class));
                    }

                    showStreetNamePopup();

                } else {
                    // residentId does not exist
                    Toast.makeText(getApplicationContext(), "Street name not in database.", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.searchButton).setClickable(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void setUpPopupRecyclerView(final View layout){


        LinearLayoutManager mLayoutManager;
        RecyclerView residentsRecyclerView;
        RoutesAdapter mAdapter;

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        residentsRecyclerView = layout.findViewById(R.id.popupRecyclerView);
        residentsRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RoutesAdapter(getApplicationContext(), mLayoutManager, routes);
        residentsRecyclerView.setAdapter(mAdapter);


    }


    private void setUpResidentsRecyclerView(View layout){
        sortResidentList();

        LinearLayoutManager mLayoutManager;
        RecyclerView residentsRecyclerView;
        ResidentsAdapter mAdapter;

        mLayoutManager = new LinearLayoutManager(this);
        residentsRecyclerView = layout.findViewById(R.id.popupRecyclerView);
        residentsRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ResidentsAdapter(this, mLayoutManager, residents);
        residentsRecyclerView.setAdapter(mAdapter);

        //showProgress(false);
    }

    public void sortResidentList(){

        Collections.sort(residents, new Comparator<Resident>() {
            @Override
            public int compare(Resident r1, Resident r2) {
            return r1.getStreetNumberInt() - r2.getStreetNumberInt();
        }
    });

    }

    public void showStreetNamePopup(){

        if(popupOpen){return;}

        popupOpen = true;

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
                    popupOpen = false;
                    findViewById(R.id.searchButton).setClickable(true);
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

    public void getRoutesFromDatabase(){

        routes = new ArrayList<>();

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    routes.add(snapshot.getKey());
                }

                chooseRoute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /*@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }*/

    public void setupStreetsRecyclerView(){

        streets = new ArrayList<>();

        database.child(route).addListenerForSingleValueEvent(new ValueEventListener() {
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

                streetsRecyclerView = findViewById(R.id.streetsRecyclerView);
                LinearLayoutManager mLayoutManager;
                StreetsAdapter mAdapter;

                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                streetsRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new StreetsAdapter(getApplicationContext(), mLayoutManager, streets);
                streetsRecyclerView.setAdapter(mAdapter);


                //Add streets to autocomplete textview
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_dropdown_item_1line, streets);
                streetNameAutoCompleteTextView.setAdapter(adapter);

                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
