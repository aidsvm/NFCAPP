package com.example.nfcapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nfcapp.R;
import com.example.nfcapp.api.ApiService;
import com.example.nfcapp.model.LoginDto;
import com.example.nfcapp.api.LoginResponse;
import com.example.nfcapp.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLoginActivity extends AppCompatActivity {


    private EditText editTextUsername;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

    }

    /**
     * Handles the back intent, goes back the the main page.
     * @param view
     */
    public void onAdminBack(View view) {
        // Start the admin login activity
        Intent intent = new Intent(this, NFCActivity.class);
        startActivity(intent);
    }

    /**
     * This function will retrieve the data from the admin specified inputs (username, password)
     * and will call the API function loginAdmin with the given inputs.
     * This API function will compare the username and password with the username and password
     * that is specified in the database. If successful login, this function will send the admin to
     * the options page.
     * @param view
     */
    public void onAdminLogin(View view) {
        editTextUsername = findViewById(R.id.username_input);
        editTextPassword = findViewById(R.id.password);

        final String adminUsername = editTextUsername.getText().toString().trim();
        String adminPassword = editTextPassword.getText().toString().trim();

        ApiService apiService = RetrofitClient.getApiService();
        LoginDto loginDto = new LoginDto(adminUsername, adminPassword);

        Call<LoginResponse> call = apiService.loginAdmin(loginDto);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Log.d("API Response", "Login successful!");

                    SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("USERNAME", adminUsername);
                    editor.apply();

                    Intent intent = new Intent(AdminLoginActivity.this, AdminOptionsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("API Error", "Failed to log in: " + response.code());
                    Toast.makeText(AdminLoginActivity.this, "Login failed: " + (response.body() != null ? response.body().getMessage() : response.message()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("API Failure", "Error: " + t.getMessage());
                Toast.makeText(AdminLoginActivity.this, "Error connecting to server: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
