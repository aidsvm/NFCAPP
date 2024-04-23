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

    /**
     * Initializes the activity with a form to input new object details.
     * This method is called when the activity starts and handles the initialization
     * of UI components and setting up initial states.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addobject);

        editTextObjectName = findViewById(R.id.name_input);
        editTextObjectDesc = findViewById(R.id.desc_input);
        editTextObjectLocation = findViewById(R.id.location_input);
    }

    /**
     * Handles the click event on the "Add Object" button.
     * This method gathers the input data from the user, creates a new Intent
     * to start {@link InitActivity}, and passes the entered object details to it.
     *
     * @param view The view (button) that was clicked.
     */
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

    /**
     * Handles the back button click to navigate to the admin options activity.
     * This method creates a new Intent to start {@link AdminOptionsActivity} and navigates the user back.
     *
     * @param view The view (button) that was clicked.
     */
    public void onBackObject(View view) {
        // Start the admin login activity
        Intent intent = new Intent(this, AdminOptionsActivity.class);
        startActivity(intent);
    }

}
