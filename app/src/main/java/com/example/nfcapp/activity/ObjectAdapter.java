package com.example.nfcapp.activity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nfcapp.R;
import com.example.nfcapp.model.ObjectEntity;

import java.util.List;

public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.ViewHolder> {
    List<ObjectEntity> objects;
    private OnItemClickListener listener;

    private int selectedItemPosition = RecyclerView.NO_POSITION; // Track the position of the selected item

    /**
     * Interface to handle click events on items within this adapter.
     */
    public interface OnItemClickListener {
        void onItemClick(ObjectEntity object);
    }

    /**
     * Constructor for ObjectAdapter.
     *
     * @param objects  List of {@link ObjectEntity} objects to be displayed in the RecyclerView.
     * @param listener Listener that handles item click events.
     */
    public ObjectAdapter(List<ObjectEntity> objects, OnItemClickListener listener) {
        this.objects = objects;
        this.listener = listener;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_object, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method updates
     * the contents of the {@link ViewHolder#itemView} to reflect the item at the given position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ObjectEntity object = objects.get(position);

        holder.tvObjectName.setText(object.getObjectName());
        holder.tvObjectLocation.setText(object.getObjectLocation());
        holder.tvUID.setText(object.getNfcId());

        holder.itemView.setBackgroundColor(selectedItemPosition == position ? Color.rgb(144, 238, 144) : Color.TRANSPARENT); // Change color on selection

        holder.itemView.setOnClickListener(view -> {
            selectedItemPosition = position;
            notifyDataSetChanged();
            listener.onItemClick(object);
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return objects.size();
    }

    /**
     * ViewHolder for items in the RecyclerView, holds references to the TextViews for object name,
     * location, and UID to optimize performance by not repeatedly finding views.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvObjectName, tvObjectLocation, tvObjectDesc, tvUID;

        public ViewHolder(View itemView) {
            super(itemView);
            tvObjectName = itemView.findViewById(R.id.tvObjectName);
            tvObjectLocation = itemView.findViewById(R.id.tvObjectLocation);
            tvUID = itemView.findViewById(R.id.tvUID);
        }
    }
}
