package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupSetEntity {
    @SerializedName("PackCode")
    @Expose
    private String packCode;

    @SerializedName("NumberOnSet")
    @Expose
    private int numberOnSet;

    @SerializedName("NumberSetOnPack")
    @Expose
    private int numberSetOnPack;
    @SerializedName("NumberRequest")
    @Expose
    private int numberTotal;


    public String getPackCode() {
        return packCode;
    }

    public int getNumberOnSet() {
        return numberOnSet;
    }

    public int getNumberSetOnPack() {
        return numberSetOnPack;
    }

    public int getNumberTotal() {

        return numberTotal;
    }
}
