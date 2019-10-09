package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepartmentEntity {
    @SerializedName("DepartmentID")
    @Expose
    private int id;

    @SerializedName("DepartmentName")
    @Expose
    private String name;

    @SerializedName("OrderType")
    @Expose
    private int orderType;

    public DepartmentEntity() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOrderType() {
        return orderType;
    }

    @Override
    public String toString() {
        return name;
    }
}
