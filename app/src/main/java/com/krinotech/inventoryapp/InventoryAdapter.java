package com.krinotech.inventoryapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.krinotech.inventoryapp.databinding.InventoryItemBinding;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryViewHolder> {
    private List<Inventory> inventories;

    public InventoryAdapter(List<Inventory> inventories) {
        this.inventories = inventories;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        InventoryItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.inventory_item, parent, false);
        return new InventoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        Inventory inventory = inventories.get(position);
        holder.bind(inventory);
    }

    @Override
    public int getItemCount() {
        if(inventories != null) {
            return inventories.size();
        }
        return 0;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
        notifyDataSetChanged();
    }
}
