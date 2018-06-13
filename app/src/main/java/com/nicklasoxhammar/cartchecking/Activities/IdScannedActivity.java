package com.nicklasoxhammar.cartchecking.Activities;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Calendar;

public class IdScannedActivity extends AppCompatActivity {

    /*LinearLayoutManager mLayoutManager;
    RecyclerView nonRecyclablesRecyclerView;
    RecyclerView.Adapter mAdapter;*/

    ArrayList<CheckBox> nonRecyclableCheckBoxList;

    CheckBox correctlyRecycledCheckBox;

    TextView textView;
    EditText commentTextView;

    Boolean correctlyRecycled;

    DatabaseReference database;

    String streetNameKey;
    Resident resident;
    String residentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_scanned);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nonRecyclableCheckBoxList = new ArrayList<>();
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

            getResidentFromDatabase();
        }


       // fillNonRecyclablesList();
    }

    public void getResidentFromDatabase() {

        database.child("residents").child(streetNameKey).child(residentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resident = (Resident) dataSnapshot.getValue(Resident.class);

                textView.setText("This cart belongs to " + resident.getFirstName() + " " + resident.getLastName() + " at " + resident.getAddress().getStreetNumber() + " " + resident.getAddress().getStreetName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setupCheckBoxList(){

        correctlyRecycledCheckBox = findViewById(R.id.correctlyRecycledCheckBox);
        nonRecyclableCheckBoxList.add(correctlyRecycledCheckBox);

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

        CheckBox recyclablesInTrashCheckBox = findViewById(R.id.recyclablesInTrashCheckBox);
        nonRecyclableCheckBoxList.add(recyclablesInTrashCheckBox);

        for (CheckBox c : nonRecyclableCheckBoxList){
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getTag() != null){

                        for (CheckBox b : nonRecyclableCheckBoxList) {
                            if (b.getTag() == null) {
                                b.setChecked(false);
                            }

                        }
                    } else {
                        correctlyRecycledCheckBox.setChecked(false);
                    }
                }
            });
        }
    }

   /* public void fillNonRecyclablesList() {

        nonRecyclables = new ArrayList<String>();

        nonRecyclables.add("Bagged recyclables, garbage");
        nonRecyclables.add("Plastic bag");
        nonRecyclables.add("Food or liquid");
        nonRecyclables.add("Clothing or linen");
        nonRecyclables.add("Tangler");
        nonRecyclables.add("Big item");
        nonRecyclables.add("Recyclables in trash");

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new NonRecyclablesAdapter(this, mLayoutManager, nonRecyclables).g;

        nonRecyclablesRecyclerView = findViewById(R.id.non_recyclables_recycler_view);
        nonRecyclablesRecyclerView.setLayoutManager(mLayoutManager);
        nonRecyclablesRecyclerView.setAdapter(mAdapter);

    }*/

   /* protected void sendEmail(View view) {

        ArrayList<String> emailTo = new ArrayList<>();
        emailTo.add(resident.getEmail());

        new SendMailTask(IdScannedActivity.this).execute();

    }*/

   public void Report(View view){

       Boolean isSomethingChecked = false;

       ArrayList<String> nonRecyclables = new ArrayList<>();

       for (CheckBox c : nonRecyclableCheckBoxList){
           if (c.isChecked()){
               nonRecyclables.add(c.getText().toString());
               isSomethingChecked = true;
           }
       }

       if (correctlyRecycledCheckBox.isChecked()){
           correctlyRecycled = true;
           isSomethingChecked = true;
       }else{
           correctlyRecycled = false;
       }

       if (isSomethingChecked == false){
           Toast.makeText(this,"Please use the checkboxes!", Toast.LENGTH_SHORT).show();
           return;
       }

       CartCheck cartCheck = new CartCheck(Calendar.getInstance().getTime().toString(), commentTextView.getText().toString(), nonRecyclables, correctlyRecycled);

       //resident.addCartCheck(cartCheck);

       try {
           database.child("residents").child(streetNameKey).child(residentId).child("cartChecks").push().setValue(cartCheck);
           Toast.makeText(this, "Report successfully added to database!", Toast.LENGTH_SHORT).show();

       }catch (Exception e){
           Log.d("Exception", "Report: " + e);

           Toast.makeText(this, "Report failed, please try again!", Toast.LENGTH_SHORT).show();
       }

       //Start MainActivity
       Intent myIntent = new Intent(IdScannedActivity.this, MainActivity.class);
       IdScannedActivity.this.startActivity(myIntent);

       finish();

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
