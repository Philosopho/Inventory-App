package com.krinotech.inventoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.krinotech.inventoryapp.databinding.ActivityEditorBinding;

public class EditorActivity extends AppCompatActivity {
    private ActivityEditorBinding binding;
    private long editingId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editor);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Inventory inventory = getIntent().getParcelableExtra(getString(R.string.inventory_extra));
        checkSavedInstanceState(savedInstanceState);
        if(inventory != null) {

            editingId = inventory.getId();
            String productName = inventory.getProductName();
            String priceS = String.valueOf(inventory.getPrice());
            String quantityS = inventory.getQuantityFormatted();
            String supplierName = inventory.getSupplierName();
            String supplierPhoneNumber = inventory.getSupplierPhoneNumber();

            bindData(productName, priceS, quantityS, supplierName, supplierPhoneNumber);
        }


        setTitle(getString(R.string.editor_activity_title));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        String productName = binding.etProductName.getText().toString().trim();
        String priceS = binding.etPrice.getText().toString().trim();
        String quantityS = binding.etQuantity.getText().toString().trim();
        String supplierName = binding.etSupplierName.getText().toString().trim();
        String supplierPhoneNumber = binding.etSupplierPhoneNumber.getText().toString().trim();

        outState.putString(getString(R.string.product_name_extra), productName);
        outState.putString(getString(R.string.price_extra), priceS);
        outState.putString(getString(R.string.quantity_extra), quantityS);
        outState.putString(getString(R.string.supplier_extra), supplierName);
        outState.putString(getString(R.string.supplier_contact_extra), supplierPhoneNumber);
        outState.putLong(getString(R.string.editing_id_extra), editingId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemId = item.getItemId();

        if (menuItemId == R.id.save) {
            saveChanges();
        }
        else if(menuItemId == R.id.deleteAll) {
            promptChoice();
        }
        else {
            NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }

    private void saveChanges() {
        String productName = binding.etProductName.getText().toString().trim();
        String priceS = binding.etPrice.getText().toString().trim();
        String quantityS = binding.etQuantity.getText().toString().trim();
        String supplierName = binding.etSupplierName.getText().toString().trim();
        String supplierPhoneNumber = binding.etSupplierPhoneNumber.getText().toString().trim();

        if(notValid(productName, priceS, quantityS, supplierName, supplierPhoneNumber)) {
            Toast.makeText(this, R.string.not_valid_insert, Toast.LENGTH_SHORT).show();
        }
        else {
            double price = Double.parseDouble(priceS);
            int quantity = Integer.parseInt(quantityS);

            if(editingId != -1) {
                long rowsAffected = updateData(editingId, productName, price, quantity, supplierName, supplierPhoneNumber);

                if(rowsAffected <= 0) {
                    Toast.makeText(this, R.string.update_failure, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, R.string.update_success, Toast.LENGTH_SHORT).show();
                }
            }
            else {
                long newId = insertData(productName, price, quantity, supplierName, supplierPhoneNumber);

                if(newId == -1) {
                    Toast.makeText(this, R.string.unsuccessful_insert, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, R.string.insert_success, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void promptChoice() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE) {
                    InventorySQLiteOpenHelper inventoryHelper = new InventorySQLiteOpenHelper(EditorActivity.this);
                    SQLiteDatabase writeableDb = inventoryHelper.getWritableDatabase();
                    int rowsAffected = inventoryHelper.deleteAll(writeableDb);
                    if(rowsAffected >= 1) {
                        Toast.makeText(EditorActivity.this, R.string.success_all_delete, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.warning_all_delete).setPositiveButton(R.string.yes_delete_all, dialogClickListener)
                .setNegativeButton(R.string.no_delete_all, dialogClickListener).show();
    }

    private void checkSavedInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            editingId = savedInstanceState.getLong(getString(R.string.editing_id_extra));
            String productName = savedInstanceState.getString(getString(R.string.product_name_extra));
            String price = savedInstanceState.getString(getString(R.string.price_extra));
            String quantity = savedInstanceState.getString(getString(R.string.quantity_extra));
            String supplier = savedInstanceState.getString(getString(R.string.supplier_extra));
            String contact = savedInstanceState.getString(getString(R.string.supplier_contact_extra));

            bindData(productName, price, quantity, supplier, contact);
        }
    }

    private void bindData(String productName, String price, String quantity, String supplier, String contact) {
        binding.etProductName.setText(productName);
        binding.etPrice.setText(price);
        binding.etQuantity.setText(quantity);
        binding.etSupplierName.setText(supplier);
        binding.etSupplierPhoneNumber.setText(contact);
    }

    private boolean notValid(String productName, String price, String quantity, String supplier, String supplierContact) {
        return productName.isEmpty() || price.isEmpty() || quantity.isEmpty() ||
                supplier.isEmpty() || supplierContact.isEmpty();
    }

    private long insertData(String productName,
                            double price,
                            int quantity,
                            String supplierName,
                            String supplierContact) {

        InventorySQLiteOpenHelper inventoryHelper = new InventorySQLiteOpenHelper(this);
        SQLiteDatabase writeableDb = inventoryHelper.getWritableDatabase();
        return inventoryHelper.insert(
                writeableDb, productName, price,
                quantity, supplierName, supplierContact
        );
    }

    private long updateData(long id, String productName,
                            double price,
                            int quantity,
                            String supplierName,
                            String supplierContact) {

        InventorySQLiteOpenHelper inventoryHelper = new InventorySQLiteOpenHelper(this);
        SQLiteDatabase writeableDb = inventoryHelper.getWritableDatabase();
        return inventoryHelper.update(writeableDb, id, productName, price,
                quantity, supplierName, supplierContact
        );
    }
}
