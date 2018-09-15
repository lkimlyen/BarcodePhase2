package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryEntity {
    @SerializedName("Module")
    @Expose
    private String module;

    @SerializedName("ProductID")
    @Expose
    private int productId;

    @SerializedName("CustomerName")
    @Expose
    private String customerName;

    @SerializedName("DateTime")
    @Expose
    private String dateTime;

    @SerializedName("DataList")
    @Expose
    private List<PackageEntity> packageList;

    public String getModule() {
        return module;
    }

    public int getProductId() {
        return productId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public List<PackageEntity> getPackageList() {
        return packageList;
    }
}
