package com.simcoder.uber.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simcoder.uber.R;
import com.simcoder.uber.historyRecyclerView.HistoryAdapter;
import com.simcoder.uber.historyRecyclerView.HistoryModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class HistoryActivity extends AppCompatActivity {

    private TextView balanceTextView;
    private RecyclerView historyRecyclerView;
    private RecyclerView.Adapter historyAdapter;

    private String customerOrDriver, userId;
    private Double Balance = 0.0;
    private ArrayList historyArrayList = new ArrayList<HistoryModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        balanceTextView = findViewById(R.id.balance_text_view);
        historyRecyclerView = findViewById(R.id.history_recycler_view);
        historyAdapter = new HistoryAdapter(historyArrayList, HistoryActivity.this);

        historyRecyclerView.setHasFixedSize(true);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(historyAdapter);


        customerOrDriver = getIntent().getExtras().getString("customerOrDriver");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getUserHistoryIds();

        if(customerOrDriver.equals("Drivers")){
            balanceTextView.setVisibility(View.VISIBLE);
        }
    }

    private void getUserHistoryIds() {
        DatabaseReference userHistoryDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(customerOrDriver).child(userId).child("history");
        userHistoryDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot history : dataSnapshot.getChildren()){
                        FetchRideInformation(history.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void FetchRideInformation(String rideKey) {
        DatabaseReference historyDatabase = FirebaseDatabase.getInstance().getReference().child("history").child(rideKey);
        historyDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    HistoryModel historyModel = new HistoryModel();

                    historyModel.setRideId(dataSnapshot.getKey());
                    Long timestamp = 0L;
                    String distance = "";

                    if(dataSnapshot.child("timestamp").getValue() != null){
                        timestamp = Long.valueOf(dataSnapshot.child("timestamp").getValue().toString());
                        historyModel.setTime(getDate(timestamp));
                    }
                    if (dataSnapshot.child("distance").getValue() != null){
                        distance = dataSnapshot.child("distance").getValue().toString();
                        historyModel.setDistance(distance.substring(0, Math.min(distance.length(), 5)) + " km");
                        historyModel.setPrice(Double.valueOf(distance) * 0.5);

                    }
                    if(dataSnapshot.child("rating").getValue() != null){
                        historyModel.setRate(Integer.valueOf(dataSnapshot.child("rating").getValue().toString()));
                    }

                    historyArrayList.add(historyModel);
                    historyAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private String getDate(Long time) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time*1000);
        String date = DateFormat.format("MM-dd-yyyy hh:mm", cal).toString();
        return date;
    }

}
