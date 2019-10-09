package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NumberInputConfirm {
    @SerializedName("NumberConfirmed")
    @Expose
    private int numberConfirmed;

    @SerializedName("TimesInput")
    @Expose
    private int timesInput;

    public int getNumberConfirmed() {
        return numberConfirmed;
    }

    public int getTimesInput() {
        return timesInput;
    }
}
