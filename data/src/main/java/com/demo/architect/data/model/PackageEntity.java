package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PackageEntity implements Serializable {

    @SerializedName("PackageID")
    @Expose
    private long packageId;

    @SerializedName("PackCode")
    @Expose
    private String packCode;

    @SerializedName("STTPack")
    @Expose
    private String serialPack;

    @SerializedName("Current")
    @Expose
    private double numberScan;

    @SerializedName("Total")
    @Expose
    private double total;

    @SerializedName("DetailDataList")
    @Expose
    private List<ProductPackagingEntity> productPackagingEntityList;

    public long getPackageId() {
        return packageId;
    }

    public String getPackCode() {
        return packCode;
    }

    public String getSerialPack() {
        return serialPack;
    }

    public double getTotal() {
        return total;
    }

    public List<ProductPackagingEntity> getProductPackagingEntityList() {
        return productPackagingEntityList;
    }

    public double getNumberScan() {
        return numberScan;
    }
}
