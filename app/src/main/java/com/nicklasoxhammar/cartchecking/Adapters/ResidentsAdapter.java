package com.nicklasoxhammar.cartchecking.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nicklasoxhammar.cartchecking.Activities.MainActivity;
import com.nicklasoxhammar.cartchecking.CartCheck;
import com.nicklasoxhammar.cartchecking.R;
import com.nicklasoxhammar.cartchecking.Resident;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Nick on 2018-06-12.
 */

public class ResidentsAdapter extends RecyclerView.Adapter<ResidentsAdapter.ViewHolder> {

    Context mContext;
    LinearLayoutManager llm;
    ArrayList<Resident> residents;

    DatabaseReference database;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView address;
        protected TextView apartmentNumber;
        protected TextView name;
        protected View cardView;

        public ViewHolder(View v) {
            super(v);

            address = v.findViewById(R.id.resident_card_address_text);
            apartmentNumber = v.findViewById(R.id.resident_card_apartment_text);
            name = v.findViewById(R.id.resident_card_name_text);
            cardView = v.findViewById(R.id.card_view_resident);

        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ResidentsAdapter(Context context, LinearLayoutManager linearLayoutManager, ArrayList<Resident> residents) {
        mContext = context;
        this.residents = residents;
        llm = linearLayoutManager;

        database = FirebaseDatabase.getInstance().getReference();


    }

    // Create new views (invoked by the layout manager)
    @Override
    public ResidentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resident_card_view, parent, false);

        mContext = parent.getContext();

        return new ResidentsAdapter.ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ResidentsAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Resident r = residents.get(position);


        if (!r.getCartOnDifferentStreet().equals("")) {
            holder.address.setText("*" + r.getStreetNumber() + " " + r.getStreetName() + "*");
        } else {
            holder.address.setText(r.getStreetNumber() + " " + r.getStreetName());
        }

        if (!r.getApartmentNumber().equals("")) {
            holder.apartmentNumber.setText("APT: " + r.getApartmentNumber());
        } else {
            holder.apartmentNumber.setText("");
        }
        holder.name.setText(r.getLastName());

        if (r.alreadyChecked) {
            holder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.colorLightGreen));
        } else {
            holder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.cardview_light_background));
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIdScannedActivity(r);
            }
        });

    }

    @Override
    public int getItemCount() {
        return residents.size();
    }

    public void startIdScannedActivity(Resident r) {
        ((MainActivity) mContext).startIdScannedActivity(r.getStreetName(), r.getID());
    }


}
