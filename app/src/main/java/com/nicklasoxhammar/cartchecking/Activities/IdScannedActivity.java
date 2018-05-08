package com.nicklasoxhammar.cartchecking.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nicklasoxhammar.cartchecking.Adapters.NonRecyclablesAdapter;
import com.nicklasoxhammar.cartchecking.R;
import com.nicklasoxhammar.cartchecking.Resident;
import com.nicklasoxhammar.cartchecking.SendMailTask;

import java.util.ArrayList;

public class IdScannedActivity extends AppCompatActivity {

    LinearLayoutManager mLayoutManager;
    RecyclerView nonRecyclablesRecyclerView;
    RecyclerView.Adapter mAdapter;
    ArrayList<String> nonRecyclables;

    TextView textView;

    DatabaseReference database;

    Resident resident;
    String residentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_scanned);

        database = FirebaseDatabase.getInstance().getReference();

        textView = findViewById(R.id.id_scanned_text_view);

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            Log.d("TAG", "onCreate: no extras=!");
        } else {
            residentId = extras.getString("residentId");

            getResidentFromDatabase();
        }


        fillNonRecyclablesList();
    }

    public void getResidentFromDatabase() {

        database.child("residents").child(residentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resident = (Resident) dataSnapshot.getValue(Resident.class);

                textView.setText("This cart belongs to " + resident.getFirstName() + " " + resident.getLastName() + " at " + resident.getAddress());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void fillNonRecyclablesList() {

        nonRecyclables = new ArrayList<String>();

        nonRecyclables.add("Bagged recyclables, garbage");
        nonRecyclables.add("Plastic bag");
        nonRecyclables.add("Food or liquid");
        nonRecyclables.add("Clothing or linen");
        nonRecyclables.add("Tangler");
        nonRecyclables.add("Big item");
        nonRecyclables.add("Recyclables in trash");

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new NonRecyclablesAdapter(this, mLayoutManager, nonRecyclables);

        nonRecyclablesRecyclerView = findViewById(R.id.non_recyclables_recycler_view);
        nonRecyclablesRecyclerView.setLayoutManager(mLayoutManager);
        nonRecyclablesRecyclerView.setAdapter(mAdapter);

    }

   /* protected void sendEmail(View view) {

        ArrayList<String> emailTo = new ArrayList<>();
        emailTo.add(resident.getEmail());

        new SendMailTask(IdScannedActivity.this).execute();

    }*/

   public void Report(View view){

   }

}
