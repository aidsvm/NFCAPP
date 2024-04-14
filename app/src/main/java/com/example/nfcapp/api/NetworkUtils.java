package com.example.nfcapp.api;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkUtils {

    public static void checkApiConnection() {
        ApiService service = RetrofitClient.getClient().create(ApiService.class);
        Call<Object> call = service.checkConnection();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Log.d("API Connection", "Success: " + response.body().toString());
                } else {
                    Log.d("API Connection", "Response Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("API Connection", "Failed to connect to API", t);
            }
        });
    }
}
