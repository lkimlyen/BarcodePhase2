package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductEntity {
    @SerializedName("OrderID")
    @Expose
    private int orderId;

    @SerializedName("DepartmentID")
    @Expose
    private int departmentID;

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

    @SerializedName("ListDepartmentID")
    @Expose
    private List<Integer> ListDepartmentID;

    public ProductEntity() {
    }

    public int getOrderId() {
        return orderId;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public int getProductDetailID() {
        return productDetailID;
    }

    public String getProductDetailName() {
        return productDetailName;
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

    public int getTimesOutput() {
        return timesOutput;
    }

    public List<NumberInput> getListInput() {
        return listInput;
    }

    public List<Integer> getListDepartmentID() {
        return ListDepartmentID;
    }

    public String getProductDetailCode() {
        return productDetailCode;
    }

    @Override
    public String toString() {
        return productDetailName;
    }

}
