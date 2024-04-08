package com.example.nfcapp;

import android.content.Intent;
import android.nfc.NfcAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class NFCActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void scanUID(Intent intent) {

    }
}