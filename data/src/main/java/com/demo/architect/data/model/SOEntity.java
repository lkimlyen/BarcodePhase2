package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SOEntity {
    @SerializedName("OrderID")
    @Expose
    private int orderId;

    @SerializedName("CodeSO")
    @Expose
    private String codeSO;

    @SerializedName("OrderType")
    @Expose
    private int orderType;

    @SerializedName("CustomerName")
    @Expose
    private String customerName;

    @SerializedName("ListTimesInput")
    @Expose
    private List<Integer> listTimesInput;

    @SerializedName("ListTimesOutput")
    @Expose
    private List<Integer> listTimesOutput;

    public SOEntity() {
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCodeSO() {
        return codeSO;
    }

    public int getOrderType() {
        return orderType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<Integer> getListTimesInput() {
        return listTimesInput;
    }

    public List<Integer> getListTimesOutput() {
        return listTimesOutput;
    }

    @Override
    public String toString() {
        return codeSO;
    }
}
