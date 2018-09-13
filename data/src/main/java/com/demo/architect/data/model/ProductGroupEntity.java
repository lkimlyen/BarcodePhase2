package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductGroupEntity {
    @SerializedName("OrderID")
    @Expose
    private int orderId;

    @SerializedName("ProductDetailID")
    @Expose
    private int productDetailID;

    @SerializedName("ProductDetailName")
    @Expose
    private String productDetailName;

    @SerializedName("Module")
    @Expose
    private String Module;

    @SerializedName("GroupCode")
    @Expose
    private String groupCode;

    @SerializedName("NumberTotalDetail")
    private int numberTotal;

    @SerializedName("Number")
    @Expose
    private int number;



    public int getOrderId() {
        return orderId;
    }

    public int getProductDetailID() {
        return productDetailID;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public int getNumber() {
        return number;
    }

    public String getProductDetailName() {
        return productDetailName;
    }

    public String getModule() {
        return Module;
    }

    public int getNumberTotal() {
        return numberTotal;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
