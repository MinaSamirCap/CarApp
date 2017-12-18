package com.simcoder.uber.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.simcoder.uber.R;

public class FixedTripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_trip);
    }


    public static void openActivity(Context context){
        context.startActivity(new Intent(context, FixedTripActivity.class));
    }
}
