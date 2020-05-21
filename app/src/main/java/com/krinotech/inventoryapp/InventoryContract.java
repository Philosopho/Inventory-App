package com.krinotech.inventoryapp;

import android.provider.BaseColumns;

public final class InventoryContract {
    public static final String CREATE_TABLE = "CREATE TABLE ";
    public static final String OPEN_PAREN = " (";
    public static final String CLOSE_PAREN = ")";
    public static final String ID_VALUE = " INTEGER PRIMARY KEY AUTOINCREMENT, ";
    public static final String TEXT_NOT_NULL = " TEXT NOT NULL, ";
    public static final String TEXT = " TEXT, ";
    public static final String INTEGER_NOT_NULL = " INTEGER NOT NULL, ";

    private InventoryContract() {}

    protected interface InventoryColumns extends BaseColumns {
        String TABLE_NAME = "inventory";
        String PRODUCT_NAME = "product_name";
        String PRICE = "price";
        String QUANTITY = "quantity";
        String SUPPLIER_NAME = "supplier_name";
        String SUPPLIER_PHONE_NUMBER = "supplier_phone_number";
    }
}
