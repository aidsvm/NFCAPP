package com.example.nfcapp;

import android.content.Intent;
import android.os.Bundle;
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

        Button deleteButton = findViewById(R.id.deleteobjectbtn);
        deleteButton.setOnClickListener(v -> deleteSelectedObject());

        fetchObjectsFromAPI();
    }

    @Override
    public void onItemClick(ObjectEntity object) {
        selectedObject = object; // Update the selected object
    }

    private void deleteSelectedObject() {
        if (selectedObject != null) {
            ApiService apiService = RetrofitClient.getApiService();
            Call<ObjectEntity> call = apiService.removeObject(selectedObject);
            call.enqueue(new Callback<ObjectEntity>() {
                @Override
                public void onResponse(Call<ObjectEntity> call, Response<ObjectEntity> response) {
                    if (response.isSuccessful()) {
                        adapter.objects.remove(selectedObject);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(AdminOptionsActivity.this, "Object deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminOptionsActivity.this, "Failed to delete object", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ObjectEntity> call, Throwable t) {
                    Toast.makeText(AdminOptionsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
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

    public void onAdminDelete() {

    }

    public void onBackOptions(View view) {
        // Start the admin login activity
        Intent intent = new Intent(this, NFCActivity.class);
        startActivity(intent);
    }

}
