package com.krinotech.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krinotech.inventoryapp.databinding.InventoryItemBinding;

import static android.provider.BaseColumns._ID;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.QUANTITY;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.TABLE_NAME;

class InventoryViewHolder extends RecyclerView.ViewHolder {
    private InventoryItemBinding binding;

    public InventoryViewHolder(@NonNull InventoryItemBinding itemView) {
        super(itemView.getRoot());

        binding = itemView;

        binding.btnSale.setOnClickListener(updateQuantityClick());
        binding.btnDetails.setOnClickListener(detailsClick());
    }

    private View.OnClickListener detailsClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                intent.putExtra(v.getResources().getString(R.string.inventory_extra), binding.getInventory());

                v.getContext().startActivity(intent);
            }
        };
    }

    private void setInventoryBinding(Inventory inventory) {
        binding.setInventory(inventory);
        binding.executePendingBindings();
        checkOutOfStock(inventory);
    }

    private View.OnClickListener updateQuantityClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Inventory inventory = binding.getInventory();
                int quantity = inventory.getQuantity();
                if(quantity >= 1) {
                    inventory.setQuantity(quantity - 1);

                    InventorySQLiteOpenHelper helper = new InventorySQLiteOpenHelper(v.getContext());
                    SQLiteDatabase writeableDb = helper.getWritableDatabase();

                    int rowsAffected = helper.updateQuantity(
                            writeableDb,
                            inventory.getQuantity(),
                            inventory.getId()
                    );

                    if(rowsAffected >= 1) {
                        setInventoryBinding(inventory);
                    }
                }
            }
        };
    }

    private void checkOutOfStock(Inventory inventory) {
        if (inventory.getQuantity() == 0) {
            binding.tvQuantity.setText(binding.tvPrice.getResources().getString(R.string.out_of_stock));
        }
    }

    public void bind(Inventory inventory) {
        setInventoryBinding(inventory);
    }
}
