package com.example.nfcapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nfcapp.R;
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
    public String username;


    /**
     * Sets up the activity. Sets up the recylerView for the list of the objects in the database.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage);

        recyclerView = findViewById(R.id.objects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ObjectAdapter(objectList, this);
        recyclerView.setAdapter(adapter);

        // function call
        fetchObjectsFromAPI();
    }

    /**
     * When an object is clicked on it is stored in selectedObject for later use.
     *
     * @param object passes in the selected object
     */
    @Override
    public void onItemClick(ObjectEntity object) {
        selectedObject = object; // Update the selected object
        // This handles when an object is selected, and highlights it.
        adapter.notifyDataSetChanged();
        Log.d("Selected Object", selectedObject.getObjectName());
    }

    /**
     * This function makes an API call to remove and object from the database, once the call is
     * successful, it will remove the object from the objectList and update the view.
     *
     * @param view
     */
    public void deleteSelectedObject(View view) {
        if (selectedObject != null) {
            ApiService apiService = RetrofitClient.getApiService();
            Call<Void> call = apiService.removeObject(selectedObject.getObjectId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Remove the selected object from the list
                        int index = objectList.indexOf(selectedObject);
                        if (index != -1) {
                            objectList.remove(index);
                            adapter.notifyItemRemoved(index); // Notify the adapter of the item removal
                        }
                        Toast.makeText(AdminOptionsActivity.this, "Object deleted successfully", Toast.LENGTH_SHORT).show();
                        selectedObject = null;  // Clear the selected object
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

    /**
     * This function calls the API to getAllObjects from the database. Once the call is successful,
     * it will update the objectList with all of the objects.
     */
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

    /**
     * When the admin clicks add object, sends them to init page.
     *
     * @param view
     */
    public void onAdminAddObject(View view) {
        // Go back to add object page
        Intent intent = new Intent(this, addObjectActivity.class);
        startActivity(intent);
    }

    /**
     * Sends back to scan activity.
     *
     * @param view
     */
    public void onBackOptions(View view) {
        // Start the admin login activity
        Intent intent = new Intent(this, NFCActivity.class);
        startActivity(intent);
    }

}
