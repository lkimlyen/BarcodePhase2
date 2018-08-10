package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderConfirmEntity {
    @SerializedName("OutputProductDetailID")
    @Expose
    private int outputProductDetailID;
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

    @SerializedName("TimesOutput")
    @Expose
    private int timesOutput;

    @SerializedName("ListInput")
    @Expose
    private List<NumberInput> listInput;

    @SerializedName("ListDepartmentIDSuccess")
    @Expose
    private List<Integer> ListDepartmentID;

}
