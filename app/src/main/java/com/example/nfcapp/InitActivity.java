package com.example.nfcapp;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);



        Intent intent = getIntent();
        if (intent != null) {
            objectName = intent.getStringExtra("objectName");
            objectDesc = intent.getStringExtra("objectDesc");
            objectLocation = intent.getStringExtra("objectLocation");
            username = intent.getStringExtra("USERNAME");
        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available on this device.", Toast.LENGTH_LONG).show();
            finish(); // Close the app if NFC isn't supported
            return;
        }
    }

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

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // Ensure the new intent is used
        setIntent(intent);
        // Handle the new intent
        handleIntent(intent);

    }

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

    private interface AdminIdCallback {
        void onAdminIdReceived(Long adminId);
        void onError(String error);
    }

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

    private void addNfc(String UID) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<String> call = apiService.addNfc(UID);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    Log.d("addNfc", "Add NFC successful!");
                }

                else {
                    Log.d("addNfc", "Add NFC not successful!");
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("API Failure", "Error: " + t.getMessage());
            }
        });



    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public void onBack(View view) {
        // Start the admin login activity
        Intent intent = new Intent(this, AdminOptionsActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }
}

