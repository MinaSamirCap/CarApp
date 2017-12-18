package com.simcoder.uber.historyRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simcoder.uber.ui.HistorySingleActivity;
import com.simcoder.uber.R;

import java.util.List;

/**
 * Created by manel on 03/04/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolders> {

    private List<HistoryModel> data;
    private Context context;

    public HistoryAdapter(List<HistoryModel> itemList, Context context) {
        this.data = itemList;
        this.context = context;
    }

    @Override
    public HistoryViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_history, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        return new HistoryViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolders holder, final int position) {

        holder.tripRatingBar.setRating(data.get(position).getRate());
        if(data.get(position).getDistance() != null){
            holder.distanceTextView.setText(data.get(position).getDistance());
        }
        if (data.get(position).getTime() != null) {
            holder.timeTextView.setText(data.get(position).getTime());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistorySingleActivity.openActivity(context, data.get(holder.getAdapterPosition()).getRideId());

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

}