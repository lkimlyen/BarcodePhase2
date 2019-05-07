package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SOWarehouseEntity {
    @SerializedName("OrderID")
    @Expose
    private long orderId;

    @SerializedName("CodeSO")
    @Expose
    private String codeSO;

    @SerializedName("Percent")
    @Expose
    private float percent;

    public long getOrderId() {
        return orderId;
    }

    public String getCodeSO() {
        return codeSO;
    }

    public float getPercent() {
        return percent;
    }

    @Override
    public String toString() {
        return getCodeSO();
    }
}
