package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderConfirmEntity {
    @SerializedName("MasterOutputID")
    @Expose
    private int masterOutputID;

    @SerializedName("OrderID")
    @Expose
    private int orderId;

    @SerializedName("DepartmentIDIn")
    @Expose
    private int departmentIDIn;

    @SerializedName("DepartmentIDOut")
    @Expose
    private int departmentIDOut;

    @SerializedName("ProductDetailID")
    @Expose
    private int productDetailID;

    @SerializedName("ProductDetailName")
    @Expose
    private String productDetailName;

    @SerializedName("ProductDetailCode")
    @Expose
    private String productDetailCode;

    @SerializedName("Module")
    @Expose
    private String module;

    @SerializedName("Barcode")
    @Expose
    private String barcode;

    @SerializedName("NumberTotalOrder")
    @Expose
    private int numberTotalOrder;

    @SerializedName("ListInputConfirmed")
    @Expose
    private List<NumberInputConfirm> listInputConfirmed;

    @SerializedName("NumberOut")
    @Expose
    private int numberOut;

    @SerializedName("LastTimeGetData")
    @Expose
    private String lastTimeGetData;

    public int getMasterOutputID() {
        return masterOutputID;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getDepartmentIDIn() {
        return departmentIDIn;
    }

    public int getDepartmentIDOut() {
        return departmentIDOut;
    }

    public int getProductDetailID() {
        return productDetailID;
    }

    public String getProductDetailName() {
        return productDetailName;
    }

    public String getProductDetailCode() {
        return productDetailCode;
    }

    public String getModule() {
        return module;
    }

    public String getBarcode() {
        return barcode;
    }

    public int getNumberTotalOrder() {
        return numberTotalOrder;
    }

    public List<NumberInputConfirm> getListInputConfirmed() {
        return listInputConfirmed;
    }

    public int getNumberOut() {
        return numberOut;
    }

    public String getLastTimeGetData() {
        return lastTimeGetData;
    }
}