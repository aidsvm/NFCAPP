package com.example.nfcapp;

import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nfcapp.model.ObjectEntity;

import java.util.List;

public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.ViewHolder> {
    List<ObjectEntity> objects;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ObjectEntity object);
    }


    public ObjectAdapter(List<ObjectEntity> objects, OnItemClickListener listener) {
        this.objects = objects;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_object, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ObjectEntity object = objects.get(position);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(object));

        holder.tvObjectName.setText(object.getObjectName());
        holder.tvObjectLocation.setText(object.getObjectLocation());
        holder.tvObjectDesc.setText(object.getObjectDesc());
        holder.tvUID.setText(object.getId());
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvObjectName, tvObjectLocation, tvObjectDesc, tvUID;

        public ViewHolder(View itemView) {
            super(itemView);
            tvObjectName = itemView.findViewById(R.id.tvObjectName);
            tvObjectLocation = itemView.findViewById(R.id.tvObjectLocation);
            tvObjectDesc = itemView.findViewById(R.id.tvObjectDesc);
            tvUID = itemView.findViewById(R.id.tvUID);
        }
    }
}
