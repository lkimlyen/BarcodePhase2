package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderConfirmEntity {
    @SerializedName("MasterOutputID")
    @Expose
    private long masterOutputID;

    @SerializedName("OrderID")
    @Expose
    private long orderId;

    @SerializedName("DepartmentIDIn")
    @Expose
    private int departmentIDIn;

    @SerializedName("DepartmentIDOut")
    @Expose
    private int departmentIDOut;

    @SerializedName("ProductDetailID")
    @Expose
    private long productDetailID;

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

    @SerializedName("State")
    @Expose
    private boolean state;

    @SerializedName("LastIDOutput")
    @Expose
    private long lastIDOutput;
    public long getMasterOutputID() {
        return masterOutputID;
    }

    public long getOrderId() {
        return orderId;
    }

    public int getDepartmentIDIn() {
        return departmentIDIn;
    }

    public int getDepartmentIDOut() {
        return departmentIDOut;
    }

    public long getProductDetailID() {
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

    public boolean isState() {
        return state;
    }

    public long getLastIDOutput() {
        return lastIDOutput;
    }
}
