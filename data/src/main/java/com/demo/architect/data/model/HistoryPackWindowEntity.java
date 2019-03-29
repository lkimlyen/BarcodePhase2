package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryPackWindowEntity {
    @SerializedName("MasterID")
    @Expose
    private long masterId;

    @SerializedName("PackName")
    @Expose
    private String packName;

    @SerializedName("PackCode")
    @Expose
    private String packCode;

    @SerializedName("NumberSetOnPack")
    @Expose
    private int numberSetOnPack;

    @SerializedName("BarCode")
    @Expose
    private String barcode;

    @SerializedName("BarCodeName")
    @Expose
    private String barcodeName;

    public long getMasterId() {
        return masterId;
    }

    public String getPackName() {
        return packName;
    }

    public String getPackCode() {
        return packCode;
    }

    public int getNumberSetOnPack() {
        return numberSetOnPack;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getBarcodeName() {
        return barcodeName;
    }
}
