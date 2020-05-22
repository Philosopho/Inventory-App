package com.krinotech.inventoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.krinotech.inventoryapp.databinding.ActivityEditorBinding;

import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.PRICE;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.PRODUCT_NAME;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.QUANTITY;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.SUPPLIER_NAME;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.SUPPLIER_PHONE_NUMBER;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.TABLE_NAME;

public class EditorActivity extends AppCompatActivity {
    private ActivityEditorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editor);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(getString(R.string.editor_activity_title));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemId = item.getItemId();

        if (menuItemId == R.id.save) {
            String productName = binding.etProductName.getText().toString();
            String priceS = binding.etPrice.getText().toString();
            String quantityS = binding.etQuantity.getText().toString();
            if(notValid(productName, priceS, quantityS)) {
                Toast.makeText(this, R.string.not_valid_insert, Toast.LENGTH_SHORT).show();
            }
            else {
                double price = Double.parseDouble(priceS);
                int quantity = Integer.parseInt(quantityS);
                String supplierName = binding.etSupplierName.getText().toString();
                String supplierPhoneNumber = binding.etSupplierPhoneNumber.getText().toString();
                if(supplierName.isEmpty()) {
                    supplierName = getString(R.string.no_information);
                }
                if(supplierPhoneNumber.isEmpty()) {
                    supplierPhoneNumber = getString(R.string.no_information);
                }
                insertData(productName, price, quantity, supplierName, supplierPhoneNumber);
                Toast.makeText(this, R.string.insert_success, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }

    private boolean notValid(String productName, String price, String quantity) {
        return productName.isEmpty() || price.isEmpty() || quantity.isEmpty();
    }

    private long insertData(String productName,
                            double price,
                            int quantity,
                            String supplierName,
                            String supplierContact) {

        SQLiteOpenHelper inventoryHelper = new InventorySQLiteOpenHelper(this);
        SQLiteDatabase writeableDb = inventoryHelper.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PRODUCT_NAME, productName);
            contentValues.put(PRICE, price);
            contentValues.put(QUANTITY, quantity);
            contentValues.put(SUPPLIER_NAME, supplierName);
            contentValues.put(SUPPLIER_PHONE_NUMBER, supplierContact);
            return writeableDb.insert(TABLE_NAME, null, contentValues);
        }
        finally {
            writeableDb.close();
        }
    }
}
