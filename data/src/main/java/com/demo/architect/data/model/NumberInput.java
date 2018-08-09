package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NumberInput {
    @SerializedName("NumberTotalInput")
    @Expose
    private int numberTotalInput;

    @SerializedName("NumberSuccess")
    @Expose
    private int numberSuccess;

    @SerializedName("NumberWaitting")
    @Expose
    private int numberWaitting;

    @SerializedName("TimesInput")
    @Expose
    private int timesInput;

    public int getNumberTotalInput() {
        return numberTotalInput;
    }

    public int getNumberSuccess() {
        return numberSuccess;
    }

    public int getNumberWaitting() {
        return numberWaitting;
    }

    public int getTimesInput() {
        return timesInput;
    }
}
