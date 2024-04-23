package com.example.nfcapp.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import com.example.nfcapp.model.ObjectDto;
import com.example.nfcapp.api.RetrofitClient;
import com.example.nfcapp.model.ObjectEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InitActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    public String UID;
    public String objectName;
    public String objectDesc;
    public String objectLocation;
    public String username;
    public Long adminId;

    /**
     * Sets up activity. Retrieves admin inputted information from the last activity (objectName,
     * (objectDesc, objectLocation). It also retrieves the session username from the admin login.
     * Sets up a NFC adapter for scanning use.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        Intent intent = getIntent();
        if (intent != null) {
            objectName = intent.getStringExtra("objectName");
            objectDesc = intent.getStringExtra("objectDesc");
            objectLocation = intent.getStringExtra("objectLocation");
        }

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        username = prefs.getString("USERNAME", null);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available on this device.", Toast.LENGTH_LONG).show();
            finish(); // Close the app if NFC isn't supported
            return;
        }
    }

    /**
     * Re-registers the NFC foreground dispatch system with the PendingIntent and IntentFilter settings
     * each time the activity resumes. This ensures that the activity will handle NFC intents before
     * any other app when the app is actively being used. This method sets up the NFC adapter to
     * intercept NFC tags that match the defined technologies while the app is in the foreground,
     * providing a more direct interaction with NFC tags.
     */
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
     * Disabling foreground dispatch helps to conserve battery and ensures that the
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

    /**
     * Handles intent.
     *
     * @param intent The new intent that was started for the activity.
     */
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
     * It will call the getAdminId function with the username, sets up an
     * ObjectDto object with the object parameters. This object will then
     * be passed to addObject to be added to the database.
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
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag.class);
            }

            // If a Tag object was successfully retrieved, process it.
            if (tag != null) {
                String UID = bytesToHex(tag.getId());
                getAdminId(username, new AdminIdCallback() {
                    @Override
                    public void onAdminIdReceived(Long adminId) {
                        ObjectDto objectDto = new ObjectDto(objectName, objectDesc, objectLocation, UID, adminId);
                        addObject(objectDto);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(InitActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    /**
     * Interface for retrieving the adminId.
     */
    private interface AdminIdCallback {
        void onAdminIdReceived(Long adminId);

        void onError(String error);
    }

    /**
     * This function makes an API call with getAdminId, and passes in username to find the
     * adminId, once successful it will initialize an adminId.
     *
     * @param username from the login session
     * @param callback passed adminId for initialization
     */
    private void getAdminId(String username, AdminIdCallback callback) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<Long> call = apiService.getAdminId(username);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adminId = response.body();
                    Log.d("Admin Id: ", adminId.toString());
                    callback.onAdminIdReceived(adminId);
                } else {
                    Log.d("Admin Id:", "NOT FOUND!");
                    callback.onError("Admin ID not found");
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.e("API Failure", "Error: " + t.getMessage());
                callback.onError(t.getMessage());
            }
        });
    }

    /**
     * This function makes an API call with an ObjectDto object. Once the call is successful, it will
     * call the addNfc function to add the assigned NFC to the NFC database table.
     *
     * @param objectDto sends this object to the API to be added to the database.
     */
    private void addObject(ObjectDto objectDto) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<ObjectEntity> call = apiService.addObject(objectDto);
        call.enqueue(new Callback<ObjectEntity>() {
            @Override
            public void onResponse(Call<ObjectEntity> call, Response<ObjectEntity> response) {
                TextView mapField = findViewById(R.id.map_object_field);
                String message = "";
                if (response.isSuccessful() && response.body() != null) {
                    ObjectEntity object = response.body();
                    message = "Object Added Successfully!";
                    Log.d("API Response", "Object added successfully with ID: " + object.getObjectId());
                    addNfc(objectDto.getNfcId());
                } else {
                    if (response.code() == 409) {
                        Toast.makeText(InitActivity.this, "NFC ID already exists in the database.", Toast.LENGTH_LONG).show();
                        message = "NFC ID already exists in the database!";
                    } else {
                        Toast.makeText(InitActivity.this, "Failed to add object: " + response.message(), Toast.LENGTH_LONG).show();
                        message = "Failed to add object!";
                    }
                    Log.e("API Error", "Failed to add object or ID is null: HTTP " + response.code());
                }
                mapField.setText(message);
            }

            @Override
            public void onFailure(Call<ObjectEntity> call, Throwable t) {
                Log.e("API Failure", "Error: " + t.getMessage());
            }
        });
    }

    /**
     * This function makes an API call to add the NFC UID into the database.
     *
     * @param UID passed to the API to be added to database
     */
    private void addNfc(String UID) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<String> call = apiService.addNfc(UID);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("addNfc", "Add NFC successful!");
                } else {
                    Log.d("addNfc", "Add NFC not successful!");
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("API Failure", "Error: " + t.getMessage());
            }
        });

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

    /**
     * Sends admin back to admin options page.
     *
     * @param view
     */
    public void onBack(View view) {
        // Start the admin login activity
        Intent intent = new Intent(this, AdminOptionsActivity.class);
        startActivity(intent);
    }
}

