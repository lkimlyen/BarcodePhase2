package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PackageEntity {

    @SerializedName("PackageID")
    @Expose
    private int packageId;

    @SerializedName("PackCode")
    @Expose
    private String packCode;

    @SerializedName("STTPack")
    @Expose
    private String serialPack;

    @SerializedName("Total")
    @Expose
    private int total;

    @SerializedName("DetailDataList")
    @Expose
    private List<ProductPackagingEntity> productPackagingEntityList;

    public int getPackageId() {
        return packageId;
    }

    public String getPackCode() {
        return packCode;
    }

    public String getSerialPack() {
        return serialPack;
    }

    public int getTotal() {
        return total;
    }

    public List<ProductPackagingEntity> getProductPackagingEntityList() {
        return productPackagingEntityList;
    }
}
