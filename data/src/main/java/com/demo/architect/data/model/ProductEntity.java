package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    @SerializedName("Module")
    @Expose
    private String module;

    @SerializedName("Barcode")
    @Expose
    private String barcode;

    @SerializedName("NumberTotal")
    @Expose
    private int numberTotal;

    @SerializedName("NumberSuccess")
    @Expose
    private int numberSuccess;

    @SerializedName("NumberWaitting")
    @Expose
    private int numberWaitting;

    @SerializedName("TimesOutput")
    @Expose
    private int timesOutput;

    @SerializedName("Cat")
    @Expose
    private boolean cat;

    @SerializedName("ChiMay")
    @Expose
    private boolean chimay;

    @SerializedName("ChiTay")
    @Expose
    private boolean chitay;

    @SerializedName("Son")
    @Expose
    private boolean son;

    @SerializedName("LapRap")
    @Expose
    private boolean laprap;

    @SerializedName("Nguoi")
    @Expose
    private boolean nguoi;

    @SerializedName("Da")
    @Expose
    private boolean da;

    @SerializedName("BaoBi")
    @Expose
    private boolean baobi;

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

    public int getNumberTotal() {
        return numberTotal;
    }

    public int getNumberSuccess() {
        return numberSuccess;
    }

    public int getNumberWaitting() {
        return numberWaitting;
    }

    public int getTimesOutput() {
        return timesOutput;
    }

    public boolean isCat() {
        return cat;
    }

    public boolean isChimay() {
        return chimay;
    }

    public boolean isChitay() {
        return chitay;
    }

    public boolean isSon() {
        return son;
    }

    public boolean isLaprap() {
        return laprap;
    }

    public boolean isNguoi() {
        return nguoi;
    }

    public boolean isDa() {
        return da;
    }

    public boolean isBaobi() {
        return baobi;
    }
}
