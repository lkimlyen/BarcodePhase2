package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SetWindowEntity {
    @SerializedName("ProductSetID")
    @Expose
    private long productSetId;

    @SerializedName("ProductSetName")
    @Expose
    private String productSetName;

    public long getProductSetId() {
        return productSetId;
    }

    public String getProductSetName() {
        return productSetName;
    }

    @Override
    public String toString() {
        return getProductSetName();
    }
}
