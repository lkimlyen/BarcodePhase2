package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryEntity {
    @SerializedName("PackageID")
    @Expose
    private int packageId;

    @SerializedName("CustomerName")
    @Expose
    private String customerName;

    @SerializedName("DateTime")
    @Expose
    private String dateTime;

    @SerializedName("Total")
    @Expose
    private int total;

    @SerializedName("DataList")
    @Expose
    private List<ProductPackagingEntity> list;


    public int getPackageId() {
        return packageId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getTotal() {
        return total;
    }

    public int totalQuantity() {
        int total = 0;
        for (ProductPackagingEntity productPackagingEntity : list) {
            total += productPackagingEntity.getNumber();
        }
        return total;
    }

    public List<ProductPackagingEntity> getList() {
        return list;
    }
}
