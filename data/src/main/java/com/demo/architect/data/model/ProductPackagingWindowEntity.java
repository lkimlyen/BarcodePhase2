package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductPackagingWindowEntity implements Serializable{
    @SerializedName("OrderID")
    @Expose
    private long orderId;
    @SerializedName("ProductSetID")
    @Expose
    private long productSetId;
    @SerializedName("BarCode")
    @Expose
    private String barcode;

    @SerializedName("ProductSetDetailID")
    @Expose
    private long productSetDetailId;

    @SerializedName("ProductSetDetailName")
    @Expose
    private String productSetDetailName;

    @SerializedName("ProductSetDetailCode")
    @Expose
    private String productSetDetailCode;

    @SerializedName("CodeOrigin")
    @Expose
    private String codeOrigin;

    @SerializedName("ProductSetDetailLong")
    @Expose
    private double length;

    @SerializedName("ProductSetDetailWide")
    @Expose
    private double height;

    @SerializedName("ProductSetDetailDeep")
    @Expose
    private double width;

    @SerializedName("NumberTotal")
    @Expose
    private int numberTotal;

    @SerializedName("NumberScaned")
    @Expose
    private int numberScaned;


    @SerializedName("ListGroup")
    @Expose
    private List<GroupSetEntity> listGroup;

    public long getOrderId() {
        return orderId;
    }

    public long getProductSetId() {
        return productSetId;
    }

    public String getBarcode() {
        return barcode;
    }

    public long getProductSetDetailId() {
        return productSetDetailId;
    }

    public String getProductSetDetailName() {
        return productSetDetailName;
    }

    public String getProductSetDetailCode() {
        return productSetDetailCode;
    }

    public String getCodeOrigin() {
        return codeOrigin;
    }

    public double getLength() {
        return length;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public int getNumberTotal() {
        return numberTotal;
    }

    public int getNumberScaned() {
        return numberScaned;
    }

    public List<GroupSetEntity> getListGroup() {
        return listGroup;
    }
}
