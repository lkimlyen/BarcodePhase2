package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CodePackEntity {
    @SerializedName("PackCode")
    @Expose
    private String packCode;

    @SerializedName("STTPack")
    @Expose
    private String sttPack;

    public String getPackCode() {
        return packCode;
    }

    public String getSttPack() {
        return sttPack;
    }

    @Override
    public String toString() {
        return sttPack;
    }
}
