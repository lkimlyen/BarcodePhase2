package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderConfirmWindowEntity {
    @SerializedName("OutputID")
    @Expose
    private long outputID;

    @SerializedName("OrderID")
    @Expose
    private long orderId;

    @SerializedName("DepartmentIDIn")
    @Expose
    private int departmentIDIn;

    @SerializedName("DepartmentIDOut")
    @Expose
    private int departmentIDOut;

    @SerializedName("ProductSetDetailID")
    @Expose
    private long productSetDetailID;

    @SerializedName("ProductSetDetailName")
    @Expose
    private String productSetDetailName;

    @SerializedName("ProductSetDetailCode")
    @Expose
    private String productSetDetailCode;

    @SerializedName("ProductSetName")
    @Expose
    private String productSetName;

    @SerializedName("ProductSetID")
    @Expose
    private long productSetID;

    @SerializedName("BarCode")
    @Expose
    private String barcode;
    @SerializedName("IsLeft")
    @Expose
    private boolean isLeft;

    @SerializedName("IsRight")
    @Expose
    private boolean isRight;

    @SerializedName("CodeOrigin")
    @Expose
    private String codeOrigin;

    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("NumberTotalOrder")
    @Expose
    private int numberTotalOrder;

    @SerializedName("NumberOut")
    @Expose
    private int numberOut;

    @SerializedName("NumberConfirmed")
    @Expose
    private int numberConfirmed;

    @SerializedName("State")
    @Expose
    private boolean state;

    public long getOutputID() {
        return outputID;
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

    public long getProductSetDetailID() {
        return productSetDetailID;
    }

    public String getProductSetDetailName() {
        return productSetDetailName;
    }

    public String getProductSetDetailCode() {
        return productSetDetailCode;
    }

    public String getProductSetName() {
        return productSetName;
    }

    public long getProductSetID() {
        return productSetID;
    }

    public String getBarcode() {
        return barcode;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public boolean isRight() {
        return isRight;
    }

    public String getCodeOrigin() {
        return codeOrigin;
    }

    public String getType() {
        return type;
    }

    public int getNumberTotalOrder() {
        return numberTotalOrder;
    }

    public int getNumberOut() {
        return numberOut;
    }

    public int getNumberConfirmed() {
        return numberConfirmed;
    }

    public boolean isState() {
        return state;
    }
}
