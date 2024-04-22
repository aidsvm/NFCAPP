package com.example.nfcapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nfcapp.api.ApiService;
import com.example.nfcapp.api.RetrofitClient;
import com.example.nfcapp.model.ObjectEntity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOptionsActivity extends AppCompatActivity implements ObjectAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ObjectAdapter adapter;
    private List<ObjectEntity> objectList = new ArrayList<>();

    private ObjectEntity selectedObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage);

        recyclerView = findViewById(R.id.objects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ObjectAdapter(objectList, this);
        recyclerView.setAdapter(adapter);

        fetchObjectsFromAPI();
    }

    @Override
    public void onItemClick(ObjectEntity object) {
        selectedObject = object; // Update the selected object
        adapter.notifyDataSetChanged();
        Log.d("Selected Object", selectedObject.getObjectName());
    }

    public void deleteSelectedObject(View view) {
        if (selectedObject != null) {
            ApiService apiService = RetrofitClient.getApiService();
            Log.d("ID", selectedObject.getObjectId().toString());
            Call<Void> call = apiService.removeObject(selectedObject.getObjectId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Refresh your list or UI here if necessary
                        Toast.makeText(AdminOptionsActivity.this, "Object deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminOptionsActivity.this, "Failed to delete object", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AdminOptionsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(AdminOptionsActivity.this, "No object selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchObjectsFromAPI() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<ObjectEntity>> call = apiService.getAllObjects();

        call.enqueue(new Callback<List<ObjectEntity>>() {
            @Override
            public void onResponse(Call<List<ObjectEntity>> call, Response<List<ObjectEntity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    objectList.clear();
                    objectList.addAll(response.body());
                    adapter.notifyDataSetChanged(); // Refresh the adapter
                    for (ObjectEntity object : objectList) {
                        Log.d("AdminOptionsActivity", "Object NAME: " + object.getObjectName());
                        Log.d("AdminOptionsActivity", "Object UID: " + object.getNfcId());
                    }
                } else {
                    Toast.makeText(AdminOptionsActivity.this, "Failed to fetch objects", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ObjectEntity>> call, Throwable t) {
                Toast.makeText(AdminOptionsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onAdminAddObject(View view) {
        // Go back to add object page
        Intent intent = new Intent(this, addObjectActivity.class);
        startActivity(intent);
    }

    public void onBackOptions(View view) {
        // Start the admin login activity
        Intent intent = new Intent(this, NFCActivity.class);
        startActivity(intent);
    }

}
