package com.krinotech.inventoryapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Inventory implements Parcelable {

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

    protected Inventory(Parcel in) {
        id = in.readLong();
        productName = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        supplierName = in.readString();
        supplierPhoneNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(productName);
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeString(supplierName);
        dest.writeString(supplierPhoneNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Inventory> CREATOR = new Creator<Inventory>() {
        @Override
        public Inventory createFromParcel(Parcel in) {
            return new Inventory(in);
        }

        @Override
        public Inventory[] newArray(int size) {
            return new Inventory[size];
        }
    };

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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
