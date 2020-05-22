package com.krinotech.inventoryapp;

import java.text.NumberFormat;
import java.util.Currency;

class CurrencyFormatter {

    public static String format(double price) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        return format.format(price);
    }
}
