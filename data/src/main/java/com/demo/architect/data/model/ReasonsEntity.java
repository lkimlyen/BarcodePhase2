package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReasonsEntity {
    @SerializedName("ReasonID")
    @Expose
    private int reasonId;

    @SerializedName("Reason")
    @Expose
    private String reason;

    public int getReasonId() {
        return reasonId;
    }

    public String getReason() {
        return reason;
    }
}
