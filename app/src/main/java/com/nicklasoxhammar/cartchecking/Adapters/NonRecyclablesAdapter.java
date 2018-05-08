package com.nicklasoxhammar.cartchecking.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nicklasoxhammar.cartchecking.R;

import java.util.ArrayList;

/**
 * Created by Nick on 2018-04-17.
 */

public class NonRecyclablesAdapter extends RecyclerView.Adapter<NonRecyclablesAdapter.ViewHolder> {

    Context mContext;
    LinearLayoutManager llm;
    ArrayList<String> nonRecyclables;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder  {

        protected TextView nonRecyclableText;
        protected View cardView;
        protected CheckBox checkBox;

        public ViewHolder(View v) {
            super(v);

            nonRecyclableText = (TextView) v.findViewById(R.id.non_recyclable_card_text);
            cardView = v.findViewById(R.id.card_view_non_recyclable);
            checkBox = v.findViewById(R.id.non_recyclable_card_checkbox);

        }

    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public NonRecyclablesAdapter(Context context, LinearLayoutManager linearLayoutManager, ArrayList<String> nonRecyclables) {
        mContext = context;
        this.nonRecyclables = nonRecyclables;
        llm = linearLayoutManager;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public NonRecyclablesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.non_recyclable_card_view, parent, false);

        mContext = parent.getContext();

        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final NonRecyclablesAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        String nonRecyclable = nonRecyclables.get(position);

        holder.nonRecyclableText.setText(nonRecyclable);
        holder.checkBox.setTag(nonRecyclable);

    }




    @Override
    public int getItemCount() {
        return nonRecyclables.size();
    }

}
