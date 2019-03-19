package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MachineEntity {
    @SerializedName("MayID")
    @Expose
    private int id;

    @SerializedName("TenMay")
    @Expose
    private String name;

    @SerializedName("DepartmentID")
    @Expose
    private int departmentId;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    @Override
    public String toString() {
        return name;
    }
}
