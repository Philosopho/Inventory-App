package com.krinotech.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import static android.provider.BaseColumns._ID;
import static com.krinotech.inventoryapp.InventoryContract.CLOSE_PAREN;
import static com.krinotech.inventoryapp.InventoryContract.CREATE_TABLE;
import static com.krinotech.inventoryapp.InventoryContract.ID_VALUE;
import static com.krinotech.inventoryapp.InventoryContract.INTEGER_NOT_NULL;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.PRICE;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.PRODUCT_NAME;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.QUANTITY;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.SUPPLIER_NAME;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.SUPPLIER_PHONE_NUMBER;
import static com.krinotech.inventoryapp.InventoryContract.InventoryColumns.TABLE_NAME;
import static com.krinotech.inventoryapp.InventoryContract.OPEN_PAREN;
import static com.krinotech.inventoryapp.InventoryContract.TEXT;
import static com.krinotech.inventoryapp.InventoryContract.TEXT_NOT_NULL;

public class InventorySQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String TAG = InventorySQLiteOpenHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "InventoryDB.db";
    public static final int DATABASE_VERSION = 1;

    public InventorySQLiteOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String textRemoveComma = TEXT.replace(",", "");
        final String FINAL_TEXT_VALUE = " " + textRemoveComma.trim();
        final String SQL_CREATE_ENTRIES =
                CREATE_TABLE + InventoryContract.InventoryColumns.TABLE_NAME + OPEN_PAREN
                        + _ID + ID_VALUE
                        + PRODUCT_NAME + TEXT_NOT_NULL
                        + PRICE + INTEGER_NOT_NULL
                        + QUANTITY + INTEGER_NOT_NULL
                        + SUPPLIER_NAME + TEXT
                        + SUPPLIER_PHONE_NUMBER + FINAL_TEXT_VALUE + CLOSE_PAREN;
        Log.d(TAG, SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public int updateQuantity(SQLiteDatabase writeableDb, int quantity, long id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(QUANTITY, quantity);
        String selection = _ID + " = ?";
        String[] whereClause = {String.valueOf(id)};
        try {
            return writeableDb.update(TABLE_NAME, contentValues, selection, whereClause);
        }
        finally {
            writeableDb.close();
        }
    }

    public int delete(SQLiteDatabase writeableDb, long id) {
        String selection = _ID + " = ?";
        String[] whereClause = {String.valueOf(id)};
        try {
            return writeableDb.delete(TABLE_NAME, selection, whereClause);
        }
        finally {
            writeableDb.close();
        }
    }

    public long insert(SQLiteDatabase writeableDb,
                       String productName,
                       double price,
                       int quantity,
                       String supplierName,
                       String supplierContact) {
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

    public int deleteAll(SQLiteDatabase writeableDb) {
        try {
            return writeableDb.delete(TABLE_NAME, "1", null);
        }
        finally {
            writeableDb.close();
        }
    }

    public long update(SQLiteDatabase writeableDb, long id, String productName, double price, int quantity, String supplierName, String supplierContact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_NAME, productName);
        contentValues.put(PRICE, price);
        contentValues.put(QUANTITY, quantity);
        contentValues.put(SUPPLIER_NAME, supplierName);
        contentValues.put(SUPPLIER_PHONE_NUMBER, supplierContact);
        String selection = _ID + " = ?";
        String[] whereClause = {String.valueOf(id)};
        try {
            return writeableDb.update(TABLE_NAME, contentValues, selection, whereClause);
        }
        finally {
            writeableDb.close();
        }
    }
}
