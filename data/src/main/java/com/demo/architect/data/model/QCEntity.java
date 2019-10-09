package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QCEntity {

    @SerializedName("QCID")
    @Expose
    private int id;

    @SerializedName("Code")
    @Expose
    private String code;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("DauMoc")
    @Expose
    private String dauMoc;

    @SerializedName("Phone")
    @Expose
    private String phone;

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDauMoc() {
        return dauMoc;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return code + " - " + name;
    }
}
