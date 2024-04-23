package com.example.nfcapp.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nfcapp.R;
import com.example.nfcapp.api.ApiService;
import com.example.nfcapp.api.RetrofitClient;
import com.example.nfcapp.model.ObjectEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NFCActivity extends AppCompatActivity {
    // Adapter for checking NFC support and reading tags.
    private NfcAdapter nfcAdapter;

    /**
     * Initializes the activity, and sets up the nfc adapter.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup content view from activity_main layout.
        setContentView(R.layout.activity_main);

        // Gets the default NFC reader from the device.
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        NetworkUtils.checkApiConnection();
        // If the device does not contain a NFC reader, a message will popup and the application will close.
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available on this device.", Toast.LENGTH_LONG).show();
            finish(); // Close the app if NFC isn't supported
            return;
        }

        // Handle any NFC intent that started the activity
        handleIntent(getIntent());
    }

    /**
     * This handles the intent for when the user clicks on admin login, which will take them to
     * the login page.
     *
     * @param view
     */

    public void onAdminLoginClick(View view) {
        // Start the admin login activity
        Intent intent = new Intent(this, AdminLoginActivity.class);
        startActivity(intent);
    }

    /**
     * Re-registers the NFC foreground dispatch system with the PendingIntent and IntentFilter settings
     * each time the activity resumes. This ensures that the activity will handle NFC intents before
     * any other app when the app is actively being used. This method sets up the NFC adapter to
     * intercept NFC tags that match the defined technologies while the app is in the foreground,
     * providing a more direct interaction with NFC tags.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Create a pending intent that will restart the activity with the intent of a discovered NFC tag.
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                // PendingIntent had to be set to mutable so an action would actually populate in it.
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE);
        // Set up an intent filter for all NFC discoveries.
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] intentFiltersArray = new IntentFilter[]{techDetected};
        // Specify which NFC technologies your app supports.
        String[][] techListsArray = new String[][]{new String[]{NfcA.class.getName()}};
        // Enable foreground dispatch to give your app priority in handling NFC intents over other apps.
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
    }

    /**
     * Disables the NFC foreground dispatch system when the activity is no longer in the foreground.
     * This method is called as part of the activity lifecycle when the activity is going into the
     * background. Disabling foreground dispatch helps to conserve battery and ensures that the
     * activity does not intercept NFC intents when it is not visible to the user.
     */
    @Override
    protected void onPause() {
        super.onPause();
        // Check if the NFC adapter is not null and then disable the NFC foreground dispatch.
        // This is important to prevent this activity from intercepting NFC intents when it is not active.
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // Ensure the new intent is used
        setIntent(intent);
        // Handle the new intent
        handleIntent(intent);

    }

    /**
     * Handles the NFC intent when a new technology (NFC tag) is discovered.
     * This will then extract the NFC UID, and then passes the UID into
     * te retrieveObjectInfo function.
     *
     * @param intent
     */

    private void handleIntent(Intent intent) {
        // Retrieve the action from the intent to determine the type of NFC interaction.
        String action = intent.getAction();

        // Check if the intent's action is one of the NFC discovered actions.
        // ACTION_NDEF_DISCOVERED: Indicates that an NDEF formatted tag was discovered.
        // ACTION_TECH_DISCOVERED: Indicates that a tag supporting a specific technology was discovered.
        // ACTION_TAG_DISCOVERED: Indicates that a tag was discovered.
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {

            Tag tag = null; // Initialize the tag object to null.

            // Check the SDK version to use the appropriate method to retrieve the Tag object.
            // For Android Tiramisu (API level 33) and above, use the type-safe method to get the Tag object.
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag.class);
            }

            // If a Tag object was successfully retrieved, process it.
            if (tag != null) {
                // Convert the tag's ID to a hex string and display it.
                // This is typically used to visually represent the tag's unique identifier to the user.
                String UID = bytesToHex(tag.getId());
                // Calls the function with the specified UID to retrieve the object information based off of it.
                retrieveObjectInfo(UID);
            }
        }
    }

    /**
     * This function calls the API function getObjectInfoByNfcId with the extracted UID
     * of the NFC card. It initializes an ObjectEntity with the object from the database
     * that matches the UID and extracts the objectName, objectDesc, and objectLocation
     * and passes it into the displayObjectData function.
     *
     * @param UID: NFC ID
     */

    private void retrieveObjectInfo(String UID) {

        ApiService apiService = RetrofitClient.getApiService();
        Call<ObjectEntity> call = apiService.getObjectInfoByNfcId(UID);
        Log.d("UID", UID);
        call.enqueue(new Callback<ObjectEntity>() {
            @Override
            public void onResponse(Call<ObjectEntity> call, Response<ObjectEntity> response) {
                // Sets up an ObjectEntity if the response is successful from the API.
                if (response.isSuccessful() && response.body() != null) {
                    ObjectEntity object = response.body();
                    String objectName = object.getObjectName();
                    String objectDesc = object.getObjectDesc();
                    String objectLocation = object.getObjectLocation();
                    // Then displays the info in the corresponding text boxes.
                    displayObjectData(objectName, objectDesc, objectLocation);
                } else {
                    Toast.makeText(NFCActivity.this, "Failed to retrieve object: " + response.code(), Toast.LENGTH_LONG).show();
                    Log.d("API Error", "Failed to retrieve object: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ObjectEntity> call, Throwable t) {
                Log.d("API Failure", "Error: " + t.getMessage());
            }
        });

    }

    /**
     * This function takes the parameters from the retrieved object and displays it to the
     * UI.
     *
     * @param objectName:     Name of the object
     * @param objectDesc:     Description of the object
     * @param objectLocation: Location of the object
     */
    private void displayObjectData(String objectName, String objectDesc, String objectLocation) {
        // Find the text view by ID and initializes a textView
        TextView nameOutput = findViewById(R.id.name_output);
        TextView descOutput = findViewById(R.id.desc_output);
        TextView locationOutput = findViewById(R.id.location_output);

        // Sets the text of each view.
        nameOutput.setText(objectName);
        descOutput.setText(objectDesc);
        locationOutput.setText(objectLocation);
    }

    /**
     * Converts the UID (which is in bytes) to Hex.
     *
     * @param bytes: takes in the initial state of the UID
     * @return the properly formatted UID
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}