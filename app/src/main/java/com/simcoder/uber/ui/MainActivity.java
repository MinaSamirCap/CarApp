package com.simcoder.uber.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.simcoder.uber.R;
import com.simcoder.uber.onAppKilled;

public class MainActivity extends BaseActivity {

    private Button driverButton, customerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        driverButton = findViewById(R.id.driver_button);
        customerButton = findViewById(R.id.customer_button);

        startService(new Intent(MainActivity.this, onAppKilled.class));
        driverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DriverLoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomerLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
