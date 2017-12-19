package com.simcoder.uber.tripRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.simcoder.uber.R;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripViewHolders> {

    private List<TripModel> data;
    private Context context;

    public TripAdapter(List<TripModel> itemList, Context context) {
        this.data = itemList;
        this.context = context;
    }

    @Override
    public TripViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_trip, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        return new TripViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(final TripViewHolders holder, final int position) {

        holder.nameTextView.setText(data.get(position).getName());
        holder.distanceTextView.setText(data.get(position).getDistance());
        holder.durationTextView.setText(data.get(position).getDuration());

        Glide.with(context).load(data.get(position).getMapPic()).into(holder.mapImageView);
        Glide.with(context).load(data.get(position).getOriginalPic()).into(holder.originalImageView);

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

}