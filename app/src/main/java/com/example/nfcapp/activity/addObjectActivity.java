package com.example.nfcapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.example.nfcapp.R;

public class addObjectActivity extends AppCompatActivity {

    private InitActivity initActivity;
    private EditText editTextObjectName;
    private EditText editTextObjectDesc;
    private EditText editTextObjectLocation;
    private Button buttonAddObject;

    public String objectName;
    public String objectDesc;
    public String objectLocation;

    public String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addobject);

        editTextObjectName = findViewById(R.id.name_input);
        editTextObjectDesc = findViewById(R.id.desc_input);
        editTextObjectLocation = findViewById(R.id.location_input);

        Intent intent = getIntent();

    }

    public void onAddObjectClicked(View view) {
        objectName = editTextObjectName.getText().toString();
        objectDesc = editTextObjectDesc.getText().toString();
        objectLocation = editTextObjectLocation.getText().toString();

        Intent intent = new Intent(this, InitActivity.class);
        intent.putExtra("objectName", objectName);
        intent.putExtra("objectDesc", objectDesc);
        intent.putExtra("objectLocation", objectLocation);
        startActivity(intent);
    }

    public void onBackObject(View view) {
        // Start the admin login activity
        Intent intent = new Intent(this, AdminOptionsActivity.class);
        startActivity(intent);
    }

}
