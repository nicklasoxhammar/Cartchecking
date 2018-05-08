package com.nicklasoxhammar.cartchecking.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nicklasoxhammar.cartchecking.R;
import com.nicklasoxhammar.cartchecking.Resident;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";

    private FirebaseAuth mAuth;

    String qrString;

    TextView codeTextView;

    FirebaseUser currentUser;

    String residentId;

    Resident resident;

    DatabaseReference database;

    ArrayList<String> invalidCharacters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        codeTextView = findViewById(R.id.codeTextView);

        invalidCharacters = new ArrayList<String>();
        invalidCharacters.add(".");
        invalidCharacters.add("#");
        invalidCharacters.add("$");
        invalidCharacters.add("[");
        invalidCharacters.add("]");

        TextView textView = findViewById(R.id.userTextView);
        textView.setText("Hello there " + currentUser.getEmail());


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

        Log.d(TAG, "checkId: " + residentId);

        for (String s : invalidCharacters){

            if(residentId.contains(s)){
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

        Intent intent = new Intent(MainActivity.this, IdScannedActivity.class);
        intent.putExtra("residentId", residentId);
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




}
