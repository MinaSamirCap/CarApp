package com.simcoder.uber.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simcoder.uber.R;
import com.simcoder.uber.tripRecyclerView.TripAdapter;
import com.simcoder.uber.tripRecyclerView.TripModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class FixedTripActivity extends AppCompatActivity {

    private RecyclerView tripRecyclerView;
    private RecyclerView.Adapter tripAdapter;

    private ArrayList tripsArrayList = new ArrayList<TripModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_trip);

        tripRecyclerView = findViewById(R.id.trips_recycler_view);
        tripAdapter = new TripAdapter(tripsArrayList, this);

        //tripRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        tripRecyclerView.setHasFixedSize(true);
        tripRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tripRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(ContextCompat.getColor(this, R.color.transparent))
                        .sizeResId(R.dimen.divider)
                        .build());

        tripRecyclerView.setAdapter(tripAdapter);

        getTrips();
    }

    private void getTrips() {
        DatabaseReference fixedTripsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("FixedTrips");
        fixedTripsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tripsArrayList.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    TripModel tripModel = child.getValue(TripModel.class);
                    tripsArrayList.add(tripModel);

                }
                Collections.reverse(tripsArrayList);
                //tripAdapter.notifyItemRangeInserted(0, tripsArrayList.size());
                tripAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, FixedTripActivity.class));
    }
}
