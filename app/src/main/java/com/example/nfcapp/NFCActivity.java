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

// Note: All this program currently does is scanning and retrieving a NFC UID. Other functionalities will
// be added after this part is done.

public class NFCActivity extends AppCompatActivity {
    // Adapter for checking NFC support and reading tags.
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup content view from activity_main layout.
        setContentView(R.layout.activity_main);

        // Gets the default NFC reader from the device.
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // If the device does not contain a NFC reader, a message will popup and the application will close.
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available on this device.", Toast.LENGTH_LONG).show();
            finish(); // Close the app if NFC isn't supported
            return;
        }

        // Handle any NFC intent that started the activity
        handleIntent(getIntent());
    }

    public void onAdminLoginClick(View view) {
        // Start the admin login activity
        Intent intent = new Intent(this, AdminLoginActivity.class);
        startActivity(intent);
    }

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



    @Override
    protected void onPause() {
        super.onPause();
        // Disable foreground dispatch when the app is not in focus to conserve resources.
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
        // Log the action of the incoming intent for debugging purposes.
        Log.d("NFCActivity", "handleIntent: " + intent.getAction());
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
                displayNfcData(bytesToHex(tag.getId()));
            }
        }
    }

    /**
     *
     * Displays NFC UID by updating UI element.
     *
     * @param data
     */
    private void displayNfcData(String data) {
        // Find the UI element to update
        TextView tvNfcData = findViewById(R.id.tvNfcData);
        // Sets text of the specified UI element.
        tvNfcData.setText(data);
    }

    /**
     *
     * Converts the UID (which is in bytes) to Hex.
     *
     * @param bytes
     * @return
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}