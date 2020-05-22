package com.krinotech.inventoryapp;

public class Inventory {

    private long id;
    private String productName;
    private double price;
    private int quantity;
    private String supplierName;
    private String supplierPhoneNumber;

    public Inventory(long id, String productName, double price, int quantity, String supplierName, String supplierPhoneNumber) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.supplierName = supplierName;
        this.supplierPhoneNumber = supplierPhoneNumber;
    }

    public long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public String getPriceFormatted() {
        return CurrencyFormatter.format(price);
    }

    public int getQuantity() {
        return quantity;
    }

    public String getQuantityFormatted() {
        return String.valueOf(quantity);
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierPhoneNumber() {
        return supplierPhoneNumber;
    }
}
