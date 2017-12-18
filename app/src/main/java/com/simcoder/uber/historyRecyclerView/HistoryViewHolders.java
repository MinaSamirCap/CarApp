package com.simcoder.uber.historyRecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.simcoder.uber.HistorySingleActivity;
import com.simcoder.uber.R;

/**
 * Created by manel on 10/10/2017.
 */

public class HistoryViewHolders extends RecyclerView.ViewHolder {

    public TextView distanceTextView;
    public TextView timeTextView;
    public RatingBar tripRatingBar;

    public HistoryViewHolders(View itemView) {
        super(itemView);
        distanceTextView = itemView.findViewById(R.id.distance_text_view);
        timeTextView = itemView.findViewById(R.id.time_text_view);
        tripRatingBar = itemView.findViewById(R.id.trip_rating_bar);
    }


}
