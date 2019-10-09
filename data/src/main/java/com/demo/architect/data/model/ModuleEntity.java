package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModuleEntity {
    @SerializedName("ProductID")
    @Expose
    private long productId;

    @SerializedName("ModuleName")
    @Expose
    private String moduleName;

    public long getProductId() {
        return productId;
    }

    public String getModuleName() {
        return moduleName;
    }

    @Override
    public String toString() {
        return moduleName;
    }
}
