package com.nicklasoxhammar.cartchecking.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nicklasoxhammar.cartchecking.Activities.MainActivity;
import com.nicklasoxhammar.cartchecking.R;

import java.util.ArrayList;

/**
 * Created by Nick on 2018-08-04.
 */

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ViewHolder> {

    Context mContext;
    LinearLayoutManager llm;
    ArrayList<String> routes;


    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder  {

        protected TextView street;
        protected View cardView;

        public ViewHolder(View v) {
            super(v);

            street = v.findViewById(R.id.street_name_card_text);
            cardView = v.findViewById(R.id.card_view_street);

        }

    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public RoutesAdapter(Context context, LinearLayoutManager linearLayoutManager, ArrayList<String> routes) {
        mContext = context;
        this.routes = routes;
        llm = linearLayoutManager;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public RoutesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.street_name_card_view, parent, false);

        mContext = parent.getContext();

        return new RoutesAdapter.ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RoutesAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.street.setText(routes.get(position));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof MainActivity){
                    ((MainActivity)mContext).setRoute(holder.street.getText().toString());
                    ((MainActivity)mContext).closePopup();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return routes.size();
    }


}
