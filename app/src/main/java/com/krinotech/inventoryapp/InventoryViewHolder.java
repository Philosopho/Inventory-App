package com.krinotech.inventoryapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krinotech.inventoryapp.databinding.InventoryItemBinding;

class InventoryViewHolder extends RecyclerView.ViewHolder {
    private InventoryItemBinding binding;

    public InventoryViewHolder(@NonNull InventoryItemBinding itemView) {
        super(itemView.getRoot());

        binding = itemView;
    }

    public void bind(Inventory inventory) {
        binding.setInventory(inventory);
        binding.executePendingBindings();
        if(inventory.getQuantity() == 0) {
            binding.tvQuantity.setText(binding.tvPrice.getResources().getString(R.string.out_of_stock));
        }
    }
}
