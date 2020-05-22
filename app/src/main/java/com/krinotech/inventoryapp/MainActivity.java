package com.krinotech.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;

import com.krinotech.inventoryapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.PRICE;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.PRODUCT_NAME;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.QUANTITY;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.SUPPLIER_NAME;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.SUPPLIER_PHONE_NUMBER;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.TABLE_NAME;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private InventoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        displayDatabase();
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayDatabase() {
        SQLiteOpenHelper databaseHelper = new InventorySQLiteOpenHelper(this);

        SQLiteDatabase readableDb = databaseHelper.getReadableDatabase();
        Cursor cursor = readableDb.query(
                TABLE_NAME, null, null,
                null, null, null, null
        );
        try {
            List<Inventory> inventories = getInventories(cursor);
            setUpAdapter(inventories);
        } finally {
            cursor.close();
            readableDb.close();
        }
    }

    private List<Inventory> getInventories(Cursor cursor) {
        List<Inventory> inventories = new ArrayList<>();

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(_ID));
            String productName =
                    cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
            double price = cursor.getDouble(cursor.getColumnIndex(PRICE));
            int quantity = cursor.getInt(cursor.getColumnIndex(QUANTITY));
            String supplierName =
                    cursor.getString(cursor.getColumnIndex(SUPPLIER_NAME));
            String supplierContact =
                    cursor.getString(cursor.getColumnIndex(SUPPLIER_PHONE_NUMBER));

            inventories.add(new Inventory(id, productName, price, quantity, supplierName, supplierContact));
        }
        return inventories;
    }

    private void setUpAdapter(List<Inventory> inventories) {
        adapter = new InventoryAdapter(inventories);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        binding.rvMain.setHasFixedSize(true);
        binding.rvMain.setLayoutManager(linearLayoutManager);
        binding.rvMain.setAdapter(adapter);
    }

    private String queryColumnValueWithId(SQLiteDatabase database, String columnName, long id) {
        String value = "";
        Cursor cursor = database.rawQuery(
                "SELECT " + columnName + " FROM " + TABLE_NAME + " WHERE " + _ID + " == " + id + ";",
                null);
        try{
            if(!cursor.isFirst()) {
                cursor.moveToFirst();
            }
            value = cursor.getString(cursor.getColumnIndex(columnName));
            return value;
        } finally {
            cursor.close();
        }
    }
}
