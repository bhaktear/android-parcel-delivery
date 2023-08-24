package com.example.android_parcel_delivery;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //Intent intent = new Intent(this,NavigationActivity.class);
        //Intent intent = new Intent(this,AddOrderActivity.class);
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}