package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductWarehouseEntity {
    @SerializedName("ProductID")
    @Expose
    private long productId;

    @SerializedName("ProductCode")
    @Expose
    private String productCode;

    @SerializedName("ProductName")
    @Expose
    private String productName;

    @SerializedName("Pack")
    @Expose
    private String pack;

    @SerializedName("NumberTotal")
    @Expose
    private int numberTotal;

    @SerializedName("NumberSucess")
    @Expose
    private int numberSucess;

    @SerializedName("NumberWaiting")
    @Expose
    private int numberWaiting;

    public long getProductId() {
        return productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public String getPack() {
        return pack;
    }

    public int getNumberTotal() {
        return numberTotal;
    }

    public int getNumberSucess() {
        return numberSucess;
    }

    public int getNumberWaiting() {
        return numberWaiting;
    }
}
