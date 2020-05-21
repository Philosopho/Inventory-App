package com.krinotech.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.krinotech.inventoryapp.databinding.ActivityMainBinding;

import java.util.Arrays;

import static android.provider.BaseColumns._ID;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.PRICE;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.PRODUCT_NAME;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.QUANTITY;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.SUPPLIER_NAME;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.SUPPLIER_PHONE_NUMBER;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.TABLE_NAME;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        displayDatabase();
    }

    private void displayDatabase() {
        SQLiteOpenHelper databaseHelper = new InventorySQLiteOpenHelper(this);

        SQLiteDatabase readableDb = databaseHelper.getReadableDatabase();
        SQLiteDatabase writeableDb = databaseHelper.getWritableDatabase();
        long id = insertData(writeableDb);
        Cursor cursor = readableDb.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        try {
            binding.tvDatabaseResults.setText(R.string.column_names);
            binding.tvDatabaseResults.append(Arrays.toString(cursor.getColumnNames()));
            binding.tvDatabaseResults
                    .append(
                            "\n\n" + getString(
                            R.string.the_number_of_rows_in_the_database_1_s,
                            String.valueOf(cursor.getCount())
                            )
                    );

            String columnValue = queryColumnValueWithId(readableDb, PRODUCT_NAME, id);
            binding.tvDatabaseResults
                    .append("\n\n"
                                    + "Column: "
                                    + PRODUCT_NAME + "\n"
                                    + getString(R.string.column_values, columnValue));
        } finally {
            cursor.close();
            readableDb.close();
            writeableDb.close();
        }
    }

    private long insertData(SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_NAME, "Food");
        contentValues.put(PRICE, 12);
        contentValues.put(QUANTITY, 15);
        contentValues.put(SUPPLIER_NAME, "Amazon");
        contentValues.put(SUPPLIER_PHONE_NUMBER, "555-5555");

        return database.insert(TABLE_NAME, null, contentValues);
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
