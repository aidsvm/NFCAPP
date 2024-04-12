package com.example.nfcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.example.nfcapp.api.ApiService;
import com.example.nfcapp.api.RetrofitClient;
import com.example.nfcapp.model.ObjectEntity;

public class addObjectActivity extends AppCompatActivity {

    private InitActivity initActivity;

    private EditText editTextObjectName;
    private EditText editTextObjectDesc;
    private EditText editTextObjectLocation;
    private Button buttonAddObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addobject);

        editTextObjectName = findViewById(R.id.name_input);
        editTextObjectDesc = findViewById(R.id.desc_input);
        editTextObjectLocation = findViewById(R.id.location_input);

        buttonAddObject.setOnClickListener(v -> onAddObjectClicked());
    }

    public void onAddObjectClicked() {
        String objectName = editTextObjectName.getText().toString();
        String objectDesc = editTextObjectDesc.getText().toString();
        String objectLocation = editTextObjectLocation.getText().toString();

        initActivity.setupInit(objectName, objectDesc, objectLocation);

        Intent intent = new Intent(this, InitActivity.class);
        startActivity(intent);
    }

}
