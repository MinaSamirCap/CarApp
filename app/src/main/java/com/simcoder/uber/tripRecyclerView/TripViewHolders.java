package com.simcoder.uber.tripRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.simcoder.uber.R;

/**
 * Created by manel on 10/10/2017.
 */

public class TripViewHolders extends RecyclerView.ViewHolder {

    public ImageView mapImageView;
    public ImageView originalImageView;
    public TextView nameTextView;
    public TextView distanceTextView;
    public TextView durationTextView;

    public TripViewHolders(View itemView) {
        super(itemView);
        mapImageView = itemView.findViewById(R.id.map_image_view);
        originalImageView = itemView.findViewById(R.id.original_image_view);
        nameTextView = itemView.findViewById(R.id.trip_name_text_view);
        distanceTextView = itemView.findViewById(R.id.distance_text_view);
        durationTextView = itemView.findViewById(R.id.duration_text_view);
    }


}
