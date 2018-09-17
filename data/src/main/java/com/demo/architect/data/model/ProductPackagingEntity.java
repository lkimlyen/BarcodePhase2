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
    private int id;

    @SerializedName("ProductName")
    @Expose
    private String productName;

    @SerializedName("ColorCode")
    @Expose
    private String productColor;

    @SerializedName("ProductLong")
    @Expose
    private int length;

    @SerializedName("ProductHeight")
    @Expose
    private int height;

    @SerializedName("ProductDeep")
    @Expose
    private int width;

    @SerializedName("Quantity")
    @Expose
    private int number;

    public String getProductName() {
        return productName;
    }

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
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

    public int getId() {
        return id;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setId(int id) {
        this.id = id;
    }
}
