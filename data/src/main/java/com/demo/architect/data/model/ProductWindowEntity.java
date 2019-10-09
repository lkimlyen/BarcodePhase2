package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductWindowEntity {
    @SerializedName("OrderID")
    @Expose
    private long orderId;

    @SerializedName("DepartmentID")
    @Expose
    private int departmentID;

    @SerializedName("ProductSetID")
    @Expose
    private long productSetID;

    @SerializedName("ProductSetName")
    @Expose
    private String productSetName;
    @SerializedName("ProductSetDetailID")
    @Expose
    private long productSetDetailID;

    @SerializedName("ProductSetDetailName")
    @Expose
    private String productSetDetailName;

    @SerializedName("ProductSetDetailCode")
    @Expose
    private String productSetDetailCode;

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
    @SerializedName("NumberTotalInput")
    @Expose
    private int numberTotalInput;

    @SerializedName("NumberSuccess")
    @Expose
    private int numberSuccess;

    @SerializedName("NumberWaitting")
    @Expose
    private int numberWaitting;

    @SerializedName("ListDepartmentID")
    @Expose
    private List<Integer> ListDepartmentID;

    public long getOrderId() {
        return orderId;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public long getProductSetID() {
        return productSetID;
    }

    public String getProductSetName() {
        return productSetName;
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

    public int getNumberTotalInput() {
        return numberTotalInput;
    }

    public int getNumberSuccess() {
        return numberSuccess;
    }

    public int getNumberWaitting() {
        return numberWaitting;
    }

    public List<Integer> getListDepartmentID() {
        return ListDepartmentID;
    }
}
