package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by uyminhduc on 10/23/16.
 */

public class BaseResponse {

    @SerializedName("ID")
    @Expose
    private int ID;

    @SerializedName("Number")
    @Expose
    private int Number;

    @SerializedName("CodeScan")
    @Expose
    private String CodeScan;

    @SerializedName("Status")
    @Expose
    private int Status;

    @SerializedName("Description")
    @Expose
    private String Description;


    public int getStatus() {
        return Status;
    }

    public String getDescription() {
        return Description;
    }

    public int getNumber() {
        return Number;
    }

    public int getID() {
        return ID;
    }

    public String getCodeScan() {
        return CodeScan;
    }
}
