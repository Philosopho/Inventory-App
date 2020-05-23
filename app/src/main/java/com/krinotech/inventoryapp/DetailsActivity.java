package com.krinotech.inventoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Toast;

import com.krinotech.inventoryapp.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding binding;
    private boolean notDeleted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if(savedInstanceState != null) {
            notDeleted = savedInstanceState.getBoolean(getString(R.string.detailes_deleted_extra));
        }
        Inventory inventory = getIntent().getParcelableExtra(getString(R.string.inventory_extra));

        setTitle(getString(R.string.details_activity_title));

        binding.setInventory(inventory);
        binding.executePendingBindings();

        checkOutOfStock(inventory.getQuantity());

        binding.increaseQuantity.setOnClickListener(increaseQuantity(inventory));
        binding.decreaseQuantity.setOnClickListener(decreaseQuantity(inventory));
        binding.delete.setOnClickListener(delete(inventory));
        binding.call.setOnClickListener(call(inventory));
    }

    private View.OnClickListener call(final Inventory inventory) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "tel:" + inventory.getSupplierPhoneNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener delete(final Inventory inventory) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notDeleted) {
                    InventorySQLiteOpenHelper helper = new InventorySQLiteOpenHelper(DetailsActivity.this);
                    SQLiteDatabase database = helper.getWritableDatabase();
                    int rowsAffected = helper.delete(database, inventory.getId());
                    if(rowsAffected >= 1) {
                        Toast.makeText(DetailsActivity.this, R.string.successful_deletion, Toast.LENGTH_SHORT).show();
                        notDeleted = false;
                    }
                }
                else {
                    Toast.makeText(DetailsActivity.this, R.string.already_deleted, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(getString(R.string.detailes_deleted_extra), notDeleted);
        super.onSaveInstanceState(outState);
    }

    private View.OnClickListener increaseQuantity(final Inventory inventory) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InventorySQLiteOpenHelper helper = new InventorySQLiteOpenHelper(DetailsActivity.this);
                SQLiteDatabase database = helper.getWritableDatabase();
                int quantityIncrease = inventory.getQuantity() + 1;
                updateQuantity(helper, database, quantityIncrease, inventory);
            }
        };
    }

    private View.OnClickListener decreaseQuantity(final Inventory inventory) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InventorySQLiteOpenHelper helper = new InventorySQLiteOpenHelper(DetailsActivity.this);
                SQLiteDatabase database = helper.getWritableDatabase();
                int quantity = inventory.getQuantity();
                if(quantity >= 1) {
                    int quantityDecrease = quantity - 1;
                    updateQuantity(helper, database, quantityDecrease, inventory);
                }
            }
        };
    }

    private void checkOutOfStock(int quantityDecrease) {
        if(quantityDecrease <= 0) {
            binding.etQuantity.setText(getString(R.string.out_of_stock));
        }
    }

    private void updateQuantity(InventorySQLiteOpenHelper helper, SQLiteDatabase database, int quantityDecrease, Inventory inventory) {
        inventory.setQuantity(quantityDecrease);
        int rowsAffected = helper.updateQuantity(database, inventory.getQuantity(), inventory.getId());
        if (rowsAffected >= 1) {
            binding.setInventory(inventory);
            binding.executePendingBindings();
            checkOutOfStock(quantityDecrease);
        }
    }
}
