package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StaffEntity {
    @SerializedName("StaffID")
    @Expose
    private int staffId;

    @SerializedName("Code")
    @Expose
    private String code;

    @SerializedName("Name")
    @Expose
    private String name;


    @SerializedName("DepartmentID")
    @Expose
    private int departmentId;

    public int getStaffId() {
        return staffId;
    }

    public String getCode() {
        return code;
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
