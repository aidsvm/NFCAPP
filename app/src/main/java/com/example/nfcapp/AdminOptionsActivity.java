package com.example.nfcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AdminOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage);
    }

    public void onAdminScan(View view) {
        // Go back to scan page
        Intent intent = new Intent(this, NFCActivity.class);
        startActivity(intent);
    }

    public void onAdminMap(View view) {
        // Go back to map/init page
        Intent intent = new Intent(this, InitActivity.class);
        startActivity(intent);
    }

    public void onAdminAddObject(View view) {
        // Go back to add object page
        Intent intent = new Intent(this, addObjectActivity.class);
        startActivity(intent);
    }

    public void onAdminDelete() {

    }

    public void onBackOptions(View view) {
        // Start the admin login activity
        Intent intent = new Intent(this, NFCActivity.class);
        startActivity(intent);
    }

}
