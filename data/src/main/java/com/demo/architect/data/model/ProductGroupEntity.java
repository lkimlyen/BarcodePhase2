package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductGroupEntity {
    @SerializedName("OrderID")
    @Expose
    private long orderId;

    @SerializedName("ProductDetailID")
    @Expose
    private long productDetailID;

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
    private double numberTotal;

    @SerializedName("Number")
    @Expose
    private double number;



    public long getOrderId() {
        return orderId;
    }

    public long getProductDetailID() {
        return productDetailID;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public double getNumber() {
        return number;
    }

    public String getProductDetailName() {
        return productDetailName;
    }

    public String getModule() {
        return Module;
    }

    public double getNumberTotal() {
        return numberTotal;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
