package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ListModuleEntity implements Serializable {
    @SerializedName("Module")
    @Expose
    private String module;

    @SerializedName("ProductID")
    @Expose
    private long productId;

    @SerializedName("NumberRequired")
    @Expose
    private int numberRequired;

    @SerializedName("DataList")
    @Expose
    private List<PackageEntity> packageList;
    public String getModule() {
        return module;
    }

    public long getProductId() {
        return productId;
    }

    public int getNumberRequired() {
        return numberRequired;
    }

    public List<PackageEntity> getPackageList() {
        return packageList;
    }
}
