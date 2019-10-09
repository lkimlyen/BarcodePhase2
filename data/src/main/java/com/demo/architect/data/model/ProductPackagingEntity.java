package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductPackagingEntity  implements Serializable{
    @SerializedName("BarCode")
    @Expose
    private String barcode;

    @SerializedName("ProductDetailID")
    @Expose
    private long id;

    @SerializedName("ProductName")
    @Expose
    private String productName;

    @SerializedName("ColorCode")
    @Expose
    private String productColor;

    @SerializedName("ProductLong")
    @Expose
    private double length;

    @SerializedName("ProductHeight")
    @Expose
    private double height;

    @SerializedName("ProductDeep")
    @Expose
    private double width;

    @SerializedName("Quantity")
    @Expose
    private int number;

    public String getProductName() {
        return productName;
    }

    public double getLength() {
        return length;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public int getNumber() {
        return number;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public long getId() {
        return id;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setId(long id) {
        this.id = id;
    }
}
