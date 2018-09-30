package com.taxi.olaclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnDriver, btnCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDriver = findViewById(R.id.btnDriver);
        btnCustomer = findViewById(R.id.btnCustomer);

        btnDriver.setOnClickListener(this);
        btnCustomer.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCustomer:
                startActivity(new Intent(this, CustomerLoginActivity.class));
                break;
            case R.id.btnDriver:
                startActivity(new Intent(this, DriverLoginActivity.class));
                break;
        }
    }
}
